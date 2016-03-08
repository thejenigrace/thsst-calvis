package simulator_visualizer.controller;

import configuration.controller.ConfiguratorEnvironment;
import configuration.model.engine.*;
import editor.controller.ConsoleController;
import editor.controller.WorkspaceController;
import editor.model.AssemblyComponent;
import javafx.application.Platform;
import simulator_visualizer.model.SimulationState;

import java.util.*;

/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class SystemController {

    static long SIMULATION_DELAY = 1500;

    private ConfiguratorEnvironment environment;
    private RegisterList registerList;
    private InstructionList instructionList;
    private Memory memory;
    private CalvisParser parser;

    private List<AssemblyComponent> observerList;
    private ConsoleController console;
    private SimulationState state;
    private Thread thread;
    private WorkspaceController workspaceController;
    private String parsedCode;

    private HashMap<String, CalvisInstruction> executionMap;
    private HashMap<Integer, String[][]> registerStackMap;
    private HashMap<Integer, String> flagsStackMap;
    private HashMap<Integer, String[][]> memoryStackMap;
    private Integer stackCount;

    public SystemController(ConfiguratorEnvironment ec, WorkspaceController wc) {
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
        this.stackCount = 0;
    }

    public void attach(AssemblyComponent observer) {
        observer.setSysCon(this);
        observerList.add(observer);
        if ( observer instanceof ConsoleController ) {
            this.console = (ConsoleController) observer;
        }
    }

    public void notifyAllObservers(CalvisInstruction currentLine, int lineNumber) {
        if (currentLine != null) {
            for (AssemblyComponent a : observerList) {
                a.update(currentLine.toString(), lineNumber);
            }
        } else {
            for (AssemblyComponent a : observerList) {
                a.update(null, lineNumber);
            }
        }
    }

    public void refreshAllObservers() {
        observerList.forEach(AssemblyComponent::refresh);
    }

    public RegisterList getRegisterState() {
        return this.registerList;
    }

    public Memory getMemoryState() {
        return this.memory;
    }

    public void pauseFromConsole() {
        this.state = SimulationState.PAUSE;
        thread.interrupt();
    }

    public void resumeFromConsole() {
        this.state = SimulationState.PLAY;
        beginSimulation();
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
                workspaceController.formatCodeArea(code);
                workspaceController.changeIconToPause();
                boolean isSuccessful = parse(code);
                if (isSuccessful) {
                    this.state = SimulationState.PLAY;
                    beginSimulation();
                } else {
                    end();
                }
                break;
        }
    }

    public void end() {
        this.state = SimulationState.STOP;
        if (thread != null) {
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
    }

    public void reset() {
        end();
        clear();
    }

    public void clear() {
        this.registerList.clear();
        this.memory.clear();
        this.executionMap = new HashMap<>();
        this.registerStackMap = new HashMap<>();
        this.flagsStackMap = new HashMap<>();
        this.memoryStackMap = new HashMap<>();
        this.stackCount = 0;
        this.parsedCode = "";
        refreshAllObservers();
    }

    public void previous() {
        if (executionMap != null) {
            if (state == SimulationState.PAUSE) {
                //System.out.println("previous");
                stackCount--;
                if (stackCount > 0) {
                    //1. Revert Environment back by one pop
                    String[][] registerStringArray = registerStackMap.get(stackCount);
                    for (int i = 0; i < registerStringArray.length; i++) {
//			            System.out.println(registerStringArray[i][0] + " : " + registerStringArray[i][1]);
                        //1.2 Set registerList with values from pop
                        try {
                            this.registerList.set(registerStringArray[i][0], registerStringArray[i][1]);
                        } catch (DataTypeMismatchException e) {
                            try {
                                workspaceController.handleErrorLoggerTab(e);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    EFlags flags = this.registerList.getEFlags();
                    String flagsValue = flagsStackMap.get(stackCount);
                    flags.setValue(flagsValue);

                    String[][] memoryArray = memoryStackMap.get(stackCount);
                    Map memoryMap = this.memory.getMemoryMap();
                    for (int i = 0; i < memoryArray.length; i++) {
                        memoryMap.put(memoryArray[i][0], memoryArray[i][1]);
                    }

                    // 2. Retrieve EIP value and store it to @var currentLine
                    String currentLine = registerList.getInstructionPointer();
                    // 3. Parse currentLine to int @var value
                    int value = Integer.parseInt(currentLine, 16);
                    value--;
                    currentLine = Integer.toHexString(value);
                    currentLine = MemoryAddressCalculator.extend(currentLine, RegisterList.instructionPointerSize, "0");
                    //2. Notify all observers that an instruction has been reverted
                    notifyAllObservers(executionMap.get(currentLine), value);
                } else {
                    end();
                    clear();
                }
            }
        }
    }

    public void next() {
        if (executionMap != null) {
            if (state == SimulationState.PAUSE) {
                if (executionMap.containsKey(registerList.getInstructionPointer())) {
                    try {
                        executeOneLine();
                    } catch (DataTypeMismatchException e) {
                        try {
                            workspaceController.handleErrorLoggerTab(e);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                } else {
                    end();
                }
            }
        }
    }

    private void beginSimulation() {
        workspaceController.enableCodeArea(false);
        thread = new Thread() {
            public void run() {
                while ((state == SimulationState.PLAY)
                        && executionMap.containsKey(registerList.getInstructionPointer())) {
                    try {
                        Thread.sleep(SIMULATION_DELAY);
                        executeOneLine();
                    } catch (InterruptedException e) {
                        System.out.println("Simulation Thread interrupted");
                    } catch (DataTypeMismatchException e) {
                        try {
                            workspaceController.handleErrorLoggerTab(e);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (state == SimulationState.PLAY) {
                    end();
                }
            }
        };
        thread.start();
    }

    private void executeOneLine() throws DataTypeMismatchException {
        // 1. Retrieve EIP value and store it to @var currentLine
        String currentLine = registerList.getInstructionPointer();
        //System.out.println(currentLine);

        // 2. Retrieve and execute the CALVIS Instruction based on @var currentLine
        boolean flag = true;
        try {
            flag = executionMap.get(currentLine).execute(console); // EXECUTE THE CALVIS INSTRUCTION
        } catch (Exception e) {
            System.out.println("INSTRUCTION EXECUTION ERROR MESSAGE: " + e.getMessage());
            System.out.println("INSTRUCTION EXECUTION ERROR CAUSE: " + e.getCause());
			e.printStackTrace();

            Platform.runLater(
                    new Thread() {
                @Override
                public void run() {
                    try {
                        workspaceController.handleErrorLoggerTab(e);
                        end();
                    } catch (Exception someOtherException) {
                        someOtherException.printStackTrace();
                    }
                }
            }
            );
        }
        // 3. Parse currentLine to int @var value
        int value = Integer.parseInt(currentLine, 16);
        // 4. Notify all observers that an instruction has been executed
        notifyAllObservers(executionMap.get(currentLine), value);
        // 5. Increment @var currentLine and store it to EIP register
        if (flag) {
            value++;
            registerList.setInstructionPointer(Integer.toHexString(value));
        }
        stackCount++;
        push();
    }

    private boolean parse(String code) {
        boolean isSuccessful = true;
        String[] codeLines = code.split("\n");
        try {
            this.executionMap = parser.parse(code);
            for (String lineNumber : this.executionMap.keySet()) {
                CalvisInstruction calvisInstruction = this.executionMap.get(lineNumber);
                /**
                 * Compute for the equivalent linenumber in textEditor of the
                 * calvis instructions
                 */
                int lineCounter = 0;
                int mappedLine = Integer.parseInt(lineNumber, 16);
                for (int i = 0; i < codeLines.length; i++) {
                    if (!codeLines[i].matches("[;].*") && !codeLines[i].trim().equals("")) {
                        if (mappedLine == lineCounter) {
                            lineCounter = i;
                            break;
                        } else {
                            lineCounter++;
                        }
                    }
                }
                calvisInstruction.verifyParameters(lineCounter);
            }
        } catch (Exception e) {
            if (e instanceof DuplicateLabelException) {
                DuplicateLabelException duplicateLabelException = (DuplicateLabelException) e;
                int labelOccurrence = 0;
                for (int i = 0; i < codeLines.length; i++) {
                    if (codeLines[i].contains(duplicateLabelException.getLabel())) {
                        if (!codeLines[i].matches("[;].*" + duplicateLabelException.getLabel())) {
                            labelOccurrence++;
                        }
                    }
                    if (labelOccurrence == 2) {
                        duplicateLabelException.setLineNumber(i + 1);
                        break;
                    }
                }
            }
            isSuccessful = false;
            e.printStackTrace();
            Platform.runLater(
                    new Thread() {
                @Override
                public void run() {
                    try {
                        workspaceController.handleErrorLoggerTab(e);
                    } catch (Exception viewException) {
                        viewException.printStackTrace();
                    }
                }
            }
            );
        }
        this.parsedCode = code;
        return isSuccessful;
    }

    private void push() {
        Map registerMap = registerList.getRegisterMap();
        Iterator<String> iterator = registerMap.keySet().iterator();
        String[][] registerStringArray = new String[registerMap.size()][2];
        int i = 0;
        while (iterator.hasNext()) {
            String registerName = iterator.next();
            registerStringArray[i][0] = registerName;
            registerStringArray[i][1] = registerList.get(registerName);
            i++;
        }
        this.registerStackMap.put(stackCount, registerStringArray);

        EFlags flags = registerList.getEFlags();
        String flagsValue = flags.getValue();
        this.flagsStackMap.put(stackCount, flagsValue);

        Map<String, String> memoryMap = memory.getMemoryMap();
        Iterator<String> iterator2 = memoryMap.keySet().iterator();
        String[][] memoryArray = new String[memoryMap.size()][2];
        int k = 0;
        while (iterator2.hasNext()) {
            String memoryAddress = iterator2.next();
            memoryArray[k][0] = memoryAddress;
            memoryArray[k][1] = memoryMap.get(memoryAddress);
            k++;
        }
        this.memoryStackMap.put(stackCount, memoryArray);

        //System.out.println(registerStringArray);
//		for (int n = 0; n < registerStringArray.length; n++) {
//			System.out.println(registerStringArray[n][0] + " : " + registerStringArray[n][1]);
//		}
    }

    public String getParsedCode() {
        return this.parsedCode;
    }

    public String[] getRegisterKeywords() {
        List<String> keywordsList = new ArrayList<>();

        Iterator<String> registerIterator = registerList.getRegisterKeys();

        populateKeywords(keywordsList, registerIterator);

        String[] keywordsArray = new String[keywordsList.size()];
        keywordsArray = keywordsList.toArray(keywordsArray);
        return keywordsArray;
    }

    public String[] getMemoryKeywords() {
        List<String> keywordsList = new ArrayList<>();

        Iterator<String> registerIterator = memory.getMemoryKeys();

        populateKeywords(keywordsList, registerIterator);

        String[] keywordsArray = new String[keywordsList.size()];
        keywordsArray = keywordsList.toArray(keywordsArray);
        return keywordsArray;
    }

    public String[] getInstructionKeywords() {
        List<String> keywordsList = new ArrayList<>();
        Iterator<String> instructionIterator = instructionList.getInstructionKeys();
        populateKeywords(keywordsList, instructionIterator);

        String[] instructionKeywords = new String[keywordsList.size()];
        instructionKeywords = keywordsList.toArray(instructionKeywords);
        return instructionKeywords;
    }

    /**
     * Method used to populate @param keywordsList with both upper case and
     * lower case keywords from @param iterator
     */
    private void populateKeywords(List keywordsList, Iterator<String> iterator) {
        while (iterator.hasNext()) {
            String key = iterator.next();
            keywordsList.add(key.toUpperCase());
            keywordsList.add(key.toLowerCase());
        }
    }

}
