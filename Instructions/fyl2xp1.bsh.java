execute(registers, memory) {
    String st0 = registers.get("ST0");
    String st1 = registers.get("ST1");
    // st1 = st1 * log base 2 (st0 + 1.0)
    registers.set("ST1", st1);
    // pop
    registers.x87().pop();
}
