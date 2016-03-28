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
                String source = registers.get(src);
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            } else {
                //throw exception
            }
        } else if( src.isMemory() ) {
            String source = memory.read(src, 64);
            String destination = registers.get(des);
            storeResultToRegister(registers, calculator, des, source, destination, desSize);
        }
    } else {
        //throw exception
    }
}

storeResultToRegister(registers, calculator, des, source, destination, desSize) {
    float fLower = calculator.hexToSinglePrecisionFloatingPoint(source.substring(8));
    float fUpper = calculator.hexToSinglePrecisionFloatingPoint(source.substring(0, 8));

    String sLower = calculator.singlePrecisionFloatingPointToHex(fLower);
    String sUpper = calculator.singlePrecisionFloatingPointToHex(fUpper);

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
