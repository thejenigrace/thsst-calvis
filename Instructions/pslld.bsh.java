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

    if( source >= 0 && source <= 31 ) {
        String binaryDes = calculator.hexToBinaryString(destination, des);
        if( desSize == 64 ) {
            for(int i = 0; i <= 8; i+=8) {
                BigInteger biDes = new BigInteger(binaryDes.substring(i, i + 8), 16);
                BigInteger biResult = biDes.shiftLeft(source);
                result += biResult.toString(16);
            }
        }
        else if( desSize == 128 ) {
            for(int i = 0; i <= 24; i+=8) {
                BigInteger biDes = new BigInteger(binaryDes.substring(i, i + 8), 16);
                BigInteger biResult = biDes.shiftLeft(source);
                result += biResult.toString(16);
            }
        }
    }
    else {
        result = calculator.hexZeroExtend("0", des);
    }

    System.out.println("Result: " + result);

    registers.set(des, result);
}

boolean checkSizeOfRegister(registers, desSize) {
    boolean checkSize = false;

    if( 128 == desSize || 64 == desSize ) {
        checkSize = true;
    }

    return checkSize;
}
