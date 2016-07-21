/*execute(registers, memory) {
    String st0 = registers.get("ST0");
    String st1 = registers.get("ST1");
    // st1 = st1 * log base 2 (st0 + 1.0)
    registers.set("ST1", st1);
    // pop
    registers.x87().pop();
}*/
execute(registers, memory) {
    /*String st0 = registers.get("ST0");
    // st0 = 2 ^ (st0) - 1
    registers.set("ST0", st0);*/
	String valueZero = registers.get("ST0");
	String valueOne = registers.get("ST1");
	Calculator c = new Calculator(registers, memory);

	double regValZero = Double.parseDouble(valueZero);
	double regValOne = Double.parseDouble(valueOne);
	double resultVal = 0.0;
		System.out.println((- (1 - (Math.sqrt(2)/2) )) + " minimum");
		System.out.println((1 - (Math.sqrt(2)/2) ) + " minimum");
	if( - (1 - Math.sqrt(2)/2 ) > regValZero || (1 - Math.sqrt(2)/2 ) < regValZero){
		resultVal = 0.01;
		registers.mxscr.setInvalidOperationFlag("1");
	}else{
		resultVal = regValOne * (Math.log(regValZero + 1.0) / Math.log(2) );
	}
	
	
//	String hexConvertedVal = c.convertDoublePrecisionToHexString(resultVal);
	registers.set("ST1", resultVal + "");
	registers.x87().status().set("C3",'0');
	registers.x87().status().set("C0",'0');
	registers.x87().status().set("C2",'0');
		registers.x87().pop();
}
