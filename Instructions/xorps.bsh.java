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

    if( des.isRegister() && checkSizeOfRegister(registers, desSize) ) {
        if( src.isRegister() ) {
            if( desSize == srcSize ) {
                String source = calculator.hexToBinaryString(registers.get(src), src);
                String destination = calculator.hexToBinaryString(registers.get(des), des);
                storeResultToRegister(registers, calculator, des, source, destination);
            }
            else {
                //throw exception
            }
        }
        else if( src.isMemory() ) {
            String source = calculator.hexToBinaryString(memory.read(src, desSize), src);
            String destination = calculator.hexToBinaryString(registers.get(des), des);
            storeResultToRegister(registers, calculator, des, source, destination);
        }
    }
    else {
        //throw exception
    }
}

storeResultToRegister(registers, calculator, des, source, destination) {
    BigInteger biSrc = new BigInteger(source, 2);
    BigInteger biDes = new BigInteger(destination, 2);
    BigInteger biResult = biDes.xor(biSrc);

    registers.set(des, calculator.binaryToHexString(biResult.toString(2), des));
}

boolean checkSizeOfRegister(registers, desSize) {
    boolean checkSize = false;

    if( 128 == desSize || 64 == desSize ) {
        checkSize = true;
    }

    return checkSize;
}
