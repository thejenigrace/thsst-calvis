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

            }
            else {
                //throw exception
            }
        }
        else if( src.isMemory() ) {
            String srcValue = memory.read(src, 64);
            String desValue = registers.get(des);
            storeResultToRegister(registers,calculator,des,srcValue,desValue,desSize);
        }
    }
    else {
        //throw exception
    }
}

storeResultToRegister(registers,calculator,des,srcValue,desValue,desSize) {
    long dUpper = calculator.convertToSignedInteger(new BigInteger(srcValue.substring(0,8), 16), 32);
    long dLower = calculator.convertToSignedInteger(new BigInteger(srcValue.substring(8), 16), 32);

    String sUpper = calculator.convertIntegerToDoublePrecisionFP(dUpper);
    String sLower = calculator.convertIntegerToDoublePrecisionFP(dLower);

    registers.set(des, sUpper + sLower);
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

    if( 64 == srcSize ) {
        checkSize = true;
    }

    return checkSize;
}
