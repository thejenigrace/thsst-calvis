execute(des,src,registers,memory) {
    int desBitSize;
    int desHexSize;
    String desValue;
    String srcValue;

    if(des.isRegister()) {
        desBitSize = registers.getBitSize(des);
        desHexSize = registers.getHexSize(des);
        desValue = registers.get(des);
    } else if(des.isMemory()) {
        desBitSize = memory.getBitSize(des);
        desHexSize = memory.getHexSize(des);
        desValue = memory.read(des,desBitSize);
    }

    if(src.isRegister()) {
        srcValue = registers.get(src);
    } else if(src.isMemory()) {
        // System.out.println("srcBaseAddress = " + memory.getBaseAddress(src));
        srcValue = memory.read(src,desBitSize);
    }

    if(des.isRegister()) {
        System.out.println("srcValue = " + srcValue);
        registers.set(des, srcValue);
    } else if(des.isMemory()) {
        // System.out.println("desBaseAddress = " + memory.removeSizeDirectives(des.getValue()));
        String desBaseAddress = memory.removeSizeDirectives(des.getValue());
        System.out.println("desBaseAddress = " + desBaseAddress);
        if(desBaseAddress.equals("00000000")) {
            System.out.println("srcValue = " + srcValue);
            memory.write(des, srcValue, des);
        }
    }
}
