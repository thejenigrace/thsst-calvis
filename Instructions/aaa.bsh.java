execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    Token tokenAL = new Token(Token.REG, "AL");
    Token tokenAH = new Token(Token.REG, "AH");

    String sAL = calculator.hexToBinaryString(registers.get("AL"), tokenAL);
    String sAH = calculator.hexToBinaryString(registers.get("AH"), tokenAH);

    BigInteger biUpperAL = new BigInteger(sAL.substring(0, 4), 2);
    BigInteger biLowerAL = new BigInteger(sAL.substring(4), 2);
    BigInteger biAL = new BigInteger(sAL, 2);
    BigInteger biAH = new BigInteger(sAH, 2);
    BigInteger toAddAL = new BigInteger("0110", 2);
    BigInteger toAddAH = new BigInteger("0001", 2);

    String oldAF = flags.getAuxiliaryFlag();
    String oldCF = flags.getCarryFlag();
    flags.setCarryFlag("0");

    if( oldAF.equals("1") || biLowerAL.intValue() > 9 ) {
        biAL = biAL.add(toAddAL);
        biAH = biAH.add(toAddAH);
        storeResultInDes(registers, calculator, biAL, biAH, tokenAL, tokenAH);

        //Flags
        flags.setCarryFlag("1");
        flags.setAuxiliaryFlag("1");
        setFlags(flags);
    }
    else {
        storeResultInDes(registers, calculator, biAL, biAH, tokenAL, tokenAH);

        //Flags
        flags.setCarryFlag("0");
        flags.setAuxiliaryFlag("0");
        setFlags(flags);
    }
}

storeResultInDes(registers, calculator, biAL, biAH, tokenAL, tokenAH) {
    BigInteger zeroOutAL = new BigInteger("00001111", 2);
    biAL = biAL.and(zeroOutAL);

    registers.set(tokenAL, calculator.binaryToHexString(biAL.toString(2), tokenAL));
    registers.set(tokenAH, calculator.binaryToHexString(biAH.toString(2), tokenAH));
}

setFlags(flags) {
    //OF, SF, ZF, PF are undefined
    flags.setOverflowFlag("0");
    flags.setSignFlag("0");
    flags.setZeroFlag("0");
    flags.setParityFlag("0");
}
