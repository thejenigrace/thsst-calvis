execute(registers, memory) {
  System.out.println("AAA");

  Calculator calculator = new Calculator(registers, memory);
  EFlags flags = registers.getEFlags();
  Token token = new Token(Token.REG, "AL");

  String sAL = calculator.hexToBinaryString(registers.get("AL"), token);
  String upperNibbleAL = sAL.substring(0, 4);
  String lowerNibbleAL = sAL.substring(4);

  BigInteger biLowerAL = new BigInteger(lowerNibbleAL, 2);
  BigInteger biUpperAL = new BigInteger(upperNibbleAL, 2);
  BigInteger biAL = new BigInteger(sAL, 2);
  BigInteger toAddAF = new BigInteger("0110", 2);
  BigInteger toAddAH = new BigInteger("00010000", 2);
  BigInteger zeroOutAH = new BigInteger("00001111", 2);

  String oldAF = flags.getAuxiliaryFlag();
  String oldCF = flags.getCarryFlag();
  flags.setCarryFlag("0");

  if( oldAF.equals("1") || biLowerAL.intValue() > 9 ) {
    biAL = biAL.add(toAddAF);
    biAL = biAL.add(toAddAH);
    biAL = biAL.and(zeroOutAH);
    registers.set(token, calculator.binaryToHexString(biAL.toString(2), token));
    flags.setCarryFlag("1");
    flags.setAuxiliaryFlag("1");

    //Flags
    flags.setOverflowFlag("0"); //undefined
    flags.setSignFlag("0"); //undefined
    flags.setZeroFlag("0"); //undefined
    flags.setParityFlag("0"); //undefined
  }
  else {
    flags.setCarryFlag("0");
    flags.setAuxiliaryFlag("0");

    biAL = biAL.and(zeroOutAH);
    registers.set(token, calculator.binaryToHexString(biAL.toString(2), token));

    //Flags
    flags.setOverflowFlag("0"); //undefined
    flags.setSignFlag("0"); //undefined
    flags.setZeroFlag("0"); //undefined
    flags.setParityFlag("0"); //undefined
  }
}
