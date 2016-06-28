/*execute(registers, memory) {
    String st0 = registers.get("ST0");
    String st1 = registers.get("ST1");
    // st1 = st1 * log base 2 (st0)
    registers.set("ST1", st1);
    // pop
    registers.x87().pop();
}
*/
execute(registers, memory) {
    /*String st0 = registers.get("ST0");
    // st0 = 2 ^ (st0) - 1
    registers.set("ST0", st0);*/
	String valueZero = registers.get("ST0");
	String valueOne = registers.get("ST1");
	Calculator c = new Calculator(registers, memory);
	BigInteger biSrcZero = new BigInteger(valueZero, 16);
	BigInteger biSrcOne = new BigInteger(valueOne, 16);
	double regValZero = c.convertHexToDoublePrecision(biSrcZero.toString(16));
	double regValOne = c.convertHexToDoublePrecision(biSrcOne.toString(16));
	double resultVal = 0.0;
	if(regValZero == 0.0 && registers.mxscr.getDivideByZeroMask() == "1"){
		if(biSrcOne >= 0){
			resultVal = -0.01;
		}
		else{
			resultVal = 0.01;
		}
	}else{
		resultVal = regValOne * (Math.log(regValZero) / Math.log(2) );
	}
	
	
	String hexConvertedVal = c.convertDoublePrecisionToHexString(resultVal);
	registers.set("ST0", c.hexZeroExtend(hexConvertedVal,20));
	registers.x87().status().set("C3",'0');
	registers.x87().status().set("C0",'0');
	registers.x87().status().set("C2",'0');
	registers.x87().pop();
}