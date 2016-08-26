execute(registers, memory) {
    int top = registers.x87().status().getTop();

    while ( top < 8 ) {
        top++;
        registers.x87().rotateBarrel(1); // rotate right to simulate pop
    }

    registers.x87().status().initializeValue();
    registers.x87().control().initializeValue();
    registers.x87().tag().initializeValue();
    registers.x87().refreshST();
}
