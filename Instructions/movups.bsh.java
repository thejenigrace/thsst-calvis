execute(des,src,registers,memory) {
    int desBitSize;
    int desHexSize;
    String srcValue;
    boolean isDesSizeNotNull = true;

    // Check if DESTINATION is either XMM or m128
    if(des.isRegister()) {
        desBitSize = registers.getBitSize(des);
        desHexSize = registers.getHexSize(des);
    } else if( des.isMemory() && (memory.getBitSize(des) == 0 ||
    memory.getBitSize(des) == 128) ) {
        desBitSize = 128;
        desHexSize = 32;
    }

    // Check if SOURCE is either XMM or m128
    if( src.isRegister() ) {
        srcValue = registers.get(src);
    } else if( src.isMemory() ) {
        srcValue = memory.read(src, desBitSize);
    }

    if( des.isRegister() && desBitSize == 128 ) {
        System.out.println("srcValue = " + srcValue);
        registers.set(des, srcValue);
    } else if( des.isMemory() ) {
        System.out.println("srcValue = " + srcValue);
        memory.write(des, srcValue, desBitSize);
    }
}
