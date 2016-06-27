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
    String i01 = i8.substring(0, 2);
    String i23 = i8.substring(2, 4);
    String i45 = i8.substring(4, 6);
    String i67 = i8.substring(6);
    String result = "";

    result += returnStringChoice(i01, source);
    result += returnStringChoice(i23, source);
    result += returnStringChoice(i45, destination);
    result += returnStringChoice(i67, destination);

    registers.set(des, result);
}

String returnStringChoice(i, value) {
    switch (i) {
        case "00":
            return value.substring(24);
        case "01":
            return value.substring(16, 24);
        case "10":
            return value.substring(8, 16);
        case "11":
            return value.substring(0, 8);
        default:
            return "00000000";
    }
}

boolean checkSizeOfRegister(registers, desSize) {
    boolean checkSize = false;

    if( 128 == desSize ) {
        checkSize = true;
    }

    return checkSize;
}
