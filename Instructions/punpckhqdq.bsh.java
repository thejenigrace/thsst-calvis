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
    else{
        desSize = memory.getBitSize(des);
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

    if(desSize == 128 && srcSize == 128) {
        String parDes = destination.substring(0, 16);
        String parSource = source.substring(0,16);
        for(int x = 0; x < 4; x++) {
            resultingHex += parSource.substring(x * 4, x * 4 + 4);
            resultingHex += parDes.substring(x * 4, x * 4 + 4);
        }

        if(isRegisterDes) {
            registers.set(des, c.binaryZeroExtend(resultingHex, srcSize/4));
        }
    }
}
