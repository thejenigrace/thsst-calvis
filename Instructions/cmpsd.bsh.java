// GP CMPSD String Instruction
execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    Token tokenEDI = new Token(Token.REG, "EDI");
    Token tokenESI = new Token(Token.REG, "ESI");
    Token tokenEAX = new Token(Token.REG, "EAX");

    cmp(registers, memory, flags, calculator, tokenEAX);

    if( flags.getDirectionFlag().equals("0") ) {
        BigInteger x = new BigInteger(registers.get("EDI"), 16);
        BigInteger y = new BigInteger(registers.get("ESI"), 16);
        BigInteger result = x.add(new BigInteger("4"));
        BigInteger result1 = y.add(new BigInteger("4"));

        registers.set(tokenEDI, calculator.binaryToHexString(result.toString(2), tokenEDI));
        registers.set(tokenESI, calculator.binaryToHexString(result1.toString(2), tokenESI));
    }
    else if( flags.getDirectionFlag().equals("1") ) {
        BigInteger x = new BigInteger(registers.get("EDI"), 16);
        BigInteger y = new BigInteger(registers.get("ESI"), 16);
        BigInteger result = x.subtract(new BigInteger("4"));
        BigInteger result1 = y.subtract(new BigInteger("4"));

        registers.set(tokenEDI, calculator.binaryToHexString(result.toString(2), tokenEDI));
        registers.set(tokenESI, calculator.binaryToHexString(result1.toString(2), tokenESI));
    }
}

cmp(registers, memory, flags, calculator, tokenEAX) {
    String destination = calculator.hexToBinaryString(memory.read(registers.get("EDI"), 8), tokenEAX);
    String source = calculator.hexToBinaryString(memory.read(registers.get("ESI"), 8), tokenEAX);

    String result = "";
    int r = 0;
    int borrow = 0;
    int carry = 0;
    int overflow = 0;

    for(int i = 31; i >= 0; i--) {
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
    String res = calculator.hexToBinaryString(d, tokenEAX);
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


// SSE2 CMPSD Comparison Instruction
execute(des,src,op3,registers,memory) {
  int QWORD = 64;
  String desValue = registers.get(des);
  String srcValue;
  Calculator calculator = new Calculator(registers,memory);
  if(src.isRegister()) {
    srcValue = registers.get(src);
    srcValue = calculator.cutToCertainHexSize("getLower",srcValue,QWORD/4);
  } else if(src.isMemory()) {
    srcValue = memory.read(src,QWORD);
  }

  String desNewValue = calculator.cutToCertainHexSize("getUpper",desValue,QWORD/4);

  // Get the desValue from 0 to 63 bit
  desValue = calculator.cutToCertainHexSize("getLower",desValue,QWORD/4);

  double doubleDes = calculator.convertHexToDoublePrecision(desValue);
  double doubleSrc = calculator.convertHexToDoublePrecision(srcValue);

  String operand = op3.getValue();
  int intOperand = Integer.parseInt(operand,16);
  System.out.println("intOperand = " + intOperand);

  int retval = Double.compare(doubleDes, doubleSrc);
  System.out.println("retval = " + retval);
  switch(intOperand) {
    case 0:
    // des == src
        if(retval == 0)
          registers.set(des,desNewValue.concat("FFFFFFFFFFFFFFFF"));
        else
          registers.set(des,desNewValue.concat("0000000000000000"));
        break;
    case 1:
    // des < src
        if(retval < 0)
          registers.set(des,desNewValue.concat("FFFFFFFFFFFFFFFF"));
        else
          registers.set(des,desNewValue.concat("0000000000000000"));
        break;
    case 2:
    // des <= src
        if(retval < 0 || retval == 0)
          registers.set(des,desNewValue.concat("FFFFFFFFFFFFFFFF"));
        else
          registers.set(des,desNewValue.concat("0000000000000000"));
        break;
    case 3:
        if(doubleDes.isNaN() || doubleSrc.isNaN())
            registers.set(des,desNewValue.concat("FFFFFFFF"));
        else
            registers.set(des,desNewValue.concat("00000000"));
        break;
    case 4:
    // des != src
        if(retval != 0)
          registers.set(des,desNewValue.concat("FFFFFFFFFFFFFFFF"));
        else
          registers.set(des,desNewValue.concat("0000000000000000"));
        break;
    case 5:
    // des >= src
        if(retval > 0 || retval == 0)
          registers.set(des,desNewValue.concat("FFFFFFFFFFFFFFFF"));
        else
          registers.set(des,desNewValue.concat("0000000000000000"));
        break;
    case 6:
    // des > src
        if(retval > 0)
          registers.set(des,desNewValue.concat("FFFFFFFFFFFFFFFF"));
        else
          registers.set(des,desNewValue.concat("0000000000000000"));
        break;
    case 7:
      if(!doubleDes.isNaN() && !doubleSrc.isNaN())
        registers.set(des,desNewValue.concat("FFFFFFFF"));
      else
        registers.set(des,desNewValue.concat("00000000"));
      break;
    default: System.out.println("NONE");
  }
}
