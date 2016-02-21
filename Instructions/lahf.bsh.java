execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    Token token = new Token(Token.REG, "AH");

    StringBuffer buffer = new StringBuffer(calculator.hexToBinaryString(registers.get("AH"), token));
    buffer.setCharAt(0, flags.getSignFlag().charAt(0));
    buffer.setCharAt(1, flags.getZeroFlag().charAt(0));
    buffer.setCharAt(3, flags.getAuxiliaryFlag().charAt(0));
    buffer.setCharAt(5, flags.getParityFlag().charAt(0));
    buffer.setCharAt(7, flags.getCarryFlag().charAt(0));

    registers.set(token, calculator.binaryToHexString(buffer.toString(), token));
}
