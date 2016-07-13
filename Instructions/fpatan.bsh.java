execute(registers, memory) {
    /*String st0 = registers.get("ST0");
    // split significand and exponent
    // st0 = gets exponent
    // significand is pushed
    registers.set("ST0", st0);*/
	
	String valueZero = registers.get("ST0");
	String valueOne = registers.get("ST1");
	Calculator c = new Calculator(registers, memory);
	BigInteger biSrcZero = new BigInteger(valueZero, 16);
	BigInteger biSrcOne = new BigInteger(valueOne, 16);
	double regValZero = c.convertHexToDoublePrecision(biSrcZero.toString(16));
	double regValOne = c.convertHexToDoublePrecision(biSrcOne.toString(16));
	double resultVal = Math.atan(regValOne/regValZero);

	registers.x87().status().set("C2",'0');
	String hexConvertedVal = c.convertDoublePrecisionToHexString(resultVal);
	registers.set("ST1", resultVal + "");
	registers.x87().status().set("C3",'0');
	registers.x87().status().set("C0",'0');
	registers.x87().status().set("C2",'0');
	registers.x87().pop();
}
