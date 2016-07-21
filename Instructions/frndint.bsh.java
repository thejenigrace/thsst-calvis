execute(registers, memory) {
    double st0 = Math.round(Double.parseDouble(registers.get("ST0")));
    // round st0 to nearest integral value
    registers.set("ST0", st0 + "");
}
