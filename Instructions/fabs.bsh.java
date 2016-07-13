execute(registers, memory) {
    String st0 = registers.get("ST0");

    registers.set("ST0", Math.abs(Double.parseDouble(st0)) + "");
}
