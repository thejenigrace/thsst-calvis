execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);

    if( des.isRegister() && registers.getBitSize(des) == 16 ) {
        if( src.isRegister() && registers.getBitSize(src) == 8 ) {
            String source = calculator.hexToBinaryString(registers.get(src), src);
            String destination = calculator.hexToBinaryString(registers.get(des), des);
            StringBuffer buffer = new StringBuffer(destination);

            registers.set(des, calculator.binaryToHexString(zeroExtend16(buffer, source), des));
        }
        else if( src.isMemory() && memory.getBitSize(src) == 8 ) {
            String source = calculator.hexToBinaryString(memory.read(src, src), src);
            String destination = calculator.hexToBinaryString(registers.get(des), des);
            StringBuffer buffer = new StringBuffer(destination);

            registers.set(des, calculator.binaryToHexString(zeroExtend16(buffer, source), des));
        }
    }
    else if( des.isRegister() && registers.getBitSize(des) == 32 ) {
        if( src.isRegister() && registers.getBitSize(src) == 16 ) {
            String source = calculator.hexToBinaryString(registers.get(src), src);
            String destination = calculator.hexToBinaryString(registers.get(des), des);
            StringBuffer buffer = new StringBuffer(destination);

            registers.set(des, calculator.binaryToHexString(zeroExtend32(buffer, source, 16), des));
        }
        else if( src.isRegister() && registers.getBitSize(src) == 8 ) {
            String source = calculator.hexToBinaryString(registers.get(src), src);
            String destination = calculator.hexToBinaryString(registers.get(des), des);
            StringBuffer buffer = new StringBuffer(destination);

            registers.set(des, calculator.binaryToHexString(zeroExtend32(buffer, source, 8), des));
        }
        else if( src.isMemory() && memory.getBitSize(src) == 16 ) {
            String source = calculator.hexToBinaryString(memory.read(src, src), src);
            String destination = calculator.hexToBinaryString(registers.get(des), des);
            StringBuffer buffer = new StringBuffer(destination);

            registers.set(des, calculator.binaryToHexString(zeroExtend32(buffer, source, 16), des));
        }
        else if( src.isMemory() && memory.getBitSize(src) == 8 ) {
            String source = calculator.hexToBinaryString(memory.read(src, src), src);
            String destination = calculator.hexToBinaryString(registers.get(des), des);
            StringBuffer buffer = new StringBuffer(destination);

            registers.set(des, calculator.binaryToHexString(zeroExtend32(buffer, source, 8), des));
        }
    }
}

String zeroExtend16(buffer, source) {
    for(int i = 0; i < 8; i++) {
        buffer.setCharAt(i, '0');
    }

    for(int i = 8; i < 16; i++) {
        buffer.setCharAt(i, source.charAt(i - 8));
    }

    return buffer.toString();
}

String zeroExtend32(buffer, source, num) {
    if(num == 16) {
        for(int i = 0; i < 16; i++) {
            buffer.setCharAt(i, '0');
        }

        for(int i = 16; i < 32; i++) {
            buffer.setCharAt(i, source.charAt(i - 16));
        }
    }
    else if (num == 8) {
        for(int i = 0; i < 24; i++) {
            buffer.setCharAt(i, '0');
        }

        for(int i = 24; i < 32; i++) {
            buffer.setCharAt(i, source.charAt(i - 24));
        }
    }

    return buffer.toString();
}
