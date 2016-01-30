 execute(des, src, registers, memory) {
 	Calculator calculator = new Calculator(registers, memory);

 	if ( des.isRegister() ) {
 		if ( src.isRegister() ) {
      System.out.println("XOR register and register");

      //get size of des, src
      int desSize = registers.getBitSize(des);
      int srcSize = registers.getBitSize(src);

      //check if des size is 8-, 16-, 32-bit
      boolean checkSize = false;
      for(int a : registers.getAvailableSizes()) {
        if(a == desSize) {
          checkSize = true;
        }
      }

      if( (desSize == srcSize) && checkSize ) {
        //get hex value of des, src then convert to binary
        String source = calculator.hexToBinaryString(registers.get(src), src);
        String destination = calculator.hexToBinaryString(registers.get(des), des);

        BigInteger biSrc = new BigInteger(source, 2);
        BigInteger biDes = new BigInteger(destination, 2);
        BigInteger biResult = biDes.xor(biSrc);

        String result = calculator.binaryToHexString(biResult.toString(2), des);
        registers.set(des, result);

        //FLAGS
        EFlags flags = registers.getEFlags();

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

        String parity = calculator.checkParity(r, des);
        flags.setParityFlag(parity);

        // flags.setAuxiliaryFlag(); undefined
      }
    }
 		else if ( src.isMemory() ) {
 			System.out.println("XOR register and memory");

 		}
    else if ( src.isHex() ) {
      System.out.println("XOR register and immediate");

      //get size of des, src
      int desSize = registers.getBitSize(des);
      int srcSize = src.getValue().length();

      //check if des size is 8-, 16-, 32-bit
      boolean checkSize = false;
      for(int a : registers.getAvailableSizes()) {
        if(a == desSize) {
          checkSize = true;
        }
      }

      if( (desSize >= srcSize) && checkSize ) {
        //get hex value of des, src then convert to binary
        String source = calculator.hexToBinaryString(src.getValue(), des);
        String destination = calculator.hexToBinaryString(registers.get(des), des);

        BigInteger biSrc = new BigInteger(source, 2);
        BigInteger biDes = new BigInteger(destination, 2);
        BigInteger biResult = biDes.xor(biSrc);

        String result = calculator.binaryToHexString(biResult.toString(2), des);
        registers.set(des, result);

        //FLAGS
        EFlags flags = registers.getEFlags();

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

        String parity = calculator.checkParity(r, des);
        flags.setParityFlag(parity);

        // flags.setAuxiliaryFlag(); undefined
      }
    }
 	}
 	else if ( des.isMemory() ) {
    if ( src.isRegister() ) {
 			System.out.println("XOR memory and register");

    }
 		else if ( src.isMemory() ) {
 			System.out.println("XOR memory and memory");

 		}
    else if ( src.isHex() ) {
  		System.out.println("XOR memory and immediate");

    }
  }
 }
