execute(des, src, immediate, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);

    int desSize = 0;
    int srcSize = 0;

    if( des.isRegister() ) {
		desSize = registers.getBitSize(des);
	}

    if( src.isRegister() ) {
		srcSize = registers.getBitSize(src);
	}

    if( des.isRegister() ) {
        if( src.isRegister() ) {
            if( (desSize == srcSize) && checkSizeOfRegister(registers, desSize) ) {
                String source = registers.get(src);
                String destination = registers.get(des);
                String i8 = calculator.hexToBinaryString(immediate.getValue(), 8);
                storeResultToRegister(registers, calculator, des, source, destination, i8, desSize);
            } else {
                //throw exception
            }
        } else if( src.isMemory() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                String source = memory.read(src, desSize);
                String destination = registers.get(des);
                String i8 = calculator.hexToBinaryString(immediate.getValue(), 8);
                storeResultToRegister(registers, calculator, des, source, destination, i8, desSize);
            } else {
                //throw exception
            }
        }
    } else {
        //throw exception
    }
}

storeResultToRegister(registers, calculator, des, source, destination, i8, desSize) {
    String i0 = i8.charAt(0) + "";
    String i1 = i8.charAt(1) + "";
    String result = "";

    if( i0.equals("0") ) {
        result += destination.substring(0, 16);
    } else {
        result += destination.substring(16);
    }

    if( i1.equals("0") ) {
        result += source.substring(0, 16);
    } else {
        result += source.substring(16);
    }

    registers.set(des, result);
}

boolean checkSizeOfRegister(registers, desSize) {
    boolean checkSize = false;

    if( 128 == desSize ) {
        checkSize = true;
    }

    return checkSize;
}
