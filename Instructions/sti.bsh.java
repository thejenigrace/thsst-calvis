execute(registers, memory) {
    EFlags flags = registers.getEFlags();

    flags.setInterruptFlag("1");
}
