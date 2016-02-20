execute(registers, memory) {
    EFlags flags = registers.getEFlags();

    flags.setDirectionFlag("1");
}
