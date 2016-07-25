package thsst.calvis.configuration.model.engine;

import thsst.calvis.configuration.model.errorlogging.ErrorLogger;

import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class ConfiguratorEnvironment {

    private Parser p;
    private Memory memory;
    private RegisterList registers;
    private InstructionList instructions;
    private ErrorLogger errorLogger = new ErrorLogger(new ArrayList<>());

    public ConfiguratorEnvironment(ArrayList<String> filePaths) {
        // 1. Setup the environment
        this.memory = new Memory(32, 16, filePaths.get(0));
        this.registers = new RegisterList(filePaths.get(1), 32);
        this.instructions = new InstructionList(filePaths.get(2));

        //1.5 check for errors
        errorLogger.combineErrorLogger(registers.getErrorLogger(), instructions.getErrorLogger());

        // 2. Create the Parser based on the environment
        if ( errorLogger.size() == 0 ) {
            this.p = new Parser(instructions, registers, memory);
            System.out.println("Memory available: "
                    + Runtime.getRuntime().freeMemory() + " / " + Runtime.getRuntime().totalMemory()
            );
        }
    }

    public Parser getParser() {
        return this.p;
    }

    public Memory getMemory() {
        return this.memory;
    }

    public RegisterList getRegisters() {
        return this.registers;
    }

    public InstructionList getInstructions() {
        return this.instructions;
    }

    public ErrorLogger getMessageLists() {
        return errorLogger;
    }

}
