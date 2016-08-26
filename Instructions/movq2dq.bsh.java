execute(des, src, registers, memory) {
    Calculator c = new Calculator(registers, memory);
    boolean isRegisterSrc = false;
    boolean isRegisterDes = false;
    int desSize = 0;
    int srcSize = 0;
    String destination = "";
    String source = "";
    String resultingHex = "";

    if(des.isRegister()) {
        desSize = registers.getBitSize(des);
        isRegisterDes = true;
        destination = registers.get(des);
    }

    if(src.isRegister()) {
        srcSize = registers.getBitSize(src);
        isRegisterSrc = true;
        source = registers.get(src);
    }

    if(src.isMemory()) {
        srcSize = desSize;
        source = memory.read(src, srcSize);
    }

    if(registers.getBitSize(des) == 128 && registers.getBitSize(src) == 64) {
        if(isRegisterDes) {
            registers.set(des, c.binaryZeroExtend(source, desSize/4));
        }
    }



}
