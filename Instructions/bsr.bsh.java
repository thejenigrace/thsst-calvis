 execute(des, src, registers, memory){
   Calculator calculator = new Calculator(registers, memory);
   EFlags flags = registers.getEFlags();

   if( des.isRegister() && registers.getBitSize(des) == 16 ) {
     if( src.isRegister() && registers.getBitSize(src) == 16 ) {
      System.out.println("BSR r16, r16");

      String source = calculator.hexToBinaryString(registers.get(src), src);
      String destination = calculator.hexToBinaryString(registers.get(des), des);

      BigInteger biSrc = new BigInteger(source, 2);
      BigInteger biDes = new BigInteger(destination, 2);

      if( biSrc.equals(BigInteger.ZERO) ) {
        flags.setZeroFlag("1");

        String result = calculator.binaryToHexString("0", des);
        registers.set(des, result); //des undefined

        flags.setCarryFlag("0"); //udnefined
        flags.setOverflowFlag("0"); //undefined
        flags.setSignFlag("0"); //undefined
        flags.setParityFlag("0"); //undefined
        flags.setAuxiliaryFlag("0"); //undefined
      }
      else {
        flags.setZeroFlag("0");

        for(int i = 0; i < source.length(); i++) {
          if( source.charAt(i) == '1' ) {
            i = 15 - i;
            String index = Integer.toHexString(i);
            index = calculator.hexZeroExtend(index, des);
            registers.set(des, index);

            break;
          }
        }

        flags.setCarryFlag("0"); //udnefined
        flags.setOverflowFlag("0"); //undefined
        flags.setSignFlag("0"); //undefined
        flags.setParityFlag("0"); //undefined
        flags.setAuxiliaryFlag("0"); //undefined
      }
     }
     else if( src.isMemory() && memory.getBitSize(src) == 16 ) {
      System.out.println("BSR r16, m16");

      String source = calculator.hexToBinaryString(memory.read(src, src), src);
      String destination = calculator.hexToBinaryString(registers.get(des), des);

      BigInteger biSrc = new BigInteger(source, 2);
      BigInteger biDes = new BigInteger(destination, 2);

      if( biSrc.equals(BigInteger.ZERO) ) {
        flags.setZeroFlag("1");

        String result = calculator.binaryToHexString("0", des);
        registers.set(des, result); //des undefined

        flags.setCarryFlag("0"); //udnefined
        flags.setOverflowFlag("0"); //undefined
        flags.setSignFlag("0"); //undefined
        flags.setParityFlag("0"); //undefined
        flags.setAuxiliaryFlag("0"); //undefined
      }
      else {
        flags.setZeroFlag("0");

        for(int i = 0; i < source.length(); i++) {
          if( source.charAt(i) == '1' ) {
            i = 15 - i;
            String index = Integer.toHexString(i);
            index = calculator.hexZeroExtend(index, des);
            registers.set(des, index);;

            break;
          }
        }

        flags.setCarryFlag("0"); //udnefined
        flags.setOverflowFlag("0"); //undefined
        flags.setSignFlag("0"); //undefined
        flags.setParityFlag("0"); //undefined
        flags.setAuxiliaryFlag("0"); //undefined
      }
     }
   }
   else if( des.isRegister() && registers.getBitSize(des) == 32 ) {
     if( src.isRegister() && registers.getBitSize(src) == 32 ) {
      System.out.println("BSR r32, r32");

      String source = calculator.hexToBinaryString(registers.get(src), src);
      String destination = calculator.hexToBinaryString(registers.get(des), des);

      BigInteger biSrc = new BigInteger(source, 2);
      BigInteger biDes = new BigInteger(destination, 2);

      if( biSrc.equals(BigInteger.ZERO) ) {
        flags.setZeroFlag("1");

        String result = calculator.binaryToHexString("0", des);
        registers.set(des, result); //des undefined

        flags.setCarryFlag("0"); //udnefined
        flags.setOverflowFlag("0"); //undefined
        flags.setSignFlag("0"); //undefined
        flags.setParityFlag("0"); //undefined
        flags.setAuxiliaryFlag("0"); //undefined
      }
      else {
        flags.setZeroFlag("0");

        for(int i = 0; i < source.length(); i++) {
          if( source.charAt(i) == '1' ) {
            i = 31 - i;
            String index = Integer.toHexString(i);
            index = calculator.hexZeroExtend(index, des);
            registers.set(des, index);

            break;
          }
        }

        flags.setCarryFlag("0"); //udnefined
        flags.setOverflowFlag("0"); //undefined
        flags.setSignFlag("0"); //undefined
        flags.setParityFlag("0"); //undefined
        flags.setAuxiliaryFlag("0"); //undefined
      }
     }
     else if( src.isMemory() && memory.getBitSize(src) == 32 ) {
      System.out.println("BSR r32, m32");

      String source = calculator.hexToBinaryString(memory.read(src, src), src);
      String destination = calculator.hexToBinaryString(registers.get(des), des);

      BigInteger biSrc = new BigInteger(source, 2);
      BigInteger biDes = new BigInteger(destination, 2);

      if( biSrc.equals(BigInteger.ZERO) ) {
        flags.setZeroFlag("1");

        String result = calculator.binaryToHexString("0", des);
        registers.set(des, result); //des undefined

        flags.setCarryFlag("0"); //udnefined
        flags.setOverflowFlag("0"); //undefined
        flags.setSignFlag("0"); //undefined
        flags.setParityFlag("0"); //undefined
        flags.setAuxiliaryFlag("0"); //undefined
      }
      else {
        flags.setZeroFlag("0");

        for(int i = 0; i < source.length(); i++) {
          if( source.charAt(i) == '1' ) {
            i = 31 - i;
            String index = Integer.toHexString(i);
            index = calculator.hexZeroExtend(index, des);
            registers.set(des, index);

            break;
          }
        }

        flags.setCarryFlag("0"); //udnefined
        flags.setOverflowFlag("0"); //undefined
        flags.setSignFlag("0"); //undefined
        flags.setParityFlag("0"); //undefined
        flags.setAuxiliaryFlag("0"); //undefined
      }
     }
   }
 }
