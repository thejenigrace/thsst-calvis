package SimulatorVisualizer.controller;

import EnvironmentConfiguration.controller.EnvironmentConfigurator;
import EnvironmentConfiguration.model.engine.*;
import MainEditor.controller.WorkspaceController;
import MainEditor.model.AssemblyComponent;
import SimulatorVisualizer.model.SimulationState;
import javafx.application.Platform;

import java.util.*;

/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class SystemController {

    static long SIMULATION_DELAY = 1500;

    private EnvironmentConfigurator environment;
    private RegisterList registerList;
    private InstructionList instructionList;
    private Memory memory;
    private CALVISParser parser;
    private List<AssemblyComponent> observerList;
    private SimulationState state;
    private HashMap<String, CALVISInstruction> executionMap;
    private WorkspaceController workspaceController;
    private Thread thread;
    private Stack<EnvironmentConfigurator> stack;

    public SystemController(EnvironmentConfigurator ec, WorkspaceController wc){
        this.environment = ec;
        this.workspaceController = wc;
        this.parser = environment.getParser();
        this.registerList = environment.getRegisters();
        this.instructionList = environment.getInstructions();
        this.memory = environment.getMemory();
        this.observerList = new ArrayList<>();
        this.state = SimulationState.STOP;
        this.stack = new Stack<>();
    }

    public void attach(AssemblyComponent observer){
        observer.setSysCon(this);
        observerList.add(observer);
    }

    public void notifyAllObservers(CALVISInstruction currentLine, int lineNumber){
        if ( currentLine != null ) {
            for (AssemblyComponent a : observerList) {
                a.update(currentLine.toString(), lineNumber);
            }
        }
        else {
            for (AssemblyComponent a : observerList) {
                a.update(null, lineNumber);
            }
        }
    }

    public void refreshAllObservers(){
        observerList.forEach(AssemblyComponent::refresh);
    }

    public RegisterList getRegisterState(){
        return this.registerList;
    }

    public Memory getMemoryState(){
        return this.memory;
    }

    public void play(String code) {
        switch (this.state) {
            case PLAY: // System is running, so we pause
                this.state = SimulationState.PAUSE;
                workspaceController.changeIconToPlay();
                thread.interrupt();
                break;
            case PAUSE: // System is paused, so we resume
                this.state = SimulationState.PLAY;
                workspaceController.changeIconToPause();
                beginSimulation();
                break;
            case STOP: // System is not running, so we start playing
                clear();
                parse(code);
                this.state = SimulationState.PLAY;
                workspaceController.formatCodeArea(code);
                workspaceController.changeIconToPause();
                beginSimulation();
                break;
        }
    }

    public void end() {
        this.state = SimulationState.STOP;
        if ( thread != null ){
            thread.interrupt();
        }
        Platform.runLater(
                new Thread() {
                    public void run() {
                        workspaceController.changeIconToPlay();
                        workspaceController.disableStepMode(true);
                        workspaceController.enableCodeArea(true);
                    }
                }
        );
        clear();
    }

    public void reset(String codeText){
        this.state = SimulationState.STOP;
        if ( thread != null ){
            thread.interrupt();
        }
        clear();
        play(codeText);
    }

    public void previous() {
        if ( executionMap != null ) {
            if ( state == SimulationState.PAUSE ){
                System.out.println("previous");
                // 1. Revert Environment back by one pop
                this.environment = stack.pop();
                this.registerList = environment.getRegisters();
                this.memory = environment.getMemory();
                // 2. Retrieve EIP value and store it to @var currentLine
                String currentLine = registerList.get("EIP");
                // 3. Parse currentLine to int @var value

                int value = Integer.parseInt(currentLine, 16);
                // 4. Notify all observers that an instruction has been reverted
                value--;
                currentLine = Integer.toHexString(value);
                currentLine = MemoryAddressCalculator.extend(currentLine, RegisterList.instructionPointerSize, "0");
                System.out.println(currentLine);
                registerList.set("EIP", currentLine);
                notifyAllObservers(executionMap.get(currentLine), value);
            }
        }
    }

    public void next() {
        if ( executionMap != null ) {
            if ( state == SimulationState.PAUSE ){
                if ( executionMap.containsKey(registerList.get("EIP")) ) {
                    System.out.println("next");
                    // 1. Retrieve EIP value and store it to @var currentLine
                    String currentLine = registerList.get("EIP");
                    System.out.println(currentLine);
                    // 2. Parse currentLine to int @var value
                    int value = Integer.parseInt(currentLine, 16);
                    // 3. Retrieve and execute the CALVIS Instruction based on @var currentLine
                    executionMap.get(currentLine).execute(); // EXECUTE THE CALVIS INSTRUCTION
                    // 4. Notify all observers that an instruction has been executed
                    notifyAllObservers(executionMap.get(currentLine), value);
                    // 5. Increment @var currentLine and store it to EIP register
                    value++;
                    registerList.set("EIP", Integer.toHexString(value));
                    stack.push(environment);
                    System.out.println("pushed to stack");
                }
                else {
                    end();
                }
            }
        }
    }

    public void clear(){
        this.registerList.clear();
        this.memory.clear();
        this.executionMap = new HashMap<>();
        refreshAllObservers();
    }

    private void parse(String code){
        this.executionMap = parser.parse(code);
    }

    private void beginSimulation(){
        workspaceController.enableCodeArea(false);
        thread = new Thread(){
            public void run(){
                while ((state == SimulationState.PLAY) &&
                            executionMap.containsKey(registerList.get("EIP"))) {
                    // 1. Retrieve EIP value and store it to @var currentLine
                    String currentLine = registerList.get("EIP");
                    // 2. Parse currentLine to int @var value
                    int value = Integer.parseInt(currentLine, 16);
                    // 3. Retrieve and execute the CALVIS Instruction based on @var currentLine
                    executionMap.get(currentLine).execute(); // EXECUTE THE CALVIS INSTRUCTION
                    // 4. Notify all observers that an instruction has been executed
                    notifyAllObservers(executionMap.get(currentLine), value);
                    // 5. Increment @var currentLine and store it to EIP register
                    value++;
                    registerList.set("EIP", Integer.toHexString(value));
                    try {
                        Thread.sleep(SIMULATION_DELAY);
                    } catch (InterruptedException e) {
                        System.out.println("Simulation Thread interrupted");
                    }
                }
                if (state == SimulationState.PLAY) {
                    end();
                }
            }
        };
        thread.start();
    }

    /** Method used to get the keywords
     *  needed to be highlighted in the text editor
     */
    public String[] getKeywords(){
        List<String> keywordsList = new ArrayList<>();

        Iterator<String> registerIterator = registerList.getRegisterKeys();
        Iterator<String> instructionIterator = instructionList.getInstructionKeys();

        populateKeywords(keywordsList, registerIterator);
        populateKeywords(keywordsList, instructionIterator);

        String[] keywordsArray = new String[keywordsList.size()];
        keywordsArray = keywordsList.toArray(keywordsArray);
        return keywordsArray;
    }

    public String[] getInstructionKeywords(){
	    List<String> keywordsList = new ArrayList<>();
	    Iterator<String> instructionIterator = instructionList.getInstructionKeys();
	    populateKeywords(keywordsList, instructionIterator);

	    String[] instructionKeywords = new String[keywordsList.size()];
	    instructionKeywords = keywordsList.toArray(instructionKeywords);
	    return instructionKeywords;
    }

    /** Method used to populate @param keywordsList
     *  with both upper case and lower case keywords from @param iterator
     */
    private void populateKeywords(List keywordsList, Iterator<String> iterator){
        while (iterator.hasNext()){
            String key = iterator.next();
            keywordsList.add(key.toUpperCase());
            keywordsList.add(key.toLowerCase());
        }
    }

}
