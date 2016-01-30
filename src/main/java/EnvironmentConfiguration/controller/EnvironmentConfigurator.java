package EnvironmentConfiguration.controller;

import EnvironmentConfiguration.model.error_logging.ErrorLogger;
import EnvironmentConfiguration.model.error_logging.ErrorMessageList;
import EnvironmentConfiguration.model.engine.CALVISParser;
import EnvironmentConfiguration.model.engine.InstructionList;
import EnvironmentConfiguration.model.engine.Memory;
import EnvironmentConfiguration.model.engine.RegisterList;

import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class EnvironmentConfigurator {

    private CALVISParser p;
    private Memory memory;
    private RegisterList registers;
    private InstructionList instructions;
    private ErrorLogger errorLogger = new ErrorLogger(new ArrayList<ErrorMessageList>());

    public EnvironmentConfigurator(ArrayList<String> filePaths){
        // 1. Setup the environment
        this.memory = new Memory(32, 16, filePaths.get(0));
        this.registers = new RegisterList(filePaths.get(1));
        this.instructions = new InstructionList(filePaths.get(2));

        //1.5 check for errors
        errorLogger.combineErrorLogger(registers.getErrorLogger(), instructions.getErrorLogger());
        // 2. Create the CALVISParser based on the environment
        if(errorLogger.size() == 0) {
            this.p = new CALVISParser(instructions, registers, memory);
        }
    }

    public CALVISParser getParser(){
        return this.p;
    }

    public Memory getMemory() {
        return this.memory;
    }

    public RegisterList getRegisters() {
        return this.registers;
    }

    public InstructionList getInstructions(){
        return this.instructions;
    }

    public ErrorLogger getMessageLists(){
        return errorLogger;
    }

}
