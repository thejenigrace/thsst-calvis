execute(des,src,registers,memory) {
    int desBitSize = 128;
    int desHexSize = 32;
    String srcValue;

    if(src.isRegister()) {
        srcValue = registers.get(src);
    } else if(src.isMemory()) {
        srcValue = memory.read(src,desBitSize);
    }

    if(des.isRegister()) {
        System.out.println("srcValue = " + srcValue);
        registers.set(des, srcValue);
    } else if(des.isMemory()) {
        // System.out.println("desEffectiveAddress = " + memory.removeSizeDirectives(des.getValue()));
        String desEffectiveAddress = memory.removeSizeDirectives(des.getValue());
        System.out.println("desEffectiveAddress = " + desEffectiveAddress);
        if(desEffectiveAddress.substring(desEffectiveAddress.length() - 1).equals("0")) {
            System.out.println("srcValue = " + srcValue);
            memory.write(des, srcValue, desBitSize);
        }
    }
}
