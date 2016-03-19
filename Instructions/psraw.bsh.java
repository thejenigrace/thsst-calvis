execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);

    int desSize = 0;

    if( des.isRegister() ) {
		desSize = registers.getBitSize(des);
	}

    if( des.isRegister() ) {
        if( src.isRegister() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                BigInteger count = new BigInteger(registers.get(src), 16);
                int source = count.intValue();
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            }
            else {
                //throw exception
            }
        }
        else if( src.isMemory() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                BigInteger count = new BigInteger(memory.read(src, desSize), 16);
                int source = count.intValue();
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            }
            else {
                //throw exception
            }
        }
        else if( src.isHex() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                BigInteger count = new BigInteger(src.getValue(), 16);
                int source = count.intValue();
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
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

storeResultToRegister(registers, calculator, des, source, destination, desSize) {
    // registers.set("MM1", "0004");
    // registers.set("MM0", "0010");
    // source = 2;
    // destination = registers.get("MM1");

    String result = "";
    Token token = new Token(Token.REG, "AX");

    if( source >= 0 && source <= 15 ) {
        String binaryDes = calculator.hexToBinaryString(destination, des);
        String bDes = "";
        String temp = "";
        BigInteger biDes;
        BigInteger biResult;
        StringBuffer buffer;
        StringBuffer bufferResult;

        if( desSize == 64 ) {
            for(int i = 0; i <= 12; i+=4) {
                if( i == 12 ) {
                    bDes = calculator.hexToBinaryString(destination.substring(i), token);
                    biDes = new BigInteger(bDes.substring(i), 2);
                    biResult = biDes.shiftRight(source);
                    temp = calculator.binarySignExtend(biResult.toString(2), token);
                    temp = calculator.binaryToHexString(temp, token);
                    result += temp;
                }
                else {
                    bDes = calculator.hexToBinaryString(destination.substring(i, i + 4), token);
                    biDes = new BigInteger(bDes.substring(i), 2);
                    biResult = biDes.shiftRight(source);
                    temp = calculator.binarySignExtend(biResult.toString(2), token);
                    temp = calculator.binaryToHexString(temp, token);
                    result += temp;
                }
            }
        }
        else if( desSize == 128 ) {
            for(int i = 0; i <= 28; i+=4) {
                if( i == 28 ) {
                    bDes = calculator.hexToBinaryString(destination.substring(i), token);
                    biDes = new BigInteger(bDes.substring(i), 2);
                    biResult = biDes.shiftRight(source);
                    temp = calculator.binarySignExtend(biResult.toString(2), token);
                    temp = calculator.binaryToHexString(temp, token);
                    result += temp;
                }
                else {
                    bDes = calculator.hexToBinaryString(destination.substring(i, i + 4), token);
                    biDes = new BigInteger(bDes.substring(i), 2);
                    biResult = biDes.shiftRight(source);
                    temp = calculator.binarySignExtend(biResult.toString(2), token);
                    temp = calculator.binaryToHexString(temp, token);
                    result += temp;
                }
            }
        }
    }
    else {

    }

    registers.set(des, result);
}

boolean checkSizeOfRegister(registers, desSize) {
    boolean checkSize = false;

    if( 128 == desSize || 64 == desSize ) {
        checkSize = true;
    }

    return checkSize;
}
