// GP String Instruction
execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    Token tokenX = new Token(Token.REG, "ESI");
    Token tokenY = new Token(Token.REG, "EDI");

    memory.write(registers.get("EDI"), memory.read(registers.get("ESI"), 32), 32);

    if( flags.getDirectionFlag().equals("0") ) {
        BigInteger x = new BigInteger(registers.get("ESI"), 16);
        BigInteger y = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.add(new BigInteger("4"));
        BigInteger result1 = y.add(new BigInteger("4"));

        registers.set("ESI", calculator.binaryToHexString(result.toString(2), tokenX));
        registers.set("EDI", calculator.binaryToHexString(result1.toString(2), tokenY));
    }
    else if( flags.getDirectionFlag().equals("1") ) {
        BigInteger x = new BigInteger(registers.get("ESI"), 16);
        BigInteger y = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.subtract(new BigInteger("4"));
        BigInteger result1 = y.subtract(new BigInteger("4"));

        registers.set("ESI", calculator.binaryToHexString(result.toString(2), tokenX));
        registers.set("EDI", calculator.binaryToHexString(result1.toString(2), tokenY));
    }
}

// SSE2 Data Transfer Instruction
execute(des,src,registers,memory) {
    int QWORD = 64;
    String srcValue;
    Calculator calculator = new Calculator(registers,memory);

    if(src.isRegister()) {
        srcValue = registers.get(src);
        srcValue = calculator.cutToCertainHexSize("getLower",srcValue,QWORD/4);
    }

    if(des.isRegister() && src.isRegister()) {
        String desValue = registers.get(des);
        desValue = calculator.cutToCertainHexSize("getUpper",desValue,QWORD/4);
        srcValue = desValue.concat(srcValue);
        registers.set(des, srcValue);
    } else if(des.isRegister() && src.isMemory()) {
        srcValue = memory.read(src, QWORD);
        registers.set(des, srcValue);
    } else if(des.isMemory() && src.isRegister()) {
        memory.write(des, srcValue, QWORD);
    }
}
