execute(registers, memory) {
    String st0 = registers.get("ST0");
    // st1 = arc tan (st1 / st0)
    registers.set("ST1", st0);
    // pop
    registers.x87().pop();
}
