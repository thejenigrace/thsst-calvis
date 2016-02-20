execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    if ( des.isRegister() ) {
        if ( src.isRegister() ) {
            int desSize = registers.getBitSize(des);
            int srcSize = registers.getBitSize(src);

            if( (desSize == srcSize) && checkSizeOfRegister(registers, desSize) ) {
                String source = calculator.hexToBinaryString(registers.get(src), src);
                String destination = calculator.hexToBinaryString(registers.get(des), des);
                String result = performTest(registers, calculator, des, source, destination);

                //Flags
                setFlagsRegister(registers, flags, calculator, des, result);
            }
        }
        else if ( src.isMemory() ) {
            int desSize = registers.getBitSize(des);

            if( checkSizeOfRegister(registers, desSize) ) {
                String destination = calculator.hexToBinaryString(registers.get(des), des);
                String source = calculator.hexToBinaryString(memory.read(src, registers.getBitSize(des)), des);
                String result = performTest(registers, calculator, des, source, destination);

                //Flags
                setFlagsRegister(registers, flags, calculator, des, result);
            }
        }
        else if ( src.isHex() ) {
            int desSize = registers.getBitSize(des);
            int srcSize = src.getValue().length();

            if( (desSize >= srcSize) && checkSizeOfRegister(registers, desSize) ) {
                String source = calculator.hexToBinaryString(src.getValue(), des);
                String destination = calculator.hexToBinaryString(registers.get(des), des);
                String result = performTest(registers, calculator, des, source, destination);

                //Flags
                setFlagsRegister(registers, flags, calculator, des, result);
            }
        }
    }
    else if ( des.isMemory() ) {
        if ( src.isRegister() ) {
            int srcSize = registers.getBitSize(src);

            String source = calculator.hexToBinaryString(registers.get(src), src);
            String destination = calculator.hexToBinaryString(memory.read(des, srcSize), src);

            BigInteger biSrc = new BigInteger(source, 2);
            BigInteger biDes = new BigInteger(destination, 2);
            BigInteger biResult = biDes.and(biSrc);

            String result = calculator.binaryToHexString(biResult.toString(2), src);

            //Flags
            BigInteger bi = new BigInteger(result, 16);
            String r = calculator.hexToBinaryString(result, src);
            setFlagsMemory(calculator, flags, bi, r);
        }
        else if ( src.isHex() ) {
            String source = calculator.hexToBinaryString(src.getValue(), des);
            String destination = calculator.hexToBinaryString(memory.read(des, des), des);

            BigInteger biSrc = new BigInteger(source, 2);
            BigInteger biDes = new BigInteger(destination, 2);
            BigInteger biResult = biDes.and(biSrc);

            String result = calculator.binaryToHexString(biResult.toString(2), des);

            //Flags
            BigInteger bi = new BigInteger(result, 16);
            String r = calculator.hexToBinaryString(result, des);
            setFlagsMemory(calculator, flags, bi, r);
        }
    }
}

String performTest(registers, calculator, des, source, destination) {
    BigInteger biSrc = new BigInteger(source, 2);
    BigInteger biDes = new BigInteger(destination, 2);
    BigInteger biResult = biDes.and(biSrc);

    return calculator.binaryToHexString(biResult.toString(2), des);
}

setFlagsRegister(registers, flags, calculator, des, result) {
    flags.setCarryFlag("0");
    flags.setOverflowFlag("0");
    flags.setAuxiliaryFlag("0"); //undefined

    BigInteger bi = new BigInteger(result, 16);
    if(bi.equals(BigInteger.ZERO)) {
        flags.setZeroFlag("1");
    }
    else {
        flags.setZeroFlag("0");
    }

    String r = calculator.hexToBinaryString(result, des);
    String sign = "" + r.charAt(0);
    flags.setSignFlag(sign);

    String parity = calculator.checkParity(r);
    flags.setParityFlag(parity);
}

setFlagsMemory(calculator, flags, bi, r) {
    flags.setCarryFlag("0");
    flags.setOverflowFlag("0");
    flags.setAuxiliaryFlag("0"); //undefined

    if(bi.equals(BigInteger.ZERO)) {
        flags.setZeroFlag("1");
    }
    else {
        flags.setZeroFlag("0");
    }

    String sign = "" + r.charAt(0);
    flags.setSignFlag(sign);

    String parity = calculator.checkParity(r);
    flags.setParityFlag(parity);
}

boolean checkSizeOfRegister(registers, desSize) {
    boolean checkSize = false;
    for(int a : registers.getAvailableSizes()) {
        if(a == desSize) {
            checkSize = true;
        }
    }

    return checkSize;
}
