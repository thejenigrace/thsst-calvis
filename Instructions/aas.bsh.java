execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    Token tokenAX = new Token(Token.REG, "AX");
    Token tokenAH = new Token(Token.REG, "AH");

    String sAX = calculator.hexToBinaryString(registers.get("AX"), tokenAX);
    String sAH = calculator.hexToBinaryString(registers.get("AH"), tokenAH);

    BigInteger biLowerAL = new BigInteger(sAX.substring(12), 2);
    BigInteger biAX = new BigInteger(sAX, 2);
    BigInteger biAH = new BigInteger(sAH, 2);
    BigInteger toAddAF = new BigInteger("0000000000000110", 2);
    BigInteger toAddAH = new BigInteger("000000001", 2);

    String oldAF = flags.getAuxiliaryFlag();
    String oldCF = flags.getCarryFlag();
    flags.setCarryFlag("0");

    if ( oldAF.equals("1") || biLowerAL.intValue() > 9 ) {
        biAX = biAX.subtract(toAddAF);
        biAH = biAH.subtract(toAddAH);
        storeResultInDes(registers, calculator, biAX, biAH, tokenAX, tokenAH);

        //Flags
        flags.setCarryFlag("1");
        flags.setAuxiliaryFlag("1");
        setFlags(flags);
    } else {
        storeResultInDes(registers, calculator, biAX, biAH, tokenAX, tokenAH);

        //Flags
        flags.setCarryFlag("0");
        flags.setAuxiliaryFlag("0");
        setFlags(flags);
    }
}

storeResultInDes(registers, calculator, biAX, biAH, tokenAX, tokenAH){
    BigInteger zeroOutAL = new BigInteger("00001111", 2);
    biAX = biAX.and(zeroOutAL);

    registers.set(tokenAX, calculator.binaryToHexString(biAX.toString(2), tokenAX));
    registers.set(tokenAH, calculator.binaryToHexString(biAH.toString(2), tokenAH));
}

setFlags(flags) {
    //OF, SF, ZF, PF are undefined
    flags.setOverflowFlag("0");
    flags.setSignFlag("0");
    flags.setZeroFlag("0");
    flags.setParityFlag("0");
}
