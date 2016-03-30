execute(src, registers, memory) {
    String st0 = registers.get("ST0");
    String st1 = "";

    if ( src.isRegister() && registers.getBitSize(src) == 80 ) {
        st1 = registers.get(src);
    }
    
    if ( st0.equals("> than st1") ) {
        registers.getEFlags().setZeroFlag("0");
        registers.getEFlags().setParityFlag("0");
        registers.getEFlags().setCarryFlag("0");  
    } else if ( st0.equals("< than st1") ) {
        registers.getEFlags().setZeroFlag("0");
        registers.getEFlags().setParityFlag("0");
        registers.getEFlags().setCarryFlag("1"); 
    } else if ( st0.equals(st1) ) {
        registers.getEFlags().setZeroFlag("1");
        registers.getEFlags().setParityFlag("0");
        registers.getEFlags().setCarryFlag("0");  
    } else if ( st0.equals("NaN or Unsupported") || st1.equals("NaN or Unsupported") ) {
        registers.getEFlags().setZeroFlag("1");
        registers.getEFlags().setParityFlag("1");
        registers.getEFlags().setCarryFlag("1"); 
    }

    registers.x87().status().set("C1", '0');
    
    registers.getEFlags().setOverflowFlag("0");
    registers.getEFlags().setSignFlag("0");
    registers.getEFlags().setAuxiliaryFlag("0");  

    registers.x87().pop();
    
}
