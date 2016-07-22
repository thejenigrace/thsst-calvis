execute(registers, memory) {
    String st0 = registers.get("ST0");
	registers.set("ST0", Math.abs(Double.parseDouble(st0)) + "");
	registers.x87().status().set("C3",'0');
	registers.x87().status().set("C0",'0');
	registers.x87().status().set("C2",'0');
	registers.x87().status().set("C1",'0');
}
