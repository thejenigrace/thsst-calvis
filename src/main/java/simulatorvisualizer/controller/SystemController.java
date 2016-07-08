package simulatorvisualizer.controller;

import configuration.controller.ConfiguratorEnvironment;
import configuration.model.engine.*;
import configuration.model.exceptions.DataTypeMismatchException;
import configuration.model.exceptions.DuplicateLabelException;
import editor.controller.ConsoleController;
import editor.controller.VisualizationController;
import editor.controller.WorkspaceController;
import editor.model.AssemblyComponent;
import javafx.application.Platform;
import simulatorvisualizer.model.EnvironmentBag;
import simulatorvisualizer.model.KeywordBuilder;
import simulatorvisualizer.model.SimulationState;

import java.util.*;

/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class SystemController {

    static long SIMULATION_DELAY = 700;

    private ConfiguratorEnvironment environment;
    private RegisterList registerList;
    private InstructionList instructionList;
    private Memory memory;
    private Parser parser;

    private List<AssemblyComponent> observerList;
    private ConsoleController console;
    private VisualizationController visualizer;
    private SimulationState state;
    private SimulationState consoleState;
    private Thread thread;
    private WorkspaceController workspaceController;
    private String parsedCode;

    private HashMap<String, CalvisFormattedInstruction> executionMap;
    private HashMap<Integer, String[][]> registerStackMap;
    private HashMap<Integer, String> flagsStackMap;
    private HashMap<Integer, String> mxscrStackMap;
    private HashMap<Integer, String[][]> memoryStackMap;
    private Integer stackCount;

    private KeywordBuilder keywordBuilder;

    public SystemController(ConfiguratorEnvironment ec, WorkspaceController wc) {
        this.environment = ec;
        this.workspaceController = wc;
        this.parser = environment.getParser();
        this.registerList = environment.getRegisters();
        this.instructionList = environment.getInstructions();
        this.memory = environment.getMemory();
        this.observerList = new ArrayList<>();
        this.state = SimulationState.STOP;
        this.consoleState = SimulationState.STOP;

        this.registerStackMap = new HashMap<>();
        this.flagsStackMap = new HashMap<>();
        this.mxscrStackMap = new HashMap<>();
        this.memoryStackMap = new HashMap<>();
        this.stackCount = 0;

        this.keywordBuilder = new KeywordBuilder(registerList, memory, instructionList);
    }

    public void attach(AssemblyComponent observer) {
        observer.setSysCon(this);
        observerList.add(observer);
        if ( observer instanceof ConsoleController ) {
            this.console = (ConsoleController) observer;
        } else if ( observer instanceof VisualizationController ) {
            this.visualizer = (VisualizationController) observer;
        }
    }

    public void notifyAllObservers(CalvisFormattedInstruction currentInstruction, int lineNumber) {
        for ( AssemblyComponent a : observerList ) {
            // Execute console instructions here
            if ( a instanceof ConsoleController && currentInstruction != null) {
                String name = currentInstruction.getName().toUpperCase();
                String bigName = name.toUpperCase();
                if ( bigName.matches("PRINTF|SCANF|CLS") ) {
                    this.console.attachCalvisInstruction(currentInstruction);
                    currentInstruction.setConsole(this.console);
                    currentInstruction.getInstruction().consoleExecute(this.registerList, this.memory, this.console);
                }
            } else {
                a.update(currentInstruction, lineNumber);
            }
        }
    }

    public void refreshAllObservers() {
        observerList.forEach(AssemblyComponent::refresh);
    }

    public void pauseFromConsole() {
        this.consoleState = this.state;
        this.state = SimulationState.PAUSE;
        this.workspaceController.disablePlayNextPrevious(true);
        thread.interrupt();
    }

    public void resumeFromConsole() {
        this.state = this.consoleState;
        this.workspaceController.disablePlayNextPrevious(false);
        beginSimulation();
    }

    public void play(String code) {
        switch ( this.state ) {
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
                if ( isSuccessful ) {
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
        if ( thread != null ) {
            this.console.stopConsole();
            this.workspaceController.disablePlayNextPrevious(false);
            thread.interrupt();
        }
        Platform.runLater(
                new Thread() {
                    public void run() {
                        workspaceController.changeIconToPlay();
                        workspaceController.disableStepMode(true);
                        workspaceController.disableCodeArea(false);
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

    public void reportError(Exception e) {
        try {
            end();
            workspaceController.handleErrorLoggerTab(e);
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }
    }

    public void previous() {
        if ( this.executionMap != null ) {
            if ( this.state == SimulationState.PAUSE ) {
                this.stackCount--;
                if ( this.stackCount > 0 ) {
                    //1. Revert Environment back by 1 pop
                    String[][] registerStringArray = this.registerStackMap.get(this.stackCount);
                    for ( int i = 0; i < registerStringArray.length; i++ ) {
                        //1.1 Registers
                        try {
                            this.registerList.set(registerStringArray[i][0], registerStringArray[i][1]);
                        } catch ( DataTypeMismatchException e ) {
                            try {
                                this.workspaceController.handleErrorLoggerTab(e);
                            } catch ( Exception e1 ) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    // 1.2 Revert EFlags back by 1 pop
                    EFlags flags = this.registerList.getEFlags();
                    String flagsValue = this.flagsStackMap.get(this.stackCount);
                    flags.setValue(flagsValue);
                    flags.refreshFlags();
                    // 1.3 Revert Mxscr back by 1 pop
                    Mxscr mxscr = this.registerList.getMxscr();
                    String mxscrValue = this.mxscrStackMap.get(this.stackCount);
                    mxscr.setValue(mxscrValue);
                    mxscr.refreshFlags();
                    // 1.4 Revert Memory back by 1 pop
                    String[][] memoryArray = this.memoryStackMap.get(this.stackCount);
                    Map memoryMap = this.memory.getMemoryMap();
                    for ( int i = 0; i < memoryArray.length; i++ ) {
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
        if ( executionMap != null ) {
            if ( state == SimulationState.PAUSE ) {
                if ( executionMap.containsKey(registerList.getInstructionPointer()) ) {
                    try {
                        executeOneLine();
                    } catch ( DataTypeMismatchException e ) {
                        try {
                            workspaceController.handleErrorLoggerTab(e);
                        } catch ( Exception e1 ) {
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
        workspaceController.disableCodeArea(true);
        thread = new Thread() {
            public void run() {
                while ( (state == SimulationState.PLAY)
                        && executionMap.containsKey(registerList.getInstructionPointer()) ) {
                    try {
                        Thread.sleep(SIMULATION_DELAY);
                        executeOneLine();
                    } catch ( InterruptedException e ) {
//                        System.out.println("Simulation Thread interrupted");
                    } catch ( DataTypeMismatchException e ) {
                        try {
                            workspaceController.handleErrorLoggerTab(e);
                        } catch ( Exception e1 ) {
                            e1.printStackTrace();
                        }
                    }
                }
                if ( state == SimulationState.PLAY ) {
                    end();
                }
            }
        };
        thread.start();
    }

    private void executeOneLine() throws DataTypeMismatchException {
        // 1. Retrieve EIP value and store it to @var currentLine
        String currentLine = registerList.getInstructionPointer();
        // 2. Retrieve and execute the CALVIS Instruction based on @var currentLine
        boolean flag = true;
        try {
            // EXECUTE THE CALVIS INSTRUCTION
            flag = executionMap.get(currentLine).execute(console, visualizer);

            // 3. Parse currentLine to int @var value
            int value = Integer.parseInt(currentLine, 16);
            // 4. Notify all observers that an instruction has been executed
            notifyAllObservers(executionMap.get(currentLine), value);
            // 5. Increment @var currentLine and store it to EIP register
            if ( flag ) {
                value++;
                registerList.setInstructionPointer(Integer.toHexString(value));
            }
            this.stackCount++;
            push();

        } catch ( Exception e ) {
            e.printStackTrace();
            Platform.runLater(
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                workspaceController.handleErrorLoggerTab(e);
                                end();
                            } catch ( Exception someOtherException ) {
                                someOtherException.printStackTrace();
                            }
                        }
                    }
            );
        }
    }

    private void push() {
        Map registerMap = registerList.getRegisterMap();
        Iterator<String> iterator = registerMap.keySet().iterator();
        String[][] registerStringArray = new String[registerMap.size()][2];
        int i = 0;
        while ( iterator.hasNext() ) {
            String registerName = iterator.next();
            registerStringArray[i][0] = registerName;
            registerStringArray[i][1] = registerList.get(registerName);
            i++;
        }
        this.registerStackMap.put(stackCount, registerStringArray);

        EFlags flags = registerList.getEFlags();
        String flagsValue = flags.getValue();
        this.flagsStackMap.put(stackCount, flagsValue);

        Mxscr mxscr = registerList.getMxscr();
        String mxscrValue = mxscr.getValue();
        this.mxscrStackMap.put(stackCount, mxscrValue);

        Map<String, String> memoryMap = memory.getMemoryMap();
        Iterator<String> iterator2 = memoryMap.keySet().iterator();
        String[][] memoryArray = new String[memoryMap.size()][2];
        int k = 0;
        while ( iterator2.hasNext() ) {
            String memoryAddress = iterator2.next();
            memoryArray[k][0] = memoryAddress;
            memoryArray[k][1] = memoryMap.get(memoryAddress);
            k++;
        }
        this.memoryStackMap.put(stackCount, memoryArray);

        // FOR PREVIOUS VALUES OF STACK
//        setOldEnvironment(registerStringArray, flagsValue, mxscrValue, memoryArray);
    }

    private boolean parse(String code) {
        boolean isSuccessful = true;
        String[] codeLines = code.split("\n");
        try {
            this.executionMap = parser.parse(code);
            for ( String lineNumber : this.executionMap.keySet() ) {
                CalvisFormattedInstruction calvisInstruction = this.executionMap.get(lineNumber);
                /**
                 * Compute for the equivalent linenumber in textEditor of the calvis instructions
                 */
                int lineCounter = 0;
                int mappedLine = Integer.parseInt(lineNumber, 16);
                for ( int i = 0; i < codeLines.length; i++ ) {
                    if ( !codeLines[i].matches("[;].*") && !codeLines[i].trim().equals("") ) {
                        if ( mappedLine == lineCounter ) {
                            lineCounter = i;
                            break;
                        } else {
                            lineCounter++;
                        }
                    }
                }
                /**
                 *  Verify Parameters of each instruction
                 */
                calvisInstruction.verifyParameters(lineCounter);
            }
        } catch ( Exception e ) {
            if ( e instanceof DuplicateLabelException ) {
                DuplicateLabelException duplicateLabelException = (DuplicateLabelException) e;
                int labelOccurrence = 0;
                for ( int i = 0; i < codeLines.length; i++ ) {
                    if ( codeLines[i].contains(duplicateLabelException.getLabel()) ) {
                        if ( !codeLines[i].matches("[;].*" + duplicateLabelException.getLabel()) ) {
                            labelOccurrence++;
                        }
                    }
                    if ( labelOccurrence == 2 ) {
                        duplicateLabelException.setLineNumber(i + 1);
                        break;
                    }
                }
            }
            isSuccessful = false;
//            e.printStackTrace();
            Platform.runLater(
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                workspaceController.handleErrorLoggerTab(e);
                            } catch ( Exception viewException ) {
                                viewException.printStackTrace();
                            }
                        }
                    }
            );
        }
        this.parsedCode = code;
        return isSuccessful;
    }

    public String getParsedCode() {
        return this.parsedCode;
    }

    public KeywordBuilder getKeywordBuilder() {
        return this.keywordBuilder;
    }

    public RegisterList getRegisterState() {
        return this.registerList;
    }

    public Memory getMemoryState() {
        return this.memory;
    }

    private void setOldEnvironment(String[][] registerStringArray, String flagsValue,
                                   String mxscrValue, String[][] memoryArray) {
        EnvironmentBag bag = new EnvironmentBag(registerStringArray, flagsValue, mxscrValue, memoryArray);
        visualizer.setOldEnvironment(bag);
    }

}
