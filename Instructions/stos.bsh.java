execute(cc, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();
    String conditionSize = cc.getValue().toUpperCase();

    Token token = new Token(Token.REG, "EDI");

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

    memory.write(registers.get("EDI"), registers.get(source), size);

    if( flags.getDirectionFlag().equals("0") ) {
        BigInteger x = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.add(offset);

        registers.set(token, calculator.binaryToHexString(result.toString(2), token));
    }
    else if( flags.getDirectionFlag().equals("1") ) {
        BigInteger x = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.subtract(offset);

        if ( result.signum() == -1 ) {
            registers.set(token, "0");
        } else {
            registers.set(token, calculator.binaryToHexString(result.toString(2), token));
        }
    }
}
