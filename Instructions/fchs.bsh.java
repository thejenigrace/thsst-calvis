execute(registers, memory) {
    String st0 = registers.get("ST0");
    Double val = Double.parseDouble(st0) * - 1.0;

    registers.set("ST0", val + "");
}
