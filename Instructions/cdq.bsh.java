execute(registers, memory) {
  System.out.println("CDQ");
  Calculator calculator = new Calculator(registers, memory);

  Token tokenEAX = new Token(Token.REG, "EAX");
  Token tokenEDX = new Token(Token.REG, "EDX");

  String eax = calculator.hexToBinaryString(registers.get("EAX"), tokenEAX);
  String edx = calculator.hexToBinaryString(registers.get("EDX"), tokenEDX);
  StringBuffer buffer = new StringBuffer(edx);

  for(int i = 0; i < edx.length(); i++) {
    buffer.setCharAt(i, eax.charAt(0));
  }

  registers.set(tokenEDX, calculator.binaryToHexString(buffer.toString(), tokenEDX));
}
