execute(registers, memory) {
	String st0 = registers.get("ST0");
		// st0 = sin(st0)
		// push cos(st0)
//	registers.set("ST0", "00003FE0000000000000");
	String value = registers.get("ST0");
	Calculator c = new Calculator(registers, memory);

	double regVal = Double.parseDouble(value);
	double resultSinVal = Math.tan(regVal);

	if(regVal > Math.pow(2,64) || regVal < Math.pow(2, 64) * -1){
		registers.x87().status().set("C2",'1');
	}
	else{
		registers.x87().status().set("C2",'0');
//		String hexConvertedCosVal = c.convertDoublePrecisionToHexString(resultCosVal);
//		String hexConvertedSinVal = c.convertDoublePrecisionToHexString(resultSinVal);

		registers.set("ST0", resultSinVal + "");
		registers.x87().push("1.0" + "");

	}
		registers.x87().status().set("C3",'0');
		registers.x87().status().set("C0",'0');

}
