package EnvironmentConfiguration.controller;

import EnvironmentConfiguration.model.*;

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

    public EnvironmentConfigurator(ArrayList<String> filepaths){
        // 1. Setup the environment
        this.memory = new Memory(16, filepaths.get(0));
        this.registers = new RegisterList(filepaths.get(1));
        this.instructions = new InstructionList(filepaths.get(2));

        //1.5 check for errors
        errorLogger.combineErrorLogger(registers.getErrorLogger());

        // 2. Create the CALVISParser based on the environment
        this.p = new CALVISParser(instructions, registers, memory);


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
