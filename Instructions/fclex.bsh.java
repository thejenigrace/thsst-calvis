execute(registers, memory) {
    registers.x87().status().clearExceptions();
}
