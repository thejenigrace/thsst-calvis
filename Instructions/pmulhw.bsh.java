execute(des, src, registers, memory) {
	int desSize = 0;
	int srcSize = 0;
	int sizeOfHex = 4;
	Calculator c = new Calculator(registers, memory);
	if(des.isRegister()){
		desSize = registers.getBitSize(des);
	}
	else{
		desSize = memory.getBitSize(des);
	}
	if(src.isRegister()){
		srcSize = registers.getBitSize(src);
	}
	else{
		srcSize = memory.getBitSize(src);
		if( srcSize == 0){
		srcSize = registers.getBitSize(des);
		}
	}
	String sourceReg = "";
	String desStr = "";
	String srcStr = "";
	///end of defining sizes///
	if(des.isRegister()){
		if(desSize == srcSize && (srcSize == 64  || srcSize == 128 ) && src.isRegister() ){
			desStr = registers.get(des);
			srcStr = registers.get(src);
			sourceReg = executeMultiply(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex);
		}
		if((srcSize == 64  || srcSize == 128 ) && src.isMemory() ){
			desStr = registers.get(des);
			srcStr = memory.read(src, desSize);
			sourceReg = executeMultiply(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex);
		}
		registers.set(des, sourceReg);
	}
	if(des.isMemory()){
		if(desSize == srcSize && ( srcSize == 64  || srcSize == 128 ) && src.isRegister() ){
			desStr = memory.read(des, desSize);
			srcStr = registers.get(src);
			sourceReg = executeMultiply(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex);
		}
	}
}

String executeMultiply(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex){
	String resultingMultiply = "";
	for(int x = 0; x < srcSize / 4; x = x + sizeOfHex){
		StringBuilder strToBuildDes = new StringBuilder();
		StringBuilder strToBuildSrc = new StringBuilder();

		for(int y = 0; y < sizeOfHex; y++){
			strToBuildDes.append(desStr.charAt(x));
			strToBuildSrc.append(srcStr.charAt(x));
		}

		BigInteger destination = new BigInteger((strToBuildDes).toString(),16);
		BigInteger source = new BigInteger((strToBuildSrc).toString(),16);
		destination = destination.multiply(source);

		resultingMultiply += c.hexZeroExtend(destination.toString(16), sizeOfHex * 2).substring(0, sizeOfHex);
		System.out.println(c.hexZeroExtend(destination.toString(16), sizeOfHex * 2).substring(0, sizeOfHex) + " hex result");
	}
	return resultingMultiply;
}