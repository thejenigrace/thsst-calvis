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
        } else if( src.isMemory() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                BigInteger count = new BigInteger(memory.read(src, desSize), 16);
                int source = count.intValue();
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            }
        } else if( src.isHex() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                BigInteger count = new BigInteger(src.getValue(), 16);
                int source = count.intValue();
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            }
        }
    }
}

storeResultToRegister(registers, calculator, des, source, destination, desSize) {
    String result = "";
    Token token = new Token(Token.REG, "AX");

    if( source >= 0 && source <= 15 ) {
        String bDes = "";
        String temp = "";
        BigInteger biDes;
        BigInteger biResult;

        if( desSize == 64 ) {
            for(int i = 0; i <= 12; i+=4) {
                if( i == 12 ) {
                    bDes = calculator.hexToBinaryString(destination.substring(i), token);
                    biDes = new BigInteger(bDes, 2);
                    biResult = biDes.shiftRight(source);
                    StringBuffer bufferResult = new StringBuffer(calculator.binaryZeroExtend(biResult.toString(2), token));

                    for(int i = 0; i <= source; i++) {
                        bufferResult.setCharAt(i, bDes.charAt(0));
                    }

                    temp = calculator.binaryToHexString(bufferResult.toString(), token);
                    result += temp;
                } else {
                    bDes = calculator.hexToBinaryString(destination.substring(i, i + 4), token);
                    biDes = new BigInteger(bDes, 2);
                    biResult = biDes.shiftRight(source);
                    StringBuffer bufferResult = new StringBuffer(calculator.binaryZeroExtend(biResult.toString(2), token));

                    for(int i = 0; i <= source; i++) {
                        bufferResult.setCharAt(i, bDes.charAt(0));
                    }

                    temp = calculator.binaryToHexString(bufferResult.toString(), token);
                    result += temp;
                }
            }
        } else if( desSize == 128 ) {
            for(int i = 0; i <= 28; i+=4) {
                if( i == 28 ) {
                    bDes = calculator.hexToBinaryString(destination.substring(i), token);
                    biDes = new BigInteger(bDes, 2);
                    biResult = biDes.shiftRight(source);
                    StringBuffer bufferResult = new StringBuffer(calculator.binaryZeroExtend(biResult.toString(2), token));

                    for(int i = 0; i <= source; i++) {
                        bufferResult.setCharAt(i, bDes.charAt(0));
                    }

                    temp = calculator.binaryToHexString(bufferResult.toString(), token);
                    result += temp;
                } else {
                    bDes = calculator.hexToBinaryString(destination.substring(i, i + 4), token);
                    biDes = new BigInteger(bDes, 2);
                    biResult = biDes.shiftRight(source);
                    StringBuffer bufferResult = new StringBuffer(calculator.binaryZeroExtend(biResult.toString(2), token));

                    for(int i = 0; i <= source; i++) {
                        bufferResult.setCharAt(i, bDes.charAt(0));
                    }

                    temp = calculator.binaryToHexString(bufferResult.toString(), token);
                    result += temp;
                }
            }
        }
    } else {
        String binaryDes = "";
        String temp = "";
        String sign = "";

        if( desSize == 64 ) {
            for(int i = 0; i <= 12; i+=4) {
                if( i == 12 ) {
                    binaryDes = calculator.hexToBinaryString(destination.substring(i), token);
                    sign = binaryDes.charAt(0) + "";
                    temp = calculator.binarySignExtend(sign, token);
                    result += calculator.binaryToHexString(temp, token);
                } else {
                    binaryDes = calculator.hexToBinaryString(destination.substring(i, i + 4), token);
                    sign = binaryDes.charAt(0) + "";
                    temp = calculator.binarySignExtend(sign, token);
                    result += calculator.binaryToHexString(temp, token);
                }
            }
        } else if( desSize == 128 ) {
            for(int i = 0; i <= 28; i+=4) {
                if( i == 28 ) {
                    binaryDes = calculator.hexToBinaryString(destination.substring(i), token);
                    sign = binaryDes.charAt(0) + "";
                    temp = calculator.binarySignExtend(sign, token);
                    result += calculator.binaryToHexString(temp, token);
                } else {
                    binaryDes = calculator.hexToBinaryString(destination.substring(i, i + 4), token);
                    sign = binaryDes.charAt(0) + "";
                    temp = calculator.binarySignExtend(sign, token);
                    result += calculator.binaryToHexString(temp, token);
                }
            }
        }
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
