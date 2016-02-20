execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    Token tokenAL = new Token(Token.REG, "AL");

    String sAL = registers.get("AL");

    BigInteger biAL = new BigInteger(sAL, 16);
    BigInteger biImm8 = new BigInteger("0A", 16);

    BigInteger[] result = biAL.divideAndRemainder(biImm8);
    storeResultInDes(registers, result);

    //Flags
    setFlags(registers, flags, calculator, tokenAL);
}

execute(des, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();
    
    Token tokenAL = new Token(Token.REG, "AL");

    if( des.getValue().length() <= 2 ) {
        BigInteger biAL = new BigInteger(registers.get("AL"), 16);
        BigInteger biImm8 = new BigInteger(des.getValue(), 16);

        BigInteger[] result = biAL.divideAndRemainder(biImm8);
        storeResultInDes(registers, result);

        //Flags
        setFlags(registers, flags, calculator, tokenAL);
    }
}

storeResultInDes(registers, result) {
    registers.set("AH", result[0].toString(16));
    registers.set("AL", result[1].toString(16));
}

setFlags(registers, flags, calculator, tokenAL) {
    flags.setCarryFlag("0"); //undefined
    flags.setAuxiliaryFlag("0"); //undefined
    flags.setOverflowFlag("0"); //undefined

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
