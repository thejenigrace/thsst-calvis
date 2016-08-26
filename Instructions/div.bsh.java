execute(src, registers, memory) {
    int srcBitSize;
    String divisor;
    EFlags ef = registers.getEFlags();
    Calculator calculator = new Calculator(registers, memory);

    if ( src.isRegister() ) {

        srcBitSize = registers.getBitSize(src);
        divisor = registers.get(src);
    }else if( src.isMemory() ) {

        srcBitSize = memory.getBitSize(src);
        divisor = memory.read(src, srcBitSize);
    }

    if ( srcBitSize == 8 ) {
        String dividend = registers.get("AX");
        divide(registers, calculator, dividend, divisor, "AL", "AH");
    } else if ( srcBitSize == 16 ) {
        String dividend = registers.get("DX") + registers.get("AX");
        divide(registers, calculator, dividend, divisor, "AX", "DX");
    } else if ( srcBitSize == 32 ) {
        String dividend = registers.get("EDX") + registers.get("EAX");
        divide(registers, calculator, dividend, divisor, "EAX", "EDX");
    }
}

divide(registers, calculator, dividend, divisor, registerForQuotient, registerForRemainder) {
    // debugging

    BigInteger biDividend = new BigInteger(dividend, 16);
    BigInteger biDivisor = new BigInteger(divisor, 16);
    BigInteger[] biResult = biDividend.divideAndRemainder(biDivisor);

    long longQuotient = Long.parseLong(biResult[0].toString());
    long longRemainder = Long.parseLong(biResult[1].toString());




    int resultBitSize = registers.getBitSize(registerForQuotient);
    if(check(longQuotient, resultBitSize)) {
        int resultHexSize = registers.getHexSize(registerForQuotient);
        String quotient = calculator.cutToCertainHexSize("getLower", biResult[0].toString(16), resultHexSize);
        String remainder = calculator.cutToCertainHexSize("getLower", biResult[1].toString(16), resultHexSize);




        registers.set(registerForQuotient, quotient);
        registers.set(registerForRemainder, remainder);
    } else {
        throw new ArithmeticException("Divide Error");
    }
}

boolean check(long quotient, int size){
    boolean answer=false;
    Long max8=Long.parseLong("255");
    Long max16=Long.parseLong("65535");
    Long max32=Long.parseLong("4294967295");         //2E+32 - 1
    if( size==8 && max8>=quotient) {

        answer=true;
    }else if( size==16 && max16>=quotient) {

        answer=true;
    }else if( size==32 && max32>=quotient) {

        answer=true;
    }

    return answer;
}
