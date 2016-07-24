execute(registers, memory) {
    String st0 = registers.get("ST0");
	double result = Math.sqrt(Double.parseDouble(st0));
    // compute square root of st0
	if(result >= 0){
		registers.set("ST0","" + result);
	}
	else{
		c.setInvalidOperation(registers, "ST0");
	}

	registers.x87().status().set("C3",'0');
	registers.x87().status().set("C2",'0');
	registers.x87().status().set("C0",'0');
}
