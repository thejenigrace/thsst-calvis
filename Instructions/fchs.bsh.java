execute(registers, memory) {
    String st0 = registers.get("ST0");
    Double val = Double.parseDouble(st0) * - 1.0;
	Calculator c = new Calculator(registers, memory);
	boolean isException = c.generateFPUExceptions(registers, val);
    registers.set("ST0", val + "");
	registers.x87().status().set("C3",'0');
	registers.x87().status().set("C2",'0');
	registers.x87().status().set("C1",'0');
	registers.x87().status().set("C0",'0');
}
