execute(registers, memory) {
   /* String st0 = registers.get("ST0");
    // st0 = tan (st0)
    registers.set("ST0", st0);
    // push 1
    String value = "3fff8000000000000000";
    registers.x87().push(value);*/
	registers.set("ST0", "00003FE0000000000000");
    String value = registers.get("ST0");
	Calculator c = new Calculator(registers, memory);
	BigInteger biSrc = new BigInteger(value, 16);
	double regVal = c.convertHexToDoublePrecision(biSrc.toString(16));
	double resultTanVal = Math.tan(regVal);
	double resultDefault = 1.0;
	
	if(regVal > Math.pow(2,64) || regVal < Math.pow(2, 64) * -1){
		registers.x87().status().set("C2",'1');
	}
	else{
		registers.x87().status().set("C2",'0');
		String hexConvertedTanVal = c.convertDoublePrecisionToHexString(resultTanVal);
		String hexConvertedDefaultVal = c.convertDoublePrecisionToHexString(resultDefault);
		registers.x87().push(c.hexZeroExtend(hexConvertedTanVal,20));
		registers.x87().push(c.hexZeroExtend(hexConvertedDefaultVal, 20));
	}
	registers.x87().status().set("C3",'0');
	registers.x87().status().set("C0",'0');
}
