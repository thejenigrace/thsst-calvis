 execute(des, src, registers, memory) {
 	Calculator calculator = new Calculator(registers, memory);

 	if ( des.isRegister() ) {
    if( src.isRegister() && src.getValue().equals("CL") ) {
      System.out.println("SAR register and CL");

      //get size of des
      int desSize = registers.getBitSize(des);
      String originalDes = calculator.hexToBinaryString(registers.get(des), des);
      String originalSign = originalDes.charAt(0) + "";

      boolean checkSize = false;
      for(int a : registers.getAvailableSizes()) {
        if(a == desSize) {
          checkSize = true;
        }
      }

      BigInteger count = new BigInteger(registers.get(src), 16);
      int limit = count.intValue();
      if( checkSize && (limit >= 0 && limit <= 31) ) {
        String destination = calculator.hexToBinaryString(registers.get(des), des);

        BigInteger biDes = new BigInteger(destination, 2);
        BigInteger biResult = biDes.shiftRight(count.intValue());

        StringBuffer buffer = new StringBuffer(biResult.toString(2));
        StringBuffer bufferResult = new StringBuffer(calculator.binaryZeroExtend(buffer.toString(), des));
        bufferResult.setCharAt(0, destination.charAt(0));

        String result = calculator.binaryToHexString(bufferResult.toString(), des);
        registers.set(des, result.toString());

        String r = calculator.hexToBinaryString(registers.get(des), des);
        BigInteger biR = new BigInteger(r, 2);

        //FLAGS
        EFlags flags = registers.getEFlags();
        if (limit == 0) {
          //flags not affected
        }
        else {
          if(biR.equals(BigInteger.ZERO)) {
            flags.setZeroFlag("1");
          }
          else {
            flags.setZeroFlag("0");
          }

          String sign = "" + r.charAt(0);
          flags.setSignFlag(sign);

          String parity = calculator.checkParity(r);
          flags.setParityFlag(parity);

          if(limit == 1 && originalSign.equals(sign)) {
            flags.setOverflowFlag("0");
          }
          else if(limit == 1 && !originalSign.equals(sign)) {
            flags.setOverflowFlag("1");
          }
          else {
            flags.setOverflowFlag("0"); //undefined
          }

          if(limit <= desSize) {
            flags.setCarryFlag(originalDes.charAt(desSize - limit).toString());
          }
          else {
            flags.setCarryFlag("0");
          }

          flags.setAuxiliaryFlag("0"); //undefined
        }
      }
    }
    else if ( src.isHex() && src.getValue().length() <= 2){
      System.out.println("SAR register and i8");

      //get size of des
      int desSize = registers.getBitSize(des);
      String originalDes = calculator.hexToBinaryString(registers.get(des), des);
      String originalSign = originalDes.charAt(0) + "";

      boolean checkSize = false;
      for(int a : registers.getAvailableSizes()) {
        if(a == desSize) {
          checkSize = true;
        }
      }

      BigInteger count = new BigInteger(src.getValue(), 16);
      int limit = count.intValue();
      if( checkSize && (limit >= 0 && limit <= 31) ) {
        String destination = calculator.hexToBinaryString(registers.get(des), des);

        BigInteger biDes = new BigInteger(destination, 2);
        BigInteger biResult = biDes.shiftRight(count.intValue());

        StringBuffer buffer = new StringBuffer(biResult.toString(2));
        StringBuffer bufferResult = new StringBuffer(calculator.binaryZeroExtend(buffer.toString(), des));
        bufferResult.setCharAt(0, destination.charAt(0));

        String result = calculator.binaryToHexString(bufferResult.toString(), des);
        registers.set(des, result.toString());

        String r = calculator.hexToBinaryString(registers.get(des), des);
        BigInteger biR = new BigInteger(r, 2);

        //FLAGS
        EFlags flags = registers.getEFlags();
        if (limit == 0) {
          //flags not affected
        }
        else {
          if(biR.equals(BigInteger.ZERO)) {
            flags.setZeroFlag("1");
          }
          else {
            flags.setZeroFlag("0");
          }

          String sign = "" + r.charAt(0);
          flags.setSignFlag(sign);

          String parity = calculator.checkParity(r);
          flags.setParityFlag(parity);

          if(limit == 1 && originalSign.equals(sign)) {
            flags.setOverflowFlag("0");
          }
          else if(limit == 1 && !originalSign.equals(sign)) {
            flags.setOverflowFlag("1");
          }
          else {
            flags.setOverflowFlag("0"); //undefined
          }

          if(limit <= desSize) {
            flags.setCarryFlag(originalDes.charAt(desSize - limit).toString());
          }
          else {
            flags.setCarryFlag("0");
          }

          flags.setAuxiliaryFlag("0"); //undefined
        }
      }
    }
 	}
  else if ( des.isMemory() ){
    if( src.isRegister() && src.getValue().equals("CL") ) {
      System.out.println("SAR memory and CL");

      //get size of des
      int desSize = memory.getBitSize(des);
      String originalDes = calculator.hexToBinaryString(memory.read(des, des), des);
      String originalSign = originalDes.charAt(0) + "";

      BigInteger count = new BigInteger(registers.get(src), 16);
      int limit = count.intValue();

      if(limit >= 0 && limit <= 31) {
        String destination = calculator.hexToBinaryString(memory.read(des, des), des);

        BigInteger biDes = new BigInteger(destination, 2);
        BigInteger biResult = biDes.shiftRight(count.intValue());

        StringBuffer buffer = new StringBuffer(biResult.toString(2));
        StringBuffer bufferResult = new StringBuffer(calculator.binaryZeroExtend(buffer.toString(), des));
        bufferResult.setCharAt(0, destination.charAt(0));

        String result = calculator.binaryToHexString(bufferResult.toString(), des);
        memory.write(des, result, des);

        String r = calculator.hexToBinaryString(memory.read(des, des), des);
        BigInteger biR = new BigInteger(r, 2);

        //FLAGS
        EFlags flags = registers.getEFlags();
        if (limit == 0) {
          //flags not affected
        }
        else {
          if(biR.equals(BigInteger.ZERO)) {
            flags.setZeroFlag("1");
          }
          else {
            flags.setZeroFlag("0");
          }

          String sign = "" + r.charAt(0);
          flags.setSignFlag(sign);

          String parity = calculator.checkParity(r);
          flags.setParityFlag(parity);

          if(limit == 1 && originalSign.equals(sign)) {
            flags.setOverflowFlag("0");
          }
          else if(limit == 1 && !originalSign.equals(sign)) {
            flags.setOverflowFlag("1");
          }
          else {
            flags.setOverflowFlag("0"); //undefined
          }

          if(limit <= desSize) {
            flags.setCarryFlag(originalDes.charAt(desSize - limit).toString());
          }
          else {
            flags.setCarryFlag("0");
          }

          flags.setAuxiliaryFlag("0"); //undefined
        }
      }
    }
    else if ( src.isHex() && src.getValue().length() <= 2){
      System.out.println("SAR memory and i8");

      //get size of des
      int desSize = memory.getBitSize(des);
      String originalDes = calculator.hexToBinaryString(memory.read(des, des), des);
      String originalSign = originalDes.charAt(0) + "";

      BigInteger count = new BigInteger(src.getValue(), 16);
      int limit = count.intValue();

      if(limit >= 0 && limit <= 31) {
        String destination = calculator.hexToBinaryString(memory.read(des, des), des);

        BigInteger biDes = new BigInteger(destination, 2);
        BigInteger biResult = biDes.shiftRight(count.intValue());

        StringBuffer buffer = new StringBuffer(biResult.toString(2));
        StringBuffer bufferResult = new StringBuffer(calculator.binaryZeroExtend(buffer.toString(), des));
        bufferResult.setCharAt(0, destination.charAt(0));

        String result = calculator.binaryToHexString(bufferResult.toString(), des);
        memory.write(des, result, des);

        String r = calculator.hexToBinaryString(memory.read(des, des), des);
        BigInteger biR = new BigInteger(r, 2);

        //FLAGS
        EFlags flags = registers.getEFlags();
        if (limit == 0) {
          //flags not affected
        }
        else {
          if(biR.equals(BigInteger.ZERO)) {
            flags.setZeroFlag("1");
          }
          else {
            flags.setZeroFlag("0");
          }

          String sign = "" + r.charAt(0);
          flags.setSignFlag(sign);

          String parity = calculator.checkParity(r);
          flags.setParityFlag(parity);

          if(limit == 1 && originalSign.equals(sign)) {
            flags.setOverflowFlag("0");
          }
          else if(limit == 1 && !originalSign.equals(sign)) {
            flags.setOverflowFlag("1");
          }
          else {
            flags.setOverflowFlag("0"); //undefined
          }

          if(limit <= desSize) {
            flags.setCarryFlag(originalDes.charAt(desSize - limit).toString());
          }
          else {
            flags.setCarryFlag("0");
          }

          flags.setAuxiliaryFlag("0"); //undefined
        }
      }
    }
 	}
 }
