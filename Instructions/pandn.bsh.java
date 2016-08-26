execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);

    int desSize = 0;
    int srcSize = 0;
    String destination = "";

    if( des.isRegister() ) {
        desSize = registers.getBitSize(des);
        destination = calculator.hexToBinaryString(registers.get(des), des);
    }

    if( src.isRegister() ) {
        srcSize = registers.getBitSize(src);
    } else {
        srcSize = memory.getBitSize(src);
    }

    destination = performNot(calculator, des, destination, desSize);

    if( des.isRegister() ) {
        if( src.isRegister() ) {
            if( (desSize == srcSize) && checkSizeOfRegister(registers, desSize) ) {
                String source = calculator.hexToBinaryString(registers.get(src), src);
                storeResultToRegister(registers, calculator, des, source, destination);
            }
        } else if( src.isMemory() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                String source = calculator.hexToBinaryString(memory.read(src, desSize), src);
                storeResultToRegister(registers, calculator, des, source, destination);
            }
        }
    }
}

storeResultToRegister(registers, calculator, des, source, destination) {
    BigInteger biSrc = new BigInteger(source, 2);
    BigInteger biDes = new BigInteger(destination, 2);
    BigInteger biResult = biDes.and(biSrc);

    registers.set(des, calculator.binaryToHexString(biResult.toString(2), des));
}

String performNot(calculator, des, destination, desSize) {
    String result = "";

    for (int i = 0; i < desSize; i++) {
        if (destination.charAt(i) == '1') {
            result = result.concat("0");
        } else if (destination.charAt(i) == '0') {
            result = result.concat("1");
        }
    }

    return result;
}

boolean checkSizeOfRegister(registers, desSize) {
    boolean checkSize = false;

    if( 128 == desSize || 64 == desSize ) {
        checkSize = true;
    }

    return checkSize;
}
