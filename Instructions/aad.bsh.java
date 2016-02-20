execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    Token tokenAL = new Token(Token.REG, "AL");
    Token tokenAH = new Token(Token.REG, "AH");

    BigInteger biImm8 = new BigInteger("0A", 16);
    storeResultInDes(registers, calculator, biImm8);

    //Flags
    setFlags(registers, flags, calculator, tokenAL);
}

execute(des, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();
    
    Token tokenAL = new Token(Token.REG, "AL");
    Token tokenAH = new Token(Token.REG, "AH");

    if( des.getValue().length() <= 2 ) {
        BigInteger biImm8 = new BigInteger(des.getValue(), 16);
        storeResultInDes(registers, calculator, biImm8);

        //Flags
        setFlags(registers, flags, calculator, tokenAL);
    }
}

storeResultInDes(registers, calculator, biImm8) {
    BigInteger biAL = new BigInteger(registers.get("AL"), 16);
    BigInteger biAH = new BigInteger(registers.get("AH"), 16);
    BigInteger ff = new BigInteger("FF", 16);

    BigInteger result = biAH.multiply(biImm8);
    result = result.add(biAL);
    result = result.and(ff);
    registers.set("AH", "00");
    registers.set("AL", result.toString(16));
}

setFlags(registers, flags, calculator, tokenAL) {
    //CF, OF, AF are undefined
    flags.setCarryFlag("0");
    flags.setOverflowFlag("0");
    flags.setAuxiliaryFlag("0");

    BigInteger bi = new BigInteger(registers.get("AL"), 16);
    if(bi.equals(BigInteger.ZERO)) {
        flags.setZeroFlag("1");
    }
    else {
        flags.setZeroFlag("0");
    }

    String r = calculator.hexToBinaryString(registers.get("AL"), tokenAL);
    String sign = "" + r.charAt(0);
    flags.setSignFlag(sign);

    String parity = calculator.checkParity(r);
    flags.setParityFlag(parity);
}
