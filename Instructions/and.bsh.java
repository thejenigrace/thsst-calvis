execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    if ( des.isRegister() ) {
        if ( src.isRegister() ) {
            int desSize = registers.getBitSize(des);
            int srcSize = registers.getBitSize(src);

            if( (desSize == srcSize) && checkSizeOfRegister(registers, desSize) ) {
                String source = calculator.hexToBinaryString(registers.get(src), src);
                String destination = calculator.hexToBinaryString(registers.get(des), des);
                storeResultToRegister(registers, calculator, des, source, destination);

                //FLAGS
                setFlagsRegister(registers, flags, calculator, des);
            }
        }
        else if ( src.isMemory() ) {
            int desSize = registers.getBitSize(des);

            if( checkSizeOfRegister(registers, desSize) ) {
                String destination = calculator.hexToBinaryString(registers.get(des), des);
                String source = calculator.hexToBinaryString(memory.read(src, registers.getBitSize(des)), des);
                storeResultToRegister(registers, calculator, des, source, destination);

                //FLAGS
                setFlagsRegister(registers, flags, calculator, des);
            }
        }
        else if ( src.isHex() ) {
            int desSize = registers.getBitSize(des);
            int srcSize = src.getValue().length();

            if( (desSize >= srcSize) && checkSizeOfRegister(registers, desSize) ) {
                String source = calculator.hexToBinaryString(src.getValue(), des);
                String destination = calculator.hexToBinaryString(registers.get(des), des);
                storeResultToRegister(registers, calculator, des, source, destination);

                //FLAGS
                setFlagsRegister(registers, flags, calculator, des);
            }
        }
    }
    else if ( des.isMemory() ) {
    if ( src.isRegister() ) {
      System.out.println("AND memory and register");

      //get size of des, src
      int srcSize = registers.getBitSize(src);

      String source = calculator.hexToBinaryString(registers.get(src), src);
      String d = memory.read(des, srcSize);
      String destination = calculator.hexToBinaryString(d, src);

      BigInteger biSrc = new BigInteger(source, 2);
      BigInteger biDes = new BigInteger(destination, 2);
      BigInteger biResult = biDes.and(biSrc);

      String result = calculator.binaryToHexString(biResult.toString(2), src);
      memory.write(des, result, srcSize);

      //FLAGS
      EFlags flags = registers.getEFlags();

      flags.setCarryFlag("0");
      flags.setOverflowFlag("0");

      BigInteger bi = new BigInteger(memory.read(des, srcSize), 16);
      if(bi.equals(BigInteger.ZERO)) {
        flags.setZeroFlag("1");
      }
      else {
        flags.setZeroFlag("0");
      }

      String r = calculator.hexToBinaryString(memory.read(des, srcSize), src);
      String sign = "" + r.charAt(0);
      flags.setSignFlag(sign);

      String parity = calculator.checkParity(r);
      flags.setParityFlag(parity);

      flags.setAuxiliaryFlag("0"); //undefined
    }
    else if ( src.isHex() ) {
      System.out.println("AND memory and immediate");

      String source = calculator.hexToBinaryString(src.getValue(), des);
      String d = memory.read(des, des);
      String destination = calculator.hexToBinaryString(d, des);

      BigInteger biSrc = new BigInteger(source, 2);
      BigInteger biDes = new BigInteger(destination, 2);
      BigInteger biResult = biDes.and(biSrc);

      String result = calculator.binaryToHexString(biResult.toString(2), des);
	  System.out.println("Result: " + result);
	  System.out.println(des.getValue());
      memory.write(des, result, des);

      //FLAGS
      EFlags flags = registers.getEFlags();

      flags.setCarryFlag("0");
      flags.setOverflowFlag("0");

      BigInteger bi = new BigInteger(memory.read(des, des), 16);
      if(bi.equals(BigInteger.ZERO)) {
        flags.setZeroFlag("1");
      }
      else {
        flags.setZeroFlag("0");
      }

      String r = calculator.hexToBinaryString(memory.read(des, des), des);
      String sign = "" + r.charAt(0);
      flags.setSignFlag(sign);

      String parity = calculator.checkParity(r);
      flags.setParityFlag(parity);

      flags.setAuxiliaryFlag("0"); //undefined
    }
  }

}

storeResultToRegister(registers, calculator, des, source, destination) {
    BigInteger biSrc = new BigInteger(source, 2);
    BigInteger biDes = new BigInteger(destination, 2);
    BigInteger biResult = biDes.and(biSrc);

    registers.set(des, calculator.binaryToHexString(biResult.toString(2), des));
}

storeResultToMemory(memory, calculator, des, src, source, destination) {
    BigInteger biSrc = new BigInteger(source, 2);
    BigInteger biDes = new BigInteger(destination, 2);
    BigInteger biResult = biDes.and(biSrc);

    System.out.println("res: " + calculator.binaryToHexString(biResult.toString(2), src)
                        + "des size " + memory.getBitSize(src));

    memory.write(des, calculator.binaryToHexString(biResult.toString(2), src), src);
}

setFlagsRegister(registers, flags, calculator, des) {
    flags.setCarryFlag("0");
    flags.setOverflowFlag("0");

    BigInteger bi = new BigInteger(registers.get(des), 16);
    if(bi.equals(BigInteger.ZERO)) {
        flags.setZeroFlag("1");
    }
    else {
        flags.setZeroFlag("0");
    }

    String r = calculator.hexToBinaryString(registers.get(des), des);
    String sign = "" + r.charAt(0);
    flags.setSignFlag(sign);

    String parity = calculator.checkParity(r);
    flags.setParityFlag(parity);

    flags.setAuxiliaryFlag("0"); //undefined
}

setFlagsMemory(memory, flags, calculator, des, src) {
    flags.setCarryFlag("0");
    flags.setOverflowFlag("0");

    BigInteger bi = new BigInteger(memory.read(des, src), 16);
    if(bi.equals(BigInteger.ZERO)) {
        flags.setZeroFlag("1");
    }
    else {
        flags.setZeroFlag("0");
    }

    String r = calculator.hexToBinaryString(memory.read(des, src), src);
    String sign = "" + r.charAt(0);
    flags.setSignFlag(sign);

    String parity = calculator.checkParity(r);
    flags.setParityFlag(parity);

    flags.setAuxiliaryFlag("0"); //undefined
}

boolean checkSizeOfRegister(registers, desSize) {
    boolean checkSize = false;
    for(int a : registers.getAvailableSizes()) {
        if(a == desSize) {
            checkSize = true;
        }
    }

    return checkSize;
}
