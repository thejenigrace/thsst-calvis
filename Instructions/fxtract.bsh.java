execute(registers, memory) {
    String st0 = registers.get("ST0");
    // split significand and exponent
    // st0 = gets exponent
    // significand is pushed
	Calculator c = new Calculator(registers, memory);
//	String value = "C05EDCCCCCCCCCCD";
	double origValue = Double.parseDouble(registers.get("ST0"));
	//System.out.println(origValue);
	String hexValNow = c.convertDoublePrecisionToHexString(Double.parseDouble(registers.get("ST0")));
	BigInteger biSrc = new BigInteger(hexValNow, 16);
	//System.out.println(biSrc.toString(16) + " hex form");
	String bitVal = c.binaryZeroExtend(biSrc.toString(2), 64);
	
	//System.out.println("complete :" + bitVal);
	String exponent = bitVal.substring(1,12);
	String mantissa = bitVal.substring(12);
	//System.out.println(exponent);
	//System.out.println(mantissa);
	int fixed = 1023;
	double deciExp = new BigInteger(exponent, 2).intValue();
	deciExp -= fixed;
	double manResult = floatToBinaryString(mantissa, origValue);
	String hexResExp = c.convertDoublePrecisionToHexString(deciExp);
	String hexResMan = c.convertDoublePrecisionToHexString(manResult);
	registers.set("ST1", deciExp + "");
	registers.set("ST0", manResult + "" );
	registers.x87().status().set("C3",'0');
	registers.x87().status().set("C0",'0');
}

double floatToBinaryString( String mantissa, double origValue) {
    String val = ".";    // Setting up string for result
	double base  = 0.5;
	double sum = 0;
	
	for(int x = 0; x < 52; x++){
		if(mantissa.charAt(x) == '1'){
			sum += base;
			//System.out.println("sum pls: " + sum);
		}
		System.out.println("base: " + base );
		base /= 2;
	}
	if(Math.abs((1.0 + sum) - origValue) < 0.000001)
		return origValue;
	else
		return 1.0 + sum;
}

