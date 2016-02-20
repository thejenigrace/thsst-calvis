execute(registers, memory){
    EFlags flags = registers.getEFlags();

    flags.setCarryFlag("0");
}
