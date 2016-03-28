execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);

    int desSize = 0;
    int srcSize = 0;

    if( des.isRegister() ) {
		desSize = registers.getBitSize(des);
	}

    if( src.isRegister() ) {
		srcSize = registers.getBitSize(src);
	} else {
		srcSize = memory.getBitSize(src);
	}

    if( des.isRegister() ) {
        if( src.isRegister() ) {
            if( (desSize == srcSize) && checkSizeOfRegister(registers, desSize) ) {
                String source = registers.get(src);
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            } else {
                //throw exception
            }
        } else if( src.isMemory() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                String source = memory.read(src, desSize);
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            } else {
                //throw exception
            }
        }
    } else {
        //throw exception
    }
}

storeResultToRegister(registers, calculator, des, source, destination, desSize) {
    registers.set("XMM0", "112233445566778899AABBCCDDEEFF00");
    registers.set("XMM1", "112233445566778899AABBCCDDEEFF00");
    destination = registers.get("XMM1");

    String result = "";
    int limit = 0;
    int start = 0;

    if( desSize == 64 ) {
        start = 8;
        limit = 16;
    } else if( desSize == 128 ) {
        start = 16;
        limit = 32;
    }

    for(int i = start; i < limit; i += 8) {
        for(int j = 0; j < 8; j++) {
            result += source.charAt(i + j) + "";
        }

        for(int j = 0; j < 8; j++) {
            result += destination.charAt(i + j) + "";
        }
    }

    registers.set(des, result);
}

boolean checkSizeOfRegister(registers, desSize) {
    boolean checkSize = false;

    if( 128 == desSize || 64 == desSize ) {
        checkSize = true;
    }

    return checkSize;
}
