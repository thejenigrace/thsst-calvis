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
    Token token = new Token(Token.REG, "AX");

    if( source >= 0 && source <= 15 ) {
        String binaryDes = calculator.hexToBinaryString(destination, des);

        if( desSize == 64 ) {
            for(int i = 0; i <= 12; i+=4) {
                BigInteger biDes = new BigInteger(binaryDes.substring(i, i + 4), 16);
                BigInteger biResult = biDes.shiftRight(source);

                StringBuffer buffer = new StringBuffer(biResult.toString(2));
                StringBuffer bufferResult = new StringBuffer(calculator.binaryZeroExtend(buffer.toString(), token));

                for(int i = 0; i <= count.intValue(); i++) {
                    bufferResult.setCharAt(i, binaryDes.charAt(0));
                }

                result += calculator.binaryToHexString(bufferResult.toString(), token);
            }
        }
        else if( desSize == 128 ) {
            for(int i = 0; i <= 28; i+=4) {
                BigInteger biDes = new BigInteger(binaryDes.substring(i, i + 4), 16);
                BigInteger biResult = biDes.shiftRight(source);

                StringBuffer buffer = new StringBuffer(biResult.toString(2));
                StringBuffer bufferResult = new StringBuffer(calculator.binaryZeroExtend(buffer.toString(), token));

                for(int i = 0; i <= count.intValue(); i++) {
                    bufferResult.setCharAt(i, binaryDes.charAt(0));
                }

                result += calculator.binaryToHexString(bufferResult.toString(), token);
            }
        }
    }
    else {
        result = calculator.binaryToHexString(calculator.binarySignExtend("1", des), des);
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
