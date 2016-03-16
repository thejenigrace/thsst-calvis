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
                storeResultToRegister(registers, calculator, des, source, destination, src);
            }
            else {
                //throw exception
            }
        }
        else if( src.isMemory() ) {
            String source = memory.read(src, 32);
            String destination = registers.get(des);
            storeResultToRegister(registers, calculator, des, source, destination, src);
        }
    }
    else {
        //throw exception
    }
}

storeResultToRegister(registers, calculator, des, source, destination, src) {
    String sLower = "";

    if( src.isMemory() ) {
        sLower = calculator.hexSinglePrecisionFPToHexInteger(source);
    }
    else if( src.isRegister() ) {
        sLower = calculator.hexSinglePrecisionFPToHexInteger(source.substring(24));
    }

    registers.set(des, sLower);
}

boolean checkSizeOfDestination(registers, desSize) {
    boolean checkSize = false;

    if( 32 == desSize ) {
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
