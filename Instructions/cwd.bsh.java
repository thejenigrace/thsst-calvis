execute(registers, memory) {
  Calculator calculator = new Calculator(registers, memory);

  Token tokenAX = new Token(Token.REG, "AX");
  Token tokenDX = new Token(Token.REG, "DX");

  String ax = calculator.hexToBinaryString(registers.get("AX"), tokenAX);
  String dx = calculator.hexToBinaryString(registers.get("DX"), tokenDX);
  StringBuffer buffer = new StringBuffer(dx);

  for(int i = 0; i < dx.length(); i++) {
    buffer.setCharAt(i, ax.charAt(0));
  }

  registers.set(tokenDX, calculator.binaryToHexString(buffer.toString(), tokenDX));
}
