execute(cc, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();
    String conditionSize = cc.getValue().toUpperCase();

    String source = "";
    int size = 0;
    BigInteger offset;

    switch ( conditionSize ) {
    case "B":
        source = "AL";
        size = 8;
        offset = new BigInteger("1");
        break;
    case "W":
        source = "AX";
        size = 16;
        offset = new BigInteger("2");
        break;
    case "D":
        source = "EAX";
        size = 32;
        offset = new BigInteger("4");
        break;
    default:
        // invalid
    }

    Token token = new Token(Token.REG, source);

    registers.set(token, memory.read(registers.get("ESI"), size));

    if( flags.getDirectionFlag().equals("0") ) {
        BigInteger x = new BigInteger(registers.get("ESI"), 16);
        BigInteger result = x.add(offset);

        registers.set("ESI", calculator.binaryToHexString(result.toString(2), token));
    }
    else if( flags.getDirectionFlag().equals("1") ) {
        BigInteger x = new BigInteger(registers.get("ESI"), 16);
        BigInteger result = x.subtract(offset);

        registers.set("ESI", calculator.binaryToHexString(result.toString(2), token));
    }
}
