execute(registers, memory) {
    String st0 = registers.get("ST0");
    String st1 = registers.get("ST1");

    // divide st0 / st1, IEEE remainder goes to st0;
    registers.set("ST0", st0);
}
