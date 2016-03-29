execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);

    int desSize = 0;
    int srcSize = 0;

    if( des.isRegister() ) {
		desSize = registers.getBitSize(des);
	}
	if( src.isRegister() ) {
		srcSize = registers.getBitSize(src);
	}

    if( des.isRegister() && checkSizeOfDestination(registers, desSize) ) {
        if( src.isRegister() ) {
            if( checkSizeOfSource(registers, srcSize) ) {
                String srcValue = registers.get(src);
                String desValue = registers.get(des);
                storeResultToRegister(registers, calculator, des, srcValue, desValue, desSize);
            }
            else {
                //throw exception
            }
        }
        else if( src.isMemory() ) {
            String srcValue = memory.read(src, 128);
            String desValue = registers.get(des);
            storeResultToRegister(registers, calculator, des, srcValue, desValue, desSize);
        }
    }
    else {
        //throw exception
    }
}

storeResultToRegister(registers, calculator, des, srcValue, desValue, desSize) {
    String str4 = srcValue.substring(0,8);
    String str3 = srcValue.substring(8,16);
    String str2 = srcValue.substring(16,24);
    String str1 = srcValue.substring(24);

    System.out.println("str4 = " + str4);
    System.out.println("str3 = " + str3);
    System.out.println("str2 = " + str2);
    System.out.println("str1 = " + str1);

    Long dPacked4 = calculator.convertToSignedInteger(new BigInteger(str4,16), 32);
    Long dPacked3 = calculator.convertToSignedInteger(new BigInteger(str3,16), 32);
    Long dPacked2 = calculator.convertToSignedInteger(new BigInteger(str2,16), 32);
    Long dPacked1 = calculator.convertToSignedInteger(new BigInteger(str1,16), 32);

    String sPacked4 = calculator.toHexSinglePrecisionString(dPacked4);
    String sPacked3 = calculator.toHexSinglePrecisionString(dPacked3);
    String sPacked2 = calculator.toHexSinglePrecisionString(dPacked2);
    String sPacked1 = calculator.toHexSinglePrecisionString(dPacked1);

    registers.set(des, sPacked4 + sPacked3 + sPacked2 + sPacked1);
}

boolean checkSizeOfDestination(registers, desSize) {
    boolean checkSize = false;

    if( 128 == desSize ) {
        checkSize = true;
    }

    return checkSize;
}

boolean checkSizeOfSource(registers, srcSize) {
    boolean checkSize = false;

    if( 64 == srcSize || 128 == srcSize ) {
        checkSize = true;
    }

    return checkSize;
}
