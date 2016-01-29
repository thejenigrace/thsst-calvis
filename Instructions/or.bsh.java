 execute(des, src, registers, memory) {
 	Calculator calculator = new Calculator(registers, memory);

 	if ( des.isRegister() ) {
 		if ( src.isRegister() ) {
      System.out.println("OR register and register");

      //get size of des, src
      int desSize = registers.getBitSize(des);
      int srcSize = registers.getBitSize(src);

      boolean checkSize = false;
      for(int a : registers.getAvailableSizes()) {
        if(a == desSize) {
          checkSize = true;
        }
      }

      if ( (desSize == srcSize) && checkSize ) {
        //get hex value of des, src then convert to binary
        String source = calculator.hexToBinaryString(registers.get(src), src);
        String destination = calculator.hexToBinaryString(registers.get(des), des);

        String result = "";

        for (int i = 0; i < desSize; i++) {
          if (source.charAt(i) == '0' && destination.charAt(i) == '0') {
            result = result.concat("0");
          }
          else {
            result = result.concat("1");
          }
        }

        result = calculator.binaryToHexString(result, des);
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
        String sign = "" + result.charAt(0);
        flags.setSignFlag(sign);

        String parity = calculator.checkParity(r, des);
        flags.setParityFlag(parity);

        System.out.println("CF: " + flags.getCarryFlag() +
                         "\nOF: " + flags.getOverflowFlag() +
                         "\nZF: " + flags.getZeroFlag() +
                         "\nSF: " + flags.getSignFlag() +
                         "\nPF: " + flags.getParityFlag() + "");
        // flags.setAuxiliaryFlag(); undefined
      }
    }
 		else if ( src.isMemory() ) {
 			System.out.println("OR register and memory");

 		}
    else if ( src.isHex() ) {
      System.out.println("OR register and immediate");

      //get size of des, src
      int desSize = registers.getBitSize(des);
      int srcSize = src.getValue().length();
      // registers.set(src, calculator.hexZeroExtend(src.getValue(), des));
      // int srcSize = registers.getBitSize(src);

      boolean checkSize = false;
      for(int a : registers.getAvailableSizes()) {
        if(a == desSize) {
          checkSize = true;
        }
      }

      if ( (desSize >= srcSize) && checkSize ) {
        //get hex value of des, src then convert to binary
        String source = calculator.hexToBinaryString(src.getValue(), des);
        String destination = calculator.hexToBinaryString(registers.get(des), des);

        String result = "";

        for (int i = 0; i < desSize; i++) {
          if (source.charAt(i) == '0' && destination.charAt(i) == '0') {
            result = result.concat("0");
          }
          else {
            result = result.concat("1");
          }
        }

        result = calculator.binaryToHexString(result, des);
        registers.set(des, result);
      }

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
      String sign = "" + result.charAt(0);
      flags.setSignFlag(sign);

      String parity = calculator.checkParity(r, des);
      flags.setParityFlag(parity);

      System.out.println("CF: " + flags.getCarryFlag() +
                       "\nOF: " + flags.getOverflowFlag() +
                       "\nZF: " + flags.getZeroFlag() +
                       "\nSF: " + flags.getSignFlag() +
                       "\nPF: " + flags.getParityFlag() + "");
      // flags.setAuxiliaryFlag(); undefined
    }
 	}
 	else if ( des.isMemory() ) {
    if ( src.isRegister() ) {
 			System.out.println("OR memory and register");

    }
 		else if ( src.isMemory() ) {
 			System.out.println("OR memory and memory");

 		}
    else if ( src.isHex() ) {
  		System.out.println("OR memory and immediate");

    }
 	}
 }
