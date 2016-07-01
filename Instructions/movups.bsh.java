execute(des,src,registers,memory) {
    int desBitSize = 128;
    int desHexSize = 32;
    String srcValue;

    // Check if SOURCE is either XMM or m128
    if( src.isRegister() ) {
        srcValue = registers.get(src);
    } else if( src.isMemory() ) {
        srcValue = memory.read(src, desBitSize);
    }

    // Check if DESTINATION is either XMM or m128
    if( des.isRegister() ) {
        System.out.println("srcValue = " + srcValue);
        registers.set(des, srcValue);
    } else if( des.isMemory() ) {
        String desEffectiveAddress = memory.removeSizeDirectives(des.getValue());
        System.out.println("desEffectiveAddress = " + desEffectiveAddress);
        if(desEffectiveAddress.substring(desEffectiveAddress.length() - 1).equals("0")) {
            System.out.println("srcValue = " + srcValue);
            memory.write(des, srcValue, desBitSize);
        }
        // System.out.println("srcValue = " + srcValue);
        // memory.write(des, srcValue, desBitSize);
    }
}
