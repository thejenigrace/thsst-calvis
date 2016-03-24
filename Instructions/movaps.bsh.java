execute(des,src,registers,memory) {
    int desBitSize;
    int desHexSize;
	int srcHexSize;
    String srcValue;
	String desValue;
	boolean isDesSizeNotNull = true;

    if(des.isRegister()) {
        desBitSize = registers.getBitSize(des);
        desHexSize = registers.getHexSize(des);
		desValue = registers.get(des);
    } else if(des.isMemory() && (memory.getBitSize(des) == 0 || memory.getBitSize(des) == 128) ) {
        desBitSize = 128;
        desHexSize = 32;
		desValue = memory.read(des, desBitSize);
		isDesSizeNotNull = false;
    }

    if(src.isRegister() ) {
        srcValue = registers.get(src);
		srcHexSize = registers.getBitSize(src);
    } else if(src.isMemory()) {
        srcValue = memory.read(src,desBitSize);
    }

    if(des.isRegister() && desBitSize == 128) {
		String effectiveAddress = src.getValue();
		effectiveAddress = memory.removeSizeDirectives(effectiveAddress);
//		System.out.println(effectiveAddress);
//		System.out.println(isDesSizeNotNull);
//		System.out.println(src.isMemory());
		if((isDesSizeNotNull && src.isMemory() &&  effectiveAddress.substring(effectiveAddress.length() - 1).equals("0"))
		|| (src.isRegister() && srcHexSize == 128 )){
			registers.set(des, srcValue);
		}

    }
	else if(des.isMemory()) {
		String effectiveAddress = des.getValue();
		effectiveAddress = memory.removeSizeDirectives(effectiveAddress);
		if(!isDesSizeNotNull && effectiveAddress.substring(effectiveAddress.length() - 1).equals("0")){
			memory.write(des, srcValue ,128);
		}
    }
}
