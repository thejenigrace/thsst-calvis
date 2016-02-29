execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    Token token = new Token(Token.REG, "EAX");

    registers.set(token, memory.read(registers.get("ESI"), 32));

    if( flags.getDirectionFlag().equals("0") ) {
        BigInteger x = new BigInteger(registers.get("ESI"), 16);
        BigInteger result = x.add(new BigInteger("4"));

        registers.set("ESI", calculator.binaryToHexString(result.toString(2), token));
    }
    else if( flags.getDirectionFlag().equals("1") ) {
        BigInteger x = new BigInteger(registers.get("ESI"), 16);
        BigInteger result = x.subtract(new BigInteger("4"));

        registers.set("ESI", calculator.binaryToHexString(result.toString(2), token));
    }
}
