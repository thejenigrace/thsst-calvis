execute(registers, memory) {
  System.out.println("AAD no operand");

  Calculator calculator = new Calculator(registers, memory);
  EFlags flags = registers.getEFlags();
  Token tokenAL = new Token(Token.REG, "AL");
  Token tokenAH = new Token(Token.REG, "AH");

  BigInteger biAL = new BigInteger(registers.get("AL"), 16);
  BigInteger biAH = new BigInteger(registers.get("AH"), 16);
  BigInteger biImm8 = new BigInteger("0A", 16);

  BigInteger result = biAH.multiply(biImm8);
  result = result.add(biAL);
  registers.set("AH", "00");
  registers.set("AL", result.toString(16));

  //Flags
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
