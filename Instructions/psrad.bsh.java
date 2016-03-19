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
    String result = "";
    Token token = new Token(Token.REG, "EAX");

    if( source >= 0 && source <= 31 ) {
        String binaryDes = calculator.hexToBinaryString(destination, des);
        if( desSize == 64 ) {
            for(int i = 0; i <= 8; i+=8) {
                if( i == 8 ) {
                    bDes = calculator.hexToBinaryString(destination.substring(i), token);
                    biDes = new BigInteger(bDes.substring(i), 2);
                    biResult = biDes.shiftRight(source);
                    temp = calculator.binarySignExtend(biResult.toString(2), token);
                    temp = calculator.binaryToHexString(temp, token);
                    result += temp;
                }
                else {
                    bDes = calculator.hexToBinaryString(destination.substring(i, i + 8), token);
                    biDes = new BigInteger(bDes.substring(i), 2);
                    biResult = biDes.shiftRight(source);
                    temp = calculator.binarySignExtend(biResult.toString(2), token);
                    temp = calculator.binaryToHexString(temp, token);
                    result += temp;
                }
            }
        }
        else if( desSize == 128 ) {
            for(int i = 0; i <= 24; i+=8) {
                if( i == 24 ) {
                    bDes = calculator.hexToBinaryString(destination.substring(i), token);
                    biDes = new BigInteger(bDes.substring(i), 2);
                    biResult = biDes.shiftRight(source);
                    temp = calculator.binarySignExtend(biResult.toString(2), token);
                    temp = calculator.binaryToHexString(temp, token);
                    result += temp;
                }
                else {
                    bDes = calculator.hexToBinaryString(destination.substring(i, i + 8), token);
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
