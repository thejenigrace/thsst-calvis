execute(registers, memory) {
        //String st0 = registers.get("ST0");
	//String value = "3FF8000000000000";
	String value = registers.get("ST0");
	Calculator c = new Calculator(registers, memory);
	BigInteger biSrc = new BigInteger(value, 16);
	double regVal = c.convertHexToDoublePrecision(biSrc.toString(16));
	double resultVal = Math.cos(regVal);

	if(resultVal > Math.pow(2,64) || resultVal < Math.pow(2, 64) * -1){
		registers.x87().status().set("C2",'1');
	}
	else{
		registers.x87().status().set("C2",'0');
		String hexConvertedVal = c.convertDoublePrecisionToHexString(resultVal);
		registers.set("ST0", c.hexZeroExtend(hexConvertedVal,20));
	}
	registers.x87().status().set("C3",'0');
	registers.x87().status().set("C0",'0');
	
	
	
}
