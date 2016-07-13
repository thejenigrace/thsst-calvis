execute(registers, memory) {
    String st0 = registers.get("ST0");
    // compute square root of st0
    registers.set("ST0", "" + Math.sqrt(Double.parseDouble(st0)));
}
