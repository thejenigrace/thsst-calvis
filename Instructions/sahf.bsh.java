execute(registers, memory){
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    Token token = new Token(Token.REG, "AH");

    String sAH = calculator.hexToBinaryString(registers.get("AH"), token);

    flags.setSignFlag(sAH.charAt(0) + "");
    flags.setZeroFlag(sAH.charAt(1) + "");
    flags.setAuxiliaryFlag(sAH.charAt(3) + "");
    flags.setParityFlag(sAH.charAt(5) + "");
    flags.setCarryFlag(sAH.charAt(7) + "");
}
