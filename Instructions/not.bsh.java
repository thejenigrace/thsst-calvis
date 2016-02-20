execute(des, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);

    if ( des.isRegister() ) {
        int desSize = registers.getBitSize(des);

        if( checkSizeOfRegister(registers, desSize) ) {
            String destination = calculator.hexToBinaryString(registers.get(des), des);
            registers.set(des, performNot(calculator, des, destination, desSize));
        }
    }
    else if ( des.isMemory() ){
        int desSize = memory.getBitSize(des);

        String destination = calculator.hexToBinaryString(memory.read(des, des), des);
        memory.write(des, performNot(calculator, des, destination, desSize), des);
    }
}

String performNot(calculator, des, destination, desSize) {
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

    return result;
}

boolean checkSizeOfRegister(registers, desSize) {
    boolean checkSize = false;
    for(int a : registers.getAvailableSizes()) {
        if(a == desSize) {
            checkSize = true;
        }
    }

    return checkSize;
}
