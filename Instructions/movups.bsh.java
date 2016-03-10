execute(des,src,registers,memory) {
    int desBitSize;
    int desHexSize;
    String srcValue;

    if(des.isRegister()) {
        desBitSize = registers.getBitSize(des);
        desHexSize = registers.getHexSize(des);
    } else if(des.isMemory()) {
        desBitSize = memory.getBitSize(des);
        desHexSize = memory.getHexSize(des);
    }

    if(src.isRegister()) {
        srcValue = registers.get(src);
    } else if(src.isMemory()) {
        srcValue = memory.read(src,desBitSize);
    }

    if(des.isRegister()) {
        System.out.println("srcValue = " + srcValue);
        registers.set(des, srcValue);
    } else if(des.isMemory()) {
        System.out.println("srcValue = " + srcValue);
        memory.write(des, srcValue, des);
    }
}
