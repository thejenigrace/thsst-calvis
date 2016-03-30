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

	if( des.isRegister() && src.isRegister() ) {
		if( checkSizeOfSource(registers, srcSize) && checkSizeOfDestination(registers, desSize) ) {
			String source = registers.get(src);
			storeResultToRegister(registers, des, source);
		}
		else {
			//throw exception
		}
	}
	else {
		//throw exception
	}
}

storeResultToRegister(registers, des, source) {
	registers.set(des, source.substring(16));
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
