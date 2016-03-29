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
    String sPacked4 = calculator.convertHexSinglePrecisionToHexInteger(srcValue.substring(0,8));
    String sPacked3 = calculator.convertHexSinglePrecisionToHexInteger(srcValue.substring(8,16));
    String sPacked2 = calculator.convertHexSinglePrecisionToHexInteger(srcValue.substring(16,24));
    String sPacked1 = calculator.convertHexSinglePrecisionToHexInteger(srcValue.substring(24));

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

    if( 128 == srcSize ) {
        checkSize = true;
    }

    return checkSize;
}
