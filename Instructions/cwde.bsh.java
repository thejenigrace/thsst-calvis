execute(registers, memory) {
  Calculator calculator = new Calculator(registers, memory);

  Token tokenAX = new Token(Token.REG, "AX");
  Token tokenEAX = new Token(Token.REG, "EAX");

  String ax = calculator.hexToBinaryString(registers.get("AX"), tokenAX);
  String eax = calculator.hexToBinaryString(registers.get("EAX"), tokenEAX);
  StringBuffer buffer = new StringBuffer(eax);

  for(int i = 0; i < 16; i++) {
    buffer.setCharAt(i, ax.charAt(0));
  }

  registers.set(tokenEAX, calculator.binaryToHexString(buffer.toString(), tokenEAX));
}
