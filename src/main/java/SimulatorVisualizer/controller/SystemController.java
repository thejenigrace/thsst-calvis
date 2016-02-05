package SimulatorVisualizer.controller;

import EnvironmentConfiguration.controller.EnvironmentConfigurator;
import EnvironmentConfiguration.model.engine.CALVISInstruction;
import EnvironmentConfiguration.model.engine.CALVISParser;
import EnvironmentConfiguration.model.engine.EFlags;
import EnvironmentConfiguration.model.engine.InstructionList;
import EnvironmentConfiguration.model.engine.Memory;
import EnvironmentConfiguration.model.engine.MemoryAddressCalculator;
import EnvironmentConfiguration.model.engine.RegisterList;
import MainEditor.controller.WorkspaceController;
import MainEditor.model.AssemblyComponent;
import SimulatorVisualizer.model.SimulationState;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
	private Thread thread;

    private WorkspaceController workspaceController;

	private HashMap<String, CALVISInstruction> executionMap;
	private HashMap<Integer, String[][]> registerStackMap;
	private HashMap<Integer, String> flagsStackMap;
	private HashMap<Integer, String[][]> memoryStackMap;
	private Integer count;

    public SystemController(EnvironmentConfigurator ec, WorkspaceController wc){
        this.environment = ec;
        this.workspaceController = wc;
        this.parser = environment.getParser();
        this.registerList = environment.getRegisters();
        this.instructionList = environment.getInstructions();
        this.memory = environment.getMemory();
        this.observerList = new ArrayList<>();
        this.state = SimulationState.STOP;

	    this.registerStackMap = new HashMap<>();
	    this.flagsStackMap = new HashMap<>();
	    this.memoryStackMap = new HashMap<>();
	    this.count = 0;
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
//        clear();
    }

    public void reset(){
        this.state = SimulationState.STOP;
        if ( thread != null ){
            thread.interrupt();
        }
        clear();
    }

	public void clear(){
		this.registerList.clear();
		this.memory.clear();
		this.executionMap = new HashMap<>();
		this.registerStackMap = new HashMap<>();
		this.flagsStackMap = new HashMap<>();
		this.memoryStackMap = new HashMap<>();
		this.count = 0;
		refreshAllObservers();
	}

    public void previous() {
        if ( executionMap != null ) {
            if ( state == SimulationState.PAUSE ){
	            //System.out.println("previous");
	            count--;
	            if ( count >= 0 ) {
		            //1. Revert Environment back by one pop
		            String[][] registerStringArray = registerStackMap.get(count);
		            for (int i = 0; i < registerStringArray.length; i++) {
			            //System.out.println(registerStringArray[i][0] + " : " + registerStringArray[i][1]);
			            //1.2 Set registerList with values from pop
			            this.registerList.set(registerStringArray[i][0], registerStringArray[i][1]);
		            }
		            EFlags flags = this.registerList.getEFlags();
		            String flagsValue = flagsStackMap.get(count);
		            flags.setValue(flagsValue);

		            String[][] memoryArray = memoryStackMap.get(count);
		            Map memoryMap = this.memory.getMemoryMap();
		            for (int i = 0; i < memoryArray.length; i++) {
			            memoryMap.put(memoryArray[i][0], memoryArray[i][1]);
		            }

		            // 2. Retrieve EIP value and store it to @var currentLine
                    String currentLine = registerList.get("EIP");
		            // 3. Parse currentLine to int @var value
                    int value = Integer.parseInt(currentLine, 16);
		            value--;
		            currentLine = Integer.toHexString(value);
		            currentLine = MemoryAddressCalculator.extend(currentLine, RegisterList.instructionPointerSize, "0");
		            //2. Notify all observers that an instruction has been reverted
		            notifyAllObservers(executionMap.get(currentLine), value);
	            }
	            else {
		            end();
	            }
            }
        }
    }

    public void next() {
        if ( executionMap != null ) {
            if ( state == SimulationState.PAUSE ){
                if ( executionMap.containsKey(registerList.get("EIP")) ) {
                    executeOneLine();
                }
                else {
                    end();
                }
            }
        }
    }

	private void beginSimulation(){
		workspaceController.enableCodeArea(false);
		thread = new Thread(){
			public void run(){
				while ((state == SimulationState.PLAY) && executionMap.containsKey(registerList.get("EIP"))) {
					executeOneLine();
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

	private void executeOneLine(){
		// 1. Retrieve EIP value and store it to @var currentLine
		String currentLine = registerList.get("EIP");
		//System.out.println(currentLine);
		// 2. Parse currentLine to int @var value
		int value = Integer.parseInt(currentLine, 16);
		// 3. Retrieve and execute the CALVIS Instruction based on @var currentLine
		try {
			executionMap.get(currentLine).execute(); // EXECUTE THE CALVIS INSTRUCTION
		} catch (Exception e){
			System.out.println("INSTRUCTION EXECUTION ERROR" + e.getMessage());
			e.printStackTrace();

			Platform.runLater(
					new Thread() {
						@Override
						public void run() {
//							try {
//								workspaceController.handleErrorWindow();
//							} catch (Exception ioexception) {
//								ioexception.printStackTrace();
//							}
						}
					}
			);
		}
		// 4. Notify all observers that an instruction has been executed
		notifyAllObservers(executionMap.get(currentLine), value);
		// 5. Increment @var currentLine and store it to EIP register
		value++;
		registerList.set("EIP", Integer.toHexString(value));
		count++;
		push();
	}

    private void parse(String code){
	    try {
		    this.executionMap = parser.parse(code);
	    } catch (Exception e){
		    e.printStackTrace();
		    Platform.runLater(
				    new Thread() {
					    @Override
					    public void run() {
						    try {
							    workspaceController.handleErrorWindow(e);
						    } catch (Exception viewException) {
							    viewException.printStackTrace();
						    }
					    }
				    }
		    );
	    }
	    push();
    }

	private void push(){
		Map registerMap = registerList.getRegisterMap();
		Iterator<String> iterator = registerMap.keySet().iterator();
		String[][] registerStringArray = new String[registerMap.size()][2];
		int i = 0;
		while (iterator.hasNext()){
			String registerName = iterator.next();
			registerStringArray[i][0] = registerName;
			registerStringArray[i][1] = registerList.get(registerName);
			i++;
		}
		this.registerStackMap.put(count, registerStringArray);

		EFlags flags = registerList.getEFlags();
		String flagsValue = flags.getValue();
		this.flagsStackMap.put(count, flagsValue);

		Map<String, String> memoryMap = memory.getMemoryMap();
		Iterator<String> iterator2 = memoryMap.keySet().iterator();
		String[][] memoryArray = new String[memoryMap.size()][2];
		int k = 0;
		while (iterator2.hasNext()){
			String memoryAddress = iterator2.next();
			memoryArray[k][0] = memoryAddress;
			memoryArray[k][1] = memoryMap.get(memoryAddress);
			k++;
		}
		this.memoryStackMap.put(count, memoryArray);

		//System.out.println(registerStringArray);
//		for (int k = 0; k < registerStringArray.length; k++) {
//			System.out.println(registerStringArray[k][0] + " : " + registerStringArray[k][1]);
//		}
	}

    /** Method used to get the keywords
     *  needed to be highlighted in the text editor
     */
//    public String[] getKeywords(){
//        List<String> keywordsList = new ArrayList<>();
//
//        Iterator<String> registerIterator = registerList.getRegisterKeys();
//        Iterator<String> instructionIterator = instructionList.getInstructionKeys();
//
//        populateKeywords(keywordsList, registerIterator);
//        populateKeywords(keywordsList, instructionIterator);
//
//        String[] keywordsArray = new String[keywordsList.size()];
//        keywordsArray = keywordsList.toArray(keywordsArray);
//        return keywordsArray;
//    }

    public String[] getRegisterKeywords(){
        List<String> keywordsList = new ArrayList<>();

        Iterator<String> registerIterator = registerList.getRegisterKeys();

        populateKeywords(keywordsList, registerIterator);

        String[] keywordsArray = new String[keywordsList.size()];
        keywordsArray = keywordsList.toArray(keywordsArray);
        return keywordsArray;
    }

    public String[] getMemoryKeywords(){
        List<String> keywordsList = new ArrayList<>();

        Iterator<String> registerIterator = memory.getMemoryKeys();

        populateKeywords(keywordsList, registerIterator);

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
