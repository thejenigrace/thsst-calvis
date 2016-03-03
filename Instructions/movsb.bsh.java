execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    Token tokenX = new Token(Token.REG, "ESI");
    Token tokenY = new Token(Token.REG, "EDI");

    memory.write(registers.get("EDI"), memory.read(registers.get("ESI"), 8), 8);

    if( flags.getDirectionFlag().equals("0") ) {
        BigInteger x = new BigInteger(registers.get("ESI"), 16);
        BigInteger y = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.add(new BigInteger("1"));
        BigInteger result1 = y.add(new BigInteger("1"));

        registers.set("ESI", calculator.binaryToHexString(result.toString(2), tokenX));
        registers.set("EDI", calculator.binaryToHexString(result1.toString(2), tokenY));
    }
    else if( flags.getDirectionFlag().equals("1") ) {
        BigInteger x = new BigInteger(registers.get("ESI"), 16);
        BigInteger y = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.subtract(new BigInteger("1"));
        BigInteger result1 = x.subtract(new BigInteger("1"));

        registers.set("ESI", calculator.binaryToHexString(result.toString(2), tokenX));
        registers.set("EDI", calculator.binaryToHexString(result1.toString(2), tokenY));

    }
}
