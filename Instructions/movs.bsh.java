execute(cc, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();
    String conditionSize = cc.getValue().toUpperCase();

    Token tokenX = new Token(Token.REG, "ESI");
    Token tokenY = new Token(Token.REG, "EDI");

    int size = 0;
    BigInteger offset;

    switch ( conditionSize ) {
        case "B": 
            size = 8;
            offset = new BigInteger("1");
            break;
        case "W":
            size = 16;
            offset = new BigInteger("2");
            break;
        case "D":
            size = 32;
            offset = new BigInteger("4");
            break;
        default:
            // invalid 
    }

    memory.write(registers.get("EDI"), memory.read(registers.get("ESI"), size), size);

    if( flags.getDirectionFlag().equals("0") ) {
        BigInteger x = new BigInteger(registers.get("ESI"), 16);
        BigInteger y = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.add(offset);
        BigInteger result1 = y.add(offset);

        registers.set("ESI", calculator.binaryToHexString(result.toString(2), tokenX));
        registers.set("EDI", calculator.binaryToHexString(result1.toString(2), tokenY));
    }
    else if( flags.getDirectionFlag().equals("1") ) {
        BigInteger x = new BigInteger(registers.get("ESI"), 16);
        BigInteger y = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.subtract(offset);
        BigInteger result1 = y.subtract(offset);

        registers.set("ESI", calculator.binaryToHexString(result.toString(2), tokenX));
        registers.set("EDI", calculator.binaryToHexString(result1.toString(2), tokenY));
    }
}
