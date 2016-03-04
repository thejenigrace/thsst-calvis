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
	else {
		srcSize = memory.getBitSize(src);
	}

    if( des.isRegister() ) {
        if( src.isRegister() ) {
            if( (desSize == srcSize) && checkSizeOfRegister(registers, desSize) ) {
                String source = calculator.hexToBinaryString(registers.get(src), src);
                String destination = calculator.hexToBinaryString(registers.get(des), des);
                storeResultToRegister(registers, calculator, des, source, destination);
            }
            else {
                //throw exception
            }
        }
        else if( src.isMemory() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                String source = calculator.hexToBinaryString(memory.read(src, desSize), src);
                String destination = calculator.hexToBinaryString(registers.get(des), des);
                storeResultToRegister(registers, calculator, des, source, destination);
            }
            else {
                //throw exception
            }
        }
    }
    else {
        //throw exception
    }
}

storeResultToRegister(registers, calculator, des, source, destination) {
    BigInteger biSrc = new BigInteger(source, 2);
    BigInteger biDes = new BigInteger(destination, 2);
    BigInteger biResult = biDes.and(biSrc);

    registers.set(des, calculator.binaryToHexString(biResult.toString(2), des));
}

boolean checkSizeOfRegister(registers, desSize) {
    boolean checkSize = false;

    if( 128 == desSize || 64 == desSize ) {
        checkSize = true;
    }

    return checkSize;
}
