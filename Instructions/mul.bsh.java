execute(src, registers, memory) {
    int srcBitSize;
    String multiplier;
    Calculator calculator = new Calculator(registers, memory);
    if ( src.isRegister() ) {
        srcBitSize = registers.getBitSize(src);
        multiplier = registers.get(src);
    } else if( src.isMemory() ) {
        srcBitSize = memory.getBitSize(src);
        multiplier = memory.read(src, srcBitSize);
    }

    if ( srcBitSize == 8 ) {
        String multiplicand = registers.get("AL");
        multiply(registers, calculator, multiplicand, multiplier, null, "AX");
    } else if ( srcBitSize == 16 ) {
        String multiplicand = registers.get("AX");
        multiply(registers, calculator, multiplicand, multiplier, "DX", "AX");
    } else if ( srcBitSize == 32 ) {
        String multiplicand = registers.get("EAX");
        multiply(registers, calculator, multiplicand, multiplier, "EDX", "EAX");
    }
}

multiply(registers, calculator, multiplicand, multiplier, registerUpperHalf, registerLowerHalf) {

    BigInteger biMultiplicand = new BigInteger(multiplicand, 16);
    BigInteger biMultiplier = new BigInteger(multiplier, 16);
    BigInteger biResult = biMultiplicand.multiply(biMultiplier);

    int registerLowerHalfHexSize = registers.getHexSize(registerLowerHalf);

    // Get the substring of the result that will fit to the size of the Lower Half Register
    String result = biResult.toString(16);
    result = calculator.hexZeroExtend(result,registerLowerHalfHexSize*2);
    String finalResult = result.substring(result.length()-registerLowerHalfHexSize);
    registers.set(registerLowerHalf, finalResult);

    EFlags ef = registers.getEFlags();
    BigInteger checkUpperHalf;
    if ( registerUpperHalf != null ) {
        int start = result.length() - registerLowerHalfHexSize * 2;
        int end = result.length() - registerLowerHalfHexSize;
        String upperResult = result.substring(start, end);
        registers.set(registerUpperHalf,upperResult);
        checkUpperHalf = new BigInteger(upperResult, 16);
    } else {
        checkUpperHalf = new BigInteger(registers.get("AH"), 16);
    }

    if( checkUpperHalf.equals(BigInteger.ZERO) ) {
        ef.setCarryFlag("0");
        ef.setOverflowFlag("0");
    } else {
        ef.setCarryFlag("1");
        ef.setOverflowFlag("1");
    }
}
