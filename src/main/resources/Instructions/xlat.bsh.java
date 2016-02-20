execute(registers, memory) {
	//get contents of registers BX and AL

	String BX = registers.get("EBX");
	String AL = registers.get("AL");

	// Add BX and AL and store in temp variable result
	int result = Integer.parseInt(BX,16) + Integer.parseInt(AL,16);	

	// transform result into hex string and store in string AL 
	AL = Integer.toHexString(result);

	Calculator cal = new Calculator(registers, memory);

	Token t = new Token (Token.REG, "AL");
	AL = cal.hexZeroExtend(AL, t);

	// get the last 2 bytes only
	AL = AL.substring(AL.length()-2, AL.length());

	// finally set the new value of AL
	registers.set("AL", AL);

}