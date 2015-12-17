package EnvironmentConfiguration.controller;

import EnvironmentConfiguration.model.CALVISParser;
import EnvironmentConfiguration.model.InstructionList;
import EnvironmentConfiguration.model.Memory;
import EnvironmentConfiguration.model.RegisterList;

/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class EnvironmentConfigurator {

    private CALVISParser p;
    private Memory memory;
    private RegisterList registers;

    public EnvironmentConfigurator(){

        // 1. Setup the environment
        this.memory = new Memory(16, "Memory/config.csv");
        this.registers = new RegisterList("Registers/register_list.csv");
        InstructionList instructions = new InstructionList("Instructions/instruction_list.csv");

        // 2. Create the EnvironmentConfiguration.model.CALVISParser with Dynamic Grammar from
        // the list of instructions and list of registers
        // include the memory to bind instruction actions

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

}
