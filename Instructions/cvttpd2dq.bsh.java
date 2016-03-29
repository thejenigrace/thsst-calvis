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
    String s31To0 = calculator.convertHexDoublePrecisionToHexInteger(srcValue.substring(0,16));
    String s63To31 = calculator.convertHexDoublePrecisionToHexInteger(srcValue.substring(16));
    String s127To64 = "0000000000000000";

    registers.set(des, s127To64 + s63To31 + s31To0);
}

boolean checkSizeOfDestination(registers, desSize) {
    boolean checkSize = false;

    if( 64 == desSize ) {
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
