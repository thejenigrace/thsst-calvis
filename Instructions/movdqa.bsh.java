execute(des, src, registers, memory) {
    Calculator c = new Calculator(registers, memory);
    boolean isRegisterSrc = false;
    boolean isRegisterDes = false;
    int desSize = 0;
    int srcSize = 0;
    String destination = "";
    String source = "";
    String resultingHex = "";
    if(des.isMemory() && src.isMemory()) {
        throw new MemoryToMemoryException("[" + des.getValue() + "]", "[" + src.getValue() + "]");
    }
    if(src.getValue().substring(src.getValue().length()-1).equals("0") && !src.getValue().equals("00000000") && src.isMemory()) {
        throw new MemoryAlignmentException(src.getValue());
    }
    if(des.getValue().substring(des.getValue().length()-1).equals("0") && !des.getValue().equals("00000000") && des.isMemory()) {
        throw new MemoryAlignmentException(des.getValue());
    }
    if(des.isRegister()) {
        desSize = registers.getBitSize(des);
        isRegisterDes = true;

    }
    else{
        desSize = memory.getBitSize(des);
        if(desSize == 0) {
            desSize = registers.getBitSize(src);
        }
    }

    if(src.isRegister()) {
        srcSize = registers.getBitSize(src);
        isRegisterSrc = true;
        source = registers.get(src);
    }
    else{
        source = memory.read(src, desSize);

    }

    if(src.isMemory()) {
        srcSize = desSize;
    }

    if(desSize == srcSize && srcSize == 128) {
        if(isRegisterDes) {
            registers.set(des, source);
        }
        else {
            if(isRegisterDes)
                memory.write(des, source, desSize);
            else
                throw new MemoryToMemoryException("[" + des.getValue() + "]", "[" + src.getValue() + "]");

        }
    }
}
