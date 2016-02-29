execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    Token tokenEDI = new Token(Token.REG, "EDI");
    Token tokenAL = new Token(Token.REG, "AL");

    BigInteger compare1 = new BigInteger("10000", 16);
    BigInteger compare2 = new BigInteger("0000", 16);

    cmp(registers, memory, flags, calculator, tokenEDI, tokenAL);

    if( flags.getDirectionFlag().equals("0") ) {
        BigInteger x = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.add(new BigInteger("1"));

        if( result.toString(16).equals("10000") || result.compareTo(compare1) == 1 ) {
            result = new BigInteger("00000000", 16);
            registers.set(tokenEDI, calculator.binaryToHexString(result.toString(2), tokenEDI));
        }
        else {
            registers.set(tokenEDI, calculator.binaryToHexString(result.toString(2), tokenEDI));
        }
    }
    else if( flags.getDirectionFlag().equals("1") ) {
        BigInteger x = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.subtract(new BigInteger("1"));

        if( result.toString(16).equals("-1") || result.compareTo(compare2) == -1 ) {
            result = new BigInteger("FFFF", 16);
            registers.set(tokenEDI, calculator.binaryToHexString(result.toString(2), tokenEDI));
        }
        else {
            registers.set(tokenEDI, calculator.binaryToHexString(result.toString(2), tokenEDI));
        }
    }
}

cmp(registers, memory, flags, calculator, tokenEDI, tokenAL) {
    String source = calculator.hexToBinaryString(memory.read(registers.get("EDI"), 8), tokenAL);
    String destination = calculator.hexToBinaryString(registers.get("AL"), tokenAL);

    String result = "";
    int r = 0;
    int borrow = 0;
    int carry = 0;
    int overflow = 0;

    for(int i = 7; i >= 0; i--) {
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
    String res = calculator.hexToBinaryString(d, tokenAL);
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
