execute(registers, memory) {
    /*String st0 = registers.get("ST0");
    // split significand and exponent
    // st0 = gets exponent
    // significand is pushed
    registers.set("ST0", st0);*/
	
	String valueZero = registers.get("ST0");
	String valueOne = registers.get("ST1");
	Calculator c = new Calculator(registers, memory);
	double regValZero = Double.parseDouble(valueZero);
	double regValOne = Double.parseDouble(valueOne);
	double resultVal = Math.atan(regValOne/regValZero);

	if(regValZero < Math.pow(2, 64) * -1 || regValOne < Math.pow(2, 64) * -1){
		registers.getMxscr().setUnderflowFlag("1");
	}
	else{
		registers.set("ST1", resultVal + "");
	}
	registers.x87().status().set("C2",'0');
//	String hexConvertedVal = c.convertDoublePrecisionToHexString(resultVal);

	registers.x87().status().set("C3",'0');
	registers.x87().status().set("C0",'0');
	registers.x87().status().set("C2",'0');
	registers.x87().pop();
}
