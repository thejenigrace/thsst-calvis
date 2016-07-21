execute(registers, memory) {
	double st0 = Double.parseDouble(registers.get("ST0"));
	double st1 = Double.parseDouble(registers.get("ST1"));
	registers.set("ST0", (st0 % st1) + "");
}
