execute(registers, memory) {
    String st0 = registers.get("ST0");
    // round st0 to nearest integral value
    registers.set("ST0", st0);
}
