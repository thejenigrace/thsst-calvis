execute(registers, memory) {
    String st0 = registers.get("ST0");
    // st0 = sin(st0)
    // push cos(st0)
    registers.set("ST0", st0);
}
