execute(des, src, registers, memory) {
  Calculator calculator = new Calculator(registers, memory);

  if( des.isRegister() && registers.getBitSize(des) == 16 ) {
    if( src.isRegister() && registers.getBitSize(src) == 8 ) {
      System.out.println("MOVSX r16, r8");

      String source = calculator.hexToBinaryString(registers.get(src), src);
      String destination = calculator.hexToBinaryString(registers.get(des), des);
      StringBuffer buffer = new StringBuffer(destination);

      for(int i = 0; i < 8; i++) {
        buffer.setCharAt(i, source.charAt(0));
      }

      for(int i = 8; i < 16; i++) {
        buffer.setCharAt(i, source.charAt(8 - i));
      }

      registers.set(des, buffer.toString());
    }
    else if( src.isMemory() && memory.getBitSize(src) == 8 ) {
      System.out.println("MOVSX r16, m8");

      String source = calculator.hexToBinaryString(memory.read(src, src), src);
      String destination = calculator.hexToBinaryString(registers.get(des), des);
      StringBuffer buffer = new StringBuffer(destination);

      for(int i = 0; i < 8; i++) {
        buffer.setCharAt(i, source.charAt(0));
      }

      for(int i = 8; i < 16; i++) {
        buffer.setCharAt(i, source.charAt(8 - i));
      }

      registers.set(des, buffer.toString());
    }
  }
  else if( des.isRegister() && registers.getBitSize(des) == 32 ) {
    if( src.isRegister() && registers.getBitSize(src) == 16 ) {
      System.out.println("MOVSX r32, r16");

      String source = calculator.hexToBinaryString(registers.get(src), src);
      String destination = calculator.hexToBinaryString(registers.get(des), des);
      StringBuffer buffer = new StringBuffer(destination);

      for(int i = 0; i < 16; i++) {
        buffer.setCharAt(i, source.charAt(0));
      }

      for(int i = 16; i < 32; i++) {
        buffer.setCharAt(i, source.charAt(16 - i));
      }

      registers.set(des, buffer.toString());
    }
    else if( src.isRegister() && registers.getBitSize(src) == 8 ) {
      System.out.println("MOVSX r32, r8");

      String source = calculator.hexToBinaryString(registers.get(src), src);
      String destination = calculator.hexToBinaryString(registers.get(des), des);
      StringBuffer buffer = new StringBuffer(destination);

      for(int i = 0; i < 24; i++) {
        buffer.setCharAt(i, source.charAt(0));
      }

      for(int i = 24; i < 32; i++) {
        buffer.setCharAt(i, source.charAt(24 - i));
      }

      registers.set(des, buffer.toString());
    }
    else if( src.isMemory() && memory.getBitSize(src) == 16 ) {
      System.out.println("MOVSX r32, m16");

      String source = calculator.hexToBinaryString(memory.read(src, src), src);
      String destination = calculator.hexToBinaryString(registers.get(des), des);
      StringBuffer buffer = new StringBuffer(destination);

      for(int i = 0; i < 16; i++) {
        buffer.setCharAt(i, source.charAt(0));
      }

      for(int i = 16; i < 32; i++) {
        buffer.setCharAt(i, source.charAt(16 - i));
      }

      registers.set(des, buffer.toString());
    }
  }
}
