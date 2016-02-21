execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);

    Token tokenAL = new Token(Token.REG, "AL");
    Token tokenAH = new Token(Token.REG, "AH");

    String al = calculator.hexToBinaryString(registers.get("AL"), tokenAL);
    String ah = calculator.hexToBinaryString(registers.get("AH"), tokenAH);
    StringBuffer buffer = new StringBuffer(ah);

    for(int i = 0; i < ah.length(); i++) {
        buffer.setCharAt(i, al.charAt(0));
    }

    registers.set(tokenAH, calculator.binaryToHexString(buffer.toString(), tokenAH));
}
