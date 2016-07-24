execute(registers, memory) {
    registers.x87().status.initializeValue();
    registers.x87().control.initializeValue();
    registers.x87().tag.initializeValue();
}
