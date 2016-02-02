execute(registers, memory) {
  Calculator calculator = new Calculator(registers, memory);
  EFlags flags = registers.getEFlags();
  Token token = new Token(Token.REG, "AL");

  String sAL = calculator.hexToBinaryString(registers.get("AL"), token);

  String upperNibbleAL = sAL.substring(0, 4);
  String lowerNibbleAL = sAL.substring(4)

  BigInteger biLowerAL = new BigInteger(lowerNibbleAL, 2);
  BigInteger biUpperAL = new BigInteger(upperNibbleAL, 2);
  BigInteger biAL = new BigInteger(sAL, 2);
  BigInteger toAddAF = new BigInteger("0110", 2);
  BigInteger toAddCF = new BigInteger("01100000", 2);

  String oldAF = flags.getAuxiliaryFlag();
  String oldCF = flags.getCarryFlag();
  flags.setCarryFlag("0");

  if( oldAF.equals("1") || biLowerAL.intValue > 9 ) {
    biAL = biAL.add(toAddAF);
    registers.set(token, calculator.binaryToHexString(biAL.toString(2), token));
    flags.setCarryFlag(oldCF);
    flags.setAuxiliaryFlag("1");

    //Flags
    flags.setOverflowFlag("0");
  }
  else {
    flags.setAuxiliaryFlag("0");

    //Flags
    flags.setOverflowFlag("0");
  }

  if( oldCF.equals("1") || biUpperAL.intValue > 9 ) {
    biAL = biAL.add(toAddCF);
    registers.set(token, calculator.binaryToHexString(biAL.toString(2), token));
    flags.setCarryFlag("1");
  }
  else {
    flags.setCarryFlag("0");
  }
}
