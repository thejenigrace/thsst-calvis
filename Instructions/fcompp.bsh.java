execute(registers, memory) {
    String st0 = registers.get("ST0");
    String st1 = registers.get("ST1");
    
    if ( st0.equals("> than st1") ) {
        registers.x87().set("C3", '0');
        registers.x87().set("C2", '0');
        registers.x87().set("C0", '0');    
    } else if ( st0.equals("< than st1") ) {
        registers.x87().set("C3", '0');
        registers.x87().set("C2", '0');
        registers.x87().set("C0", '1');  
    } else if ( st0.equals(st1) ) {
        registers.x87().set("C3", '1');
        registers.x87().set("C2", '0');
        registers.x87().set("C0", '0');  
    } else if ( st0.equals("NaN or Unsupported") || st1.equals("NaN or Unsupported") ) {
        registers.x87().set("C3", '1');
        registers.x87().set("C2", '1');
        registers.x87().set("C0", '1');  
    }

    registers.x87().pop();    
    registers.x87().pop();
}