execute(src, registers, memory) {
    String st0 = registers.get("ST0");
    String st1 = "";

    if ( src.isMemory() ) {
        int size = memory.getBitSize(src);
        st1 = memory.read(src, size);
        Calculator calculator = new Calculator(registers,memory);
        if ( size == 16 ) {
            // convert 16 bit integer to extended precision
            st1 = calculator.convertHexToDoublePrecision(st1);
        } else if ( size == 32 ) {
            // convert 32 bit integer to extended precision
            st1 = calculator.convertHexToDoublePrecision(st1);
        }
    }

    if ( st0.equals("> than st1") ) {
        registers.x87().status().set("C3", '0');
        registers.x87().status().set("C2", '0');
        registers.x87().status().set("C0", '0');
    } else if ( st0.equals("< than st1") ) {
        registers.x87().status().set("C3", '0');
        registers.x87().status().set("C2", '0');
        registers.x87().status().set("C0", '1');
    } else if ( st0.equals(st1) ) {
        registers.x87().status().set("C3", '1');
        registers.x87().status().set("C2", '0');
        registers.x87().status().set("C0", '0');
    } else if ( st0.equals("NaN or Unsupported") || st1.equals("NaN or Unsupported") ) {
        registers.x87().status().set("C3", '1');
        registers.x87().status().set("C2", '1');
        registers.x87().status().set("C0", '1');
    }

    registers.x87().pop();

}
