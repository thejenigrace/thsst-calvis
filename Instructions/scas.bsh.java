execute(cc, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    String conditionSize = cc.getValue().toUpperCase();

    String registerName = "";
    int size = 0;
    BigInteger offset;

    switch ( conditionSize ) {
        case "B":
            registerName = "AL";
            size = 8;
            offset = new BigInteger("1");
            break;
        case "W":
            registerName = "AX";
            size = 16;
            offset = new BigInteger("2");
            break;
        case "D":
            registerName = "EAX";
            size = 32;
            offset = new BigInteger("4");
            break;
        default:
            // invalid
    }

    Token tokenEDI = new Token(Token.REG, "EDI");
    Token tokenA = new Token(Token.REG, registerName);

    cmp(registers, memory, flags, calculator, tokenEDI, tokenA, size, registerName);

    if( flags.getDirectionFlag().equals("0") ) {
        BigInteger x = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.add(offset);

        registers.set(tokenEDI, calculator.binaryToHexString(result.toString(2), tokenEDI));
    }
    else if( flags.getDirectionFlag().equals("1") ) {
        BigInteger x = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.subtract(offset);

        registers.set(tokenEDI, calculator.binaryToHexString(result.toString(2), tokenEDI));
    }
}

cmp(registers, memory, flags, calculator, tokenEDI, tokenA, size, registerName) {
    String source = calculator.hexToBinaryString(memory.read(registers.get("EDI"), size), tokenA);
    String destination = calculator.hexToBinaryString(registers.get(registerName), tokenA);

    String result = "";
    int r = 0;
    int borrow = 0;
    int carry = 0;
    int overflow = 0;

    for(int i = size - 1; i >= 0; i--) {
        r = Integer.parseInt(String.valueOf(destination.charAt(i)))
            - Integer.parseInt(String.valueOf(source.charAt(i)))
            - borrow;

        if( r < 0 ) {
            borrow = 1;
            r += 2;
            result = result.concat(r.toString());

            if( i == 0 ) {
                carry = 1;
            }
            if( i == 0 || i == 1) {
                overflow++;
            }
        }
        else {
            borrow = 0;
            result = result.concat(r.toString());
        }
    }

    String d = new StringBuffer(result).reverse().toString();

    //FLAGS
    String res = d;
    BigInteger biR = new BigInteger(res, 2);

    flags.setCarryFlag(carry.toString());

    if(overflow == 1) {
        flags.setOverflowFlag("1");
    }
    else {
        flags.setOverflowFlag("0");
    }

    BigInteger compareToZero = new BigInteger(d, 2);
    if(compareToZero.equals(BigInteger.ZERO)) {
        flags.setZeroFlag("1");
    }
    else {
        flags.setZeroFlag("0");
    }

    String sign = "" + res.charAt(0);
    flags.setSignFlag(sign);

    String parity = calculator.checkParity(res);
    flags.setParityFlag(parity);

    String auxiliary = calculator.checkAuxiliarySub(source, destination);
    flags.setAuxiliaryFlag(auxiliary);
}
