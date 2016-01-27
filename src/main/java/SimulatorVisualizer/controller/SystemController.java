package SimulatorVisualizer.controller;

import EnvironmentConfiguration.controller.EnvironmentConfigurator;
import EnvironmentConfiguration.model.CALVISInstruction;
import EnvironmentConfiguration.model.CALVISParser;
import EnvironmentConfiguration.model.InstructionList;
import EnvironmentConfiguration.model.Memory;
import EnvironmentConfiguration.model.RegisterList;
import MainEditor.model.AssemblyComponent;
import SimulatorVisualizer.model.SimulationState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

    public SystemController(EnvironmentConfigurator ec){
        this.environment = ec;
        this.parser = environment.getParser();
        this.registerList = environment.getRegisters();
        this.instructionList = environment.getInstructions();
        this.memory = environment.getMemory();
        this.observerList = new ArrayList<>();
        this.state = SimulationState.STOP;
    }

    public void attach(AssemblyComponent observer){
        observer.setSysCon(this);
        observerList.add(observer);
    }

    public void notifyAllObservers(){
        observerList.forEach(AssemblyComponent::update);
    }

    public RegisterList getRegisterState(){
        return this.registerList;
    }

    public Memory getMemoryState(){
        return this.memory;
    }

    public void play(String code) {
        switch (this.state) {
            case PLAY: // System is currently playing, so don't play anymore
                break;
            case PAUSE:
                beginSimulation();
                break;
            case STOP: // System is not running, so we play
               // reset();
                parse(code);
               // this.state = SimulationState.PLAY;
               // beginSimulation();
                break;
        }
    }

    public void pause() {
        if ( this.state == SimulationState.PLAY ) {
            this.state = SimulationState.PAUSE;
        }
    }

    public void stop() {
        this.state = SimulationState.STOP;
    }

    public void reset(){
        stop();
        this.registerList.clear();
        this.memory.clear();
        notifyAllObservers();
    }

    private void parse(String code){
        executionMap = parser.parse(code);
    }

    private void beginSimulation(){
        new Thread() {
            public void run(){
                while ((state == SimulationState.PLAY) &&
                        executionMap.containsKey(environment.getRegisters().get("EIP"))){
                    try {
                        Thread.sleep(SIMULATION_DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 1. Retrieve EIP value and store it to @var currentLine
                    String currentLine = environment.getRegisters().get("EIP");
                    // 2. Retrieve and execute the CALVIS Instruction based on @var currentLine
                    executionMap.get(currentLine).execute(); // EXECUTE THE CALVIS INSTRUCTION
                    // 3. Increment @var currentLine and store it to EIP register
                    int value = Integer.parseInt(currentLine, 16);
                    value++;
                    environment.getRegisters().set("EIP", Integer.toHexString(value));
                    // 4. Notify all observers that an instruction has been executed
                    notifyAllObservers();
                }
                state = SimulationState.STOP;
            }
        }.start();

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
