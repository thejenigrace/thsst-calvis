execute(registers, memory) {
    String st0 = registers.get("ST0");
    // split significand and exponent
    // st0 = gets exponent
    // significand is pushed
    Calculator c = new Calculator(registers, memory);
    double origValue = Double.parseDouble(registers.get("ST0"));

    String hexValNow = c.convertDoublePrecisionToHexString(Double.parseDouble(registers.get("ST0")));
    BigInteger biSrc = new BigInteger(hexValNow, 16);

    String bitVal = c.binaryZeroExtend(biSrc.toString(2), 64);


    String exponent = bitVal.substring(1,12);
    String mantissa = bitVal.substring(12);


    int fixed = 1023;
    double deciExp = new BigInteger(exponent, 2).intValue();
    deciExp -= fixed;
    double manResult = floatToBinaryString(mantissa, origValue);
    String hexResExp = c.convertDoublePrecisionToHexString(deciExp);
    String hexResMan = c.convertDoublePrecisionToHexString(manResult);
    if(Double.parseDouble(registers.get("ST0")) != 0) {
        registers.x87().push(manResult + "");
        registers.set("ST1",deciExp + "");
    }
    else{
        registers.getMxscr().setDivideByZeroFlag("1");
    }
    registers.x87().status().set("C3",'0');
    registers.x87().status().set("C0",'0');
}

double floatToBinaryString( String mantissa, double origValue) {
    String val = ".";    // Setting up string for result
    double base  = 0.5;
    double sum = 0;

    for(int x = 0; x < 52; x++) {
        if(mantissa.charAt(x) == '1') {
            sum += base;

        }

        base /= 2;
    }
    if(Math.abs((1.0 + sum) - origValue) < 0.000001)
        return origValue;
    else
        return 1.0 + sum;
}
