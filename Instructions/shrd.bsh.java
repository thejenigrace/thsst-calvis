 execute(des, src, count, registers, memory) {
 	Calculator calculator = new Calculator(registers, memory);

		 System.out.println("SHRD");
 	if ( des.isRegister() && src.isRegister() ) {
    //get size of des, src
    int desSize = registers.getBitSize(des);
    int srcSize = registers.getBitSize(src);

    if( (desSize == srcSize == 16) && count.getValue().equals("CL") ) {
      
    }
    else if ( (desSize == srcSize == 32) && count.getValue().length() <= 2 ) {

    }
 	}
  else if ( des.isMemory() && src.isMemory() ) {
    //get size of des, src
    int desSize = registers.getBitSize(des);
    int srcSize = registers.getBitSize(src);

    if( (desSize == srcSize == 16) && count.getValue().equals("CL") ) {

    }
    else if ( (desSize == srcSize == 32) && count.getValue().length() <= 2 ) {

    }
 	}
 }
