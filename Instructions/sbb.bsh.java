execute(des, src, registers, memory) {
  Calculator calculator = new Calculator(registers, memory);
  EFlags flags = registers.getEFlags();

  if ( des.isRegister() ) {
    if ( src.isRegister() ) {
      System.out.println("SBB register register");

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

      if( (desSize == srcSize) && checkSize) {
        //get hex value of des, src then convert to binary
        String source = calculator.hexToBinaryString(registers.get(src), src);
        String destination = calculator.hexToBinaryString(registers.get(des), des);

        BigInteger biSrc = new BigInteger(source, 2);
        BigInteger biCarry = new BigInteger(flags.getCarryFlag(), 2);
        BigInteger biResult = biSrc.add(biCarry);
        source = calculator.binaryZeroExtend(biResult.toString(2), src);

        String result = "";
        int r = 0;
        int borrow = 0;
        int carry = 0;
        int overflow = 0;

        for(int i = desSize - 1; i >= 0; i--) {
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
        registers.set(des, calculator.binaryToHexString(d, des));

        //FLAGS
        String res = calculator.hexToBinaryString(registers.get(des), des);
        BigInteger biR = new BigInteger(res, 2);

        flags.setCarryFlag(carry.toString());

        if(overflow == 1) {
          flags.setOverflowFlag("1");
        }
        else {
          flags.setOverflowFlag("0");
        }

        if(biR.equals(BigInteger.ZERO)) {
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
    }
    else if ( src.isMemory() ){
      System.out.println("SBB register memory");

      //get size of des, src
      int desSize = registers.getBitSize(des);

      //get hex value of des, src then convert to binary
      String source = calculator.hexToBinaryString(memory.read(src, desSize), des);
      String destination = calculator.hexToBinaryString(registers.get(des), des);

      BigInteger biSrc = new BigInteger(source, 2);
      BigInteger biCarry = new BigInteger(flags.getCarryFlag(), 2);
      BigInteger biResult = biSrc.add(biCarry);
      source = calculator.binaryZeroExtend(biResult.toString(2), des);

      String result = "";
      int r = 0;
      int borrow = 0;
      int carry = 0;
      int overflow = 0;

      for(int i = desSize - 1; i >= 0; i--) {
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
      registers.set(des, calculator.binaryToHexString(d, des));

      //FLAGS
      String res = calculator.hexToBinaryString(registers.get(des), des);
      BigInteger biR = new BigInteger(res, 2);

      flags.setCarryFlag(carry.toString());

      if(overflow == 1) {
        flags.setOverflowFlag("1");
      }
      else {
        flags.setOverflowFlag("0");
      }

      if(biR.equals(BigInteger.ZERO)) {
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
    else if ( src.isHex() ) {
      System.out.println("SBB register immediate");

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

      if( (desSize >= srcSize) && checkSize) {
        //get hex value of des, src then convert to binary
        String source = calculator.hexToBinaryString(src.getValue(), des);
        String destination = calculator.hexToBinaryString(registers.get(des), des);

        BigInteger biSrc = new BigInteger(source, 2);
        BigInteger biCarry = new BigInteger(flags.getCarryFlag(), 2);
        BigInteger biResult = biSrc.add(biCarry);
        source = calculator.binaryZeroExtend(biResult.toString(2), des);

        String result = "";
        int r = 0;
        int borrow = 0;
        int carry = 0;
        int overflow = 0;

        for(int i = desSize - 1; i >= 0; i--) {
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
        registers.set(des, calculator.binaryToHexString(d, des));

        //FLAGS
        String res = calculator.hexToBinaryString(registers.get(des), des);
        BigInteger biR = new BigInteger(res, 2);

        flags.setCarryFlag(carry.toString());

        if(overflow == 1) {
          flags.setOverflowFlag("1");
        }
        else {
          flags.setOverflowFlag("0");
        }

        if(biR.equals(BigInteger.ZERO)) {
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
    }
  }
  else if ( des.isMemory() ) {
    if ( src.isRegister() ){
      System.out.println("SBB memory register");

      //get size of des, src
      int srcSize = registers.getBitSize(src);

      //get hex value of des, src then convert to binary
      String source = calculator.hexToBinaryString(registers.get(src), src);
      String destination = calculator.hexToBinaryString(memory.read(des, srcSize), src);

      BigInteger biSrc = new BigInteger(source, 2);
      BigInteger biCarry = new BigInteger(flags.getCarryFlag(), 2);
      BigInteger biResult = biSrc.add(biCarry);
      source = calculator.binaryZeroExtend(biResult.toString(2), src);

      String result = "";
      int r = 0;
      int borrow = 0;
      int carry = 0;
      int overflow = 0;

      for(int i = srcSize - 1; i >= 0; i--) {
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
      memory.write(des, calculator.binaryToHexString(d, src), srcSize);

      //FLAGS
      String res = calculator.hexToBinaryString(memory.read(des, srcSize), src);
      BigInteger biR = new BigInteger(res, 2);

      flags.setCarryFlag(carry.toString());

      if(overflow == 1) {
        flags.setOverflowFlag("1");
      }
      else {
        flags.setOverflowFlag("0");
      }

      if(biR.equals(BigInteger.ZERO)) {
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
    else if ( src.isHex() ){
      System.out.println("SBB memory immediate");

      //get size of des, src
      int desSize = memory.getBitSize(des);

      //get hex value of des, src then convert to binary
      String source = calculator.hexToBinaryString(src.getValue(), des);
      String destination = calculator.hexToBinaryString(memory.read(des, des), des);

      BigInteger biSrc = new BigInteger(source, 2);
      BigInteger biCarry = new BigInteger(flags.getCarryFlag(), 2);
      BigInteger biResult = biSrc.add(biCarry);
      source = calculator.binaryZeroExtend(biResult.toString(2), des);

      String result = "";
      int r = 0;
      int borrow = 0;
      int carry = 0;
      int overflow = 0;

      for(int i = desSize - 1; i >= 0; i--) {
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
      memory.write(des, calculator.binaryToHexString(d, des), des);

      //FLAGS
      String res = calculator.hexToBinaryString(memory.read(des, des), des);
      BigInteger biR = new BigInteger(res, 2);

      flags.setCarryFlag(carry.toString());

      if(overflow == 1) {
        flags.setOverflowFlag("1");
      }
      else {
        flags.setOverflowFlag("0");
      }

      if(biR.equals(BigInteger.ZERO)) {
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
  }
}
