execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();
    
    Token token = new Token(Token.REG, "AL");

    String sAL = calculator.hexToBinaryString(registers.get("AL"), token);

    BigInteger biLowerAL = new BigInteger(sAL.substring(4), 2);
    BigInteger biUpperAL = new BigInteger(sAL.substring(0, 4), 2);
    BigInteger biAL = new BigInteger(sAL, 2);
    BigInteger toSubtractAF = new BigInteger("0110", 2);
    BigInteger toSubtractCF = new BigInteger("01100000", 2);

    String oldAF = flags.getAuxiliaryFlag();
    String oldCF = flags.getCarryFlag();
    flags.setCarryFlag("0");

    if( oldAF.equals("1") || biLowerAL.intValue() > 9 ) {
        biAL = biAL.subtract(toSubtractAF);
        registers.set(token, calculator.binaryToHexString(biAL.toString(2), token));
        flags.setCarryFlag(oldCF);
        flags.setAuxiliaryFlag("1");

        //Flags
        setFlags(registers, flags, calculator, token);
    }
    else {
        flags.setAuxiliaryFlag("0");

        //Flags
        setFlags(registers, flags, calculator, token);
    }

    if( oldCF.equals("1") || biUpperAL.intValue() > 9 ) {
        biAL = biAL.subtract(toSubtractCF);
        registers.set(token, calculator.binaryToHexString(biAL.toString(2), token));
        flags.setCarryFlag("1");

        //Flags
        setFlags(registers, flags, calculator, token);
    }
}

setFlags(registers, flags, calculator, token) {
    flags.setOverflowFlag("0"); //undefined

    BigInteger bi = new BigInteger(registers.get("AL"), 16);
    if(bi.equals(BigInteger.ZERO)) {
        flags.setZeroFlag("1");
    }
    else {
        flags.setZeroFlag("0");
    }

    String r = calculator.hexToBinaryString(registers.get("AL"), token);
    String sign = "" + r.charAt(0);
    flags.setSignFlag(sign);

    String parity = calculator.checkParity(r);
    flags.setParityFlag(parity);
}
