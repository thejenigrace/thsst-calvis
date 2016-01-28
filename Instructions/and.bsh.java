 execute(des, src, registers, memory) {
 	Calculator calculator = new EnvironmentConfiguration.model.Calculator(registers, memory);

 	if ( des.isRegister() ) {
 		if ( src.isRegister() ) {
      System.out.println("AND register and register");

      //get size of des, src
      int desSize = registers.getBitSize(des);
      int srcSize = registers.getBitSize(src);

      if ( (desSize == srcSize) && ((desSize == 32) || (desSize == 16) || (desSize == 8)) ) {
        //get hex value of des, src then convert to binary
        String source = calculator.hexToBinaryString(registers.get(src), src);
        String destination = calculator.hexToBinaryString(registers.get(des), des);

        String result = "";

        for (int i = 0; i < desSize; i++) {
          if (source.charAt(i) == '1' && destination.charAt(i) == '1') {
            result = result.concat("1");
          }
          else {
            result = result.concat("0");
          }
        }

        result = calculator.binaryToHexString(result, des);
        registers.set(des, result);

        //FLAGS
        EFlags flags = registers.getEFlags();

        flags.setCarryFlag("0");
        flags.setOverflowFlag("0");

        String d = registers.get(des);
        if(d.equals("00") || d.equals("0000") || d.equals("00000000")) {
          flags.setZeroFlag("1");
        }
        else {
          flags.setZeroFlag("0");
        }

        String sign = "" + calculator.hexToBinaryString(registers.get(des), des).charAt(0);
        System.out.println("result: " + calculator.hexToBinaryString(registers.get(des), des) + "\nsign: " + sign);
        flags.setSignFlag(sign);

        System.out.println("CF: " + flags.getCarryFlag() +
                         "\nOF: " + flags.getOverflowFlag() +
                         "\nZF: " + flags.getZeroFlag() +
                         "\nSF: " + flags.getSignFlag() + "");
       // flags.setAuxiliaryFlag(); undefined
       // flags.setParityFlag();
      }
    }
 		else if ( src.isMemory() ) {
 			System.out.println("AND register and memory");

 		}
    else if ( src.isHex() ) {
      System.out.println("AND register and immediate");

      //get size of des, src
      int desSize = registers.getBitSize(des);
      int srcSize = src.getValue().length();
      // registers.set(src, calculator.hexZeroExtend(src.getValue(), des));
      // int srcSize = registers.getBitSize(src);

      if ( (desSize >= srcSize) && ((desSize == 32) || (desSize == 16) || (desSize == 8)) ) {
        //get hex value of des, src then convert to binary
        String source = calculator.hexToBinaryString(src.getValue(), des);
        String destination = calculator.hexToBinaryString(registers.get(des), des);

        String result = "";

        for (int i = 0; i < desSize; i++) {
          if (source.charAt(i) == '1' && destination.charAt(i) == '1') {
            result = result.concat("1");
          }
          else {
            result = result.concat("0");
          }
        }

        result = calculator.binaryToHexString(result, des);
        registers.set(des, result);
      }

      //FLAGS
      EFlags flags = registers.getEFlags();

      flags.setCarryFlag("0");
      flags.setOverflowFlag("0");

    }
 	}
 	else if ( des.isMemory() ) {
    if ( src.isRegister() ) {
 			System.out.println("AND memory and register");

    }
 		else if ( src.isMemory() ) {
 			System.out.println("AND memory and memory");

 		}
    else if ( src.isHex() ) {
  		System.out.println("AND memory and immediate");

    }
 	}
 }
