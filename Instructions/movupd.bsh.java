execute(des,src,registers,memory) {
    int desBitSize = 128;
    int desHexSize = desBitSize / 4;
    String srcValue;

    // Check if SOURCE is either XMM or m128
    if( src.isRegister() ) {
        srcValue = registers.get(src);
    } else if( src.isMemory() ) {
        srcValue = memory.read(src,desBitSize);
    }

    // Check if DESTINATION is either XMM or m128
    if( des.isRegister() ) {
        registers.set(des, srcValue);
    } else if( des.isMemory() ) {
        memory.write(des, srcValue, desBitSize);
    }
}
