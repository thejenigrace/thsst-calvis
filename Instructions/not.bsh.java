 execute(des, registers, memory) {
 	Calculator calculator = new Calculator(registers, memory);

 	if ( des.isRegister() ) {
 		System.out.println("NOT register");

    //get size of des
    int desSize = registers.getBitSize(des);

    boolean checkSize = false;
    for(int a : registers.getAvailableSizes()) {
      if(a == desSize) {
        checkSize = true;
      }
    }

    if( checkSize ) {
      String destination = calculator.hexToBinaryString(registers.get(des), des);

      String result = "";

      for (int i = 0; i < desSize; i++) {
        if (destination.charAt(i) == '1') {
          result = result.concat("0");
        }
        else if (destination.charAt(i) == '0') {
          result = result.concat("1");
        }
      }

      result = calculator.binaryToHexString(result, des);
      registers.set(des, result);
    }
 	}
 	else if ( des.isMemory() ){
    System.out.println("NOT memory");

    int desSize = memory.getBitSize(des);

    String destination = calculator.hexToBinaryString(memory.read(des, des), des);

    String result = "";

    for (int i = 0; i < desSize; i++) {
      if (destination.charAt(i) == '1') {
        result = result.concat("0");
      }
      else if (destination.charAt(i) == '0') {
        result = result.concat("1");
      }
    }

    result = calculator.binaryToHexString(result, des);
    memory.write(des, result, des);
 	}
 }
