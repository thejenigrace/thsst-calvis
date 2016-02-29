execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    Token token = new Token(Token.REG, "AX");

    registers.set(token, memory.read(registers.get("ESI"), 16));

    if( flags.getDirectionFlag().equals("0") ) {
        BigInteger x = new BigInteger(registers.get("ESI"), 16);
        BigInteger result = x.add(new BigInteger("2"));

        registers.set("ESI", calculator.binaryToHexString(result.toString(2), token));
    }
    else if( flags.getDirectionFlag().equals("1") ) {
        BigInteger x = new BigInteger(registers.get("ESI"), 16);
        BigInteger result = x.subtract(new BigInteger("2"));

        registers.set("ESI", calculator.binaryToHexString(result.toString(2), token));
    }
}
