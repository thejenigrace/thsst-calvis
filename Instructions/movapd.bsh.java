execute(des,src,registers,memory) {
    int desBitSize = 128;
    int desHexSize = 32;
    String srcValue;

    if(src.isRegister()) {
        srcValue = registers.get(src);
    } else if(src.isMemory()) {
        srcValue = memory.read(src,desBitSize);
		if(src.getValue().substring(src.getValue().length()-1).equals("0") && !src.getValue().equals("00000000")){
			throw new MemoryAlignmentException(src.getValue());
		}
    }

    if(des.isRegister()) {
        System.out.println("srcValue = " + srcValue);
        registers.set(des, srcValue);
    } else if(des.isMemory()) {
		if(des.getValue().substring(des.getValue().length() - 1).equals("0") && !des.getValue().equals("00000000")){
			throw new MemoryAlignmentException(des.getValue());
		}
        // System.out.println("desEffectiveAddress = " + memory.removeSizeDirectives(des.getValue()));
        String desEffectiveAddress = memory.removeSizeDirectives(des.getValue());
        System.out.println("desEffectiveAddress = " + desEffectiveAddress);
        System.out.println("srcValue = " + srcValue);
        memory.write(des, srcValue, desBitSize);

    }
}
