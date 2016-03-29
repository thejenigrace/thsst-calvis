execute(des, src, registers, memory) {
    int DWORD = 32;
    int QWORD = 64;
    int DQWORD = 128;

    Calculator calculator = new Calculator(registers, memory);

    int desSize = 0;
    int srcSize = 0;

    if( des.isRegister() ) {
		desSize = registers.getBitSize(des);
	}
	if( src.isRegister() ) {
		srcSize = registers.getBitSize(src);
	}

    if( des.isRegister() && checkSizeOfDestination(registers, desSize) ) {
        if( src.isRegister() ) {
            if( checkSizeOfSource(registers, srcSize) ) {
                String srcValue = registers.get(src);
                String desValue = registers.get(des);
                storeResultToRegister(registers, calculator, des, srcValue, desValue, desSize);
            }
            else {
                //throw exception
            }
        }
        else if( src.isMemory() ) {
            String srcValue = memory.read(src, DWORD);
            String desValue = registers.get(des);
            storeResultToRegister(registers, calculator, des, srcValue, desValue, desSize);
        }
    }
    else {
        //throw exception
    }
}

storeResultToRegister(registers, calculator, des, srcValue, desValue, desSize) {
    String unmodified = desValue.substring(0,16);
    String value = "";
    if ( srcValue.length() == 8 ) {
        value = calculator.convertSPToDP(srcValue);
    } else {
        value = calculator.convertSPToDP(srcValue.substring(24));
    }

    // value = calculator.hexZeroExtend(value,QWORD/4);

    registers.set(des, unmodified + value);
}

boolean checkSizeOfDestination(registers, desSize) {
    boolean checkSize = false;

    if( 128 == desSize ) {
        checkSize = true;
    }

    return checkSize;
}

boolean checkSizeOfSource(registers, srcSize) {
    boolean checkSize = false;

    if( 32 == srcSize || 128 == srcSize ) {
        checkSize = true;
    }

    return checkSize;
}
