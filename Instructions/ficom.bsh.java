execute(src, registers, memory) {
    String st0 = registers.get("ST0");
    String st1 = "";

    Double d1 = Double.parseDouble(st0);
    Double d2;

    if ( src.isMemory() ) {
        int size = memory.getBitSize(src);
        st1 = memory.read(src, size);
        Converter converter = new Converter(st1);
        if ( size == 16 ) {
            // convert 16 bit integer to extended precision
            st1 = converter.to16BitSignedInteger() + "";
        } else if ( size == 32 ) {
            // convert 32 bit integer to extended precision
            st1 = converter.to32BitSignedInteger() + "";
        }
        d2 = Double.parseDouble(st1);
    }
    comparison(registers, d1, d2);
}

comparison(registers, double1, double2) {
  if ( double1.isNaN() || double2.isNaN() ) {
      registers.x87().status().set("C3", '1');
      registers.x87().status().set("C2", '1');
      registers.x87().status().set("C0", '1');
      registers.x87().status().setInvalidOperationFlag();
  } else if ( double1.compareTo(double2) > 0 ) {
      registers.x87().status().set("C3", '0');
      registers.x87().status().set("C2", '0');
      registers.x87().status().set("C0", '0');
  } else if ( double1.compareTo(double2) < 0 ) {
      registers.x87().status().set("C3", '0');
      registers.x87().status().set("C2", '0');
      registers.x87().status().set("C0", '1');
  } else if ( double1.compareTo(double2) == 0 ) {
      registers.x87().status().set("C3", '1');
      registers.x87().status().set("C2", '0');
      registers.x87().status().set("C0", '0');
  }
  registers.x87().status().set("C1", '0');
}
