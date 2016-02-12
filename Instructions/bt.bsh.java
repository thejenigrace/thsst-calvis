 execute(des, src, registers, memory){
   Calculator calculator = new Calculator(registers, memory);

   if( des.isRegister() && registers.getBitSize(des) == 16 ) {
     if( src.isRegister() && registers.getBitSize(src) == 16 ) {
      System.out.println("BT r16, r16");

      String d = calculator.hexToBinaryString(registers.get(des), des);
      String destination = new StringBuffer(d).reverse().toString();

      String s = calculator.hexToBinaryString(registers.get(src), src);
      BigInteger source = new BigInteger(s, 2);

      EFlags flags = registers.getEFlags();
   	  flags.setCarryFlag(destination.charAt(source.intValue()) + "");
      flags.setOverflowFlag("0"); //undefined
      flags.setSignFlag("0"); //undefined
      flags.setParityFlag("0"); //undefined
      flags.setAuxiliaryFlag("0"); //undefined
      //Zero flag no change
     }
     else if( src.isHex() && src.getValue().length() <= 2 ) {
       BigInteger l = new BigInteger(src.getValue(), 16);
       int limit = l.intValue();

       if( limit >= 0 && limit <= 15 ) {
        System.out.println("BT r16, i8");

        String d = calculator.hexToBinaryString(registers.get(des), des);
        String destination = new StringBuffer(d).reverse().toString();

        String s = calculator.hexToBinaryString(src.getValue(), src);
        BigInteger source = new BigInteger(s, 2);

        EFlags flags = registers.getEFlags();
     	  flags.setCarryFlag(destination.charAt(source.intValue()) + "");
        flags.setOverflowFlag("0"); //undefined
        flags.setSignFlag("0"); //undefined
        flags.setParityFlag("0"); //undefined
        flags.setAuxiliaryFlag("0"); //undefined
        //Zero flag no change
       }
     }
   }
   else if( des.isRegister() && registers.getBitSize(des) == 32 ) {
     if( src.isRegister() && registers.getBitSize(src) == 32 ) {
      System.out.println("BT r32, r32");

      String d = calculator.hexToBinaryString(registers.get(des), des);
      String destination = new StringBuffer(d).reverse().toString();

      String s = calculator.hexToBinaryString(registers.get(src), src);
      BigInteger source = new BigInteger(s, 2);

      EFlags flags = registers.getEFlags();
   	  flags.setCarryFlag(destination.charAt(source.intValue()) + "");
      flags.setOverflowFlag("0"); //undefined
      flags.setSignFlag("0"); //undefined
      flags.setParityFlag("0"); //undefined
      flags.setAuxiliaryFlag("0"); //undefined
      //Zero flag no change
     }
     else if( src.isHex() && src.getValue().length() <= 2 ) {
      BigInteger l = new BigInteger(src.getValue(), 16);
      int limit = l.intValue();

      if( limit >= 0 && limit <= 31 ) {
       System.out.println("BT r32, i8");

       String d = calculator.hexToBinaryString(registers.get(des), des);
       String destination = new StringBuffer(d).reverse().toString();

       String s = calculator.hexToBinaryString(src.getValue(), src);
       BigInteger source = new BigInteger(s, 2);

       EFlags flags = registers.getEFlags();
       flags.setCarryFlag(destination.charAt(source.intValue()) + "");
       flags.setOverflowFlag("0"); //undefined
       flags.setSignFlag("0"); //undefined
       flags.setParityFlag("0"); //undefined
       flags.setAuxiliaryFlag("0"); //undefined
       //Zero flag no change
      }
     }
   }
   else if( des.isMemory() && memory.getBitSize(des) == 16 ) {
     if( src.isRegister() && registers.getBitSize(src) == 16 ) {
      System.out.println("BT m16, r16");

      String d = calculator.hexToBinaryString(memory.read(des, des), des);
      String destination = new StringBuffer(d).reverse().toString();

      String s = calculator.hexToBinaryString(registers.get(src), src);
      BigInteger source = new BigInteger(s, 2);

      EFlags flags = registers.getEFlags();
   	  flags.setCarryFlag(destination.charAt(source.intValue()) + "");
      flags.setOverflowFlag("0"); //undefined
      flags.setSignFlag("0"); //undefined
      flags.setParityFlag("0"); //undefined
      flags.setAuxiliaryFlag("0"); //undefined
      //Zero flag no change
     }
     else if( src.isHex() && src.getValue().length() <= 2 ) {
       BigInteger l = new BigInteger(src.getValue(), 16);
       int limit = l.intValue();

       if( limit >= 0 && limit <= 16 ) {
        System.out.println("BT m16, i8");

        String d = calculator.hexToBinaryString(memory.read(des, des), des);
        String destination = new StringBuffer(d).reverse().toString();

        String s = calculator.hexToBinaryString(src.getValue(), src);
        BigInteger source = new BigInteger(s, 2);

        EFlags flags = registers.getEFlags();
        flags.setCarryFlag(destination.charAt(source.intValue()) + "");
        flags.setOverflowFlag("0"); //undefined
        flags.setSignFlag("0"); //undefined
        flags.setParityFlag("0"); //undefined
        flags.setAuxiliaryFlag("0"); //undefined
        //Zero flag no change
       }
     }
   }
   else if( des.isMemory() && memory.getBitSize(des) == 32 ) {
     if( src.isRegister() && registers.getBitSize(src) == 32 ) {
      System.out.println("BT m32, r32");

      String d = calculator.hexToBinaryString(memory.read(des, des), des);
      String destination = new StringBuffer(d).reverse().toString();

      String s = calculator.hexToBinaryString(registers.get(src), src);
      BigInteger source = new BigInteger(s, 2);

      EFlags flags = registers.getEFlags();
   	  flags.setCarryFlag(destination.charAt(source.intValue()) + "");
      flags.setOverflowFlag("0"); //undefined
      flags.setSignFlag("0"); //undefined
      flags.setParityFlag("0"); //undefined
      flags.setAuxiliaryFlag("0"); //undefined
      //Zero flag no change
     }
     else if( src.isHex() && src.getValue().length() <= 2 ) {
       BigInteger l = new BigInteger(src.getValue(), 16);
       int limit = l.intValue();

       if( limit >= 0 && limit <= 31 ) {
        System.out.println("BT m32, i8");

        String d = calculator.hexToBinaryString(memory.read(des, des), des);
        String destination = new StringBuffer(d).reverse().toString();

        String s = calculator.hexToBinaryString(src.getValue(), src);
        BigInteger source = new BigInteger(s, 2);

        EFlags flags = registers.getEFlags();
        flags.setCarryFlag(destination.charAt(source.intValue()) + "");
        flags.setOverflowFlag("0"); //undefined
        flags.setSignFlag("0"); //undefined
        flags.setParityFlag("0"); //undefined
        flags.setAuxiliaryFlag("0"); //undefined
        //Zero flag no change
       }
     }
   }
 }
