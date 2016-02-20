execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();
    
    Token token = new Token(Token.REG, "AL");

    String sAL = calculator.hexToBinaryString(registers.get("AL"), token);

    BigInteger biLowerAL = new BigInteger(sAL.substring(4), 2);
    BigInteger biUpperAL = new BigInteger(sAL.substring(0, 4), 2);
    BigInteger biAL = new BigInteger(sAL, 2);
    BigInteger toAddAF = new BigInteger("0110", 2);
    BigInteger toAddAH = new BigInteger("00010000", 2);

    String oldAF = flags.getAuxiliaryFlag();
    String oldCF = flags.getCarryFlag();
    flags.setCarryFlag("0");

    if( oldAF.equals("1") || biLowerAL.intValue() > 9 ) {
        biAL = biAL.subtract(toAddAF);
        biAL = biAL.subtract(toAddAH);
        storeResultInDes(registers, calculator, biAL, token);

        //Flags
        flags.setCarryFlag("1");
        flags.setAuxiliaryFlag("1");
        setFlags(flags);
    }
    else {
        storeResultInDes(registers, calculator, biAL, token);

        //Flags
        flags.setCarryFlag("0");
        flags.setAuxiliaryFlag("0");
        setFlags(flags);
    }
}

storeResultInDes(registers, calculator, biAL, token) {
    BigInteger zeroOutAH = new BigInteger("00001111", 2);
    biAL = biAL.and(zeroOutAH);
    registers.set(token, calculator.binaryToHexString(biAL.toString(2), token));
}

setFlags(flags) {
    //OF, SF, ZF, PF are undefined
    flags.setOverflowFlag("0");
    flags.setSignFlag("0");
    flags.setZeroFlag("0");
    flags.setParityFlag("0");
}
