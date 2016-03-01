execute(des, src, registers, memory) {
	int desSize = 0;
	int srcSize = 0;
	int sizeOfHex = 2;
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
	}
	String sourceReg = "";
	String desStr = "";
	String srcStr = "";
	///end of defining sizes///
	if(des.isRegister()){
		if(desSize == srcSize && (srcSize == 64  || srcSize == 128 ) && src.isRegister() ){
			desStr = registers.get(des);
			srcStr = registers.get(src);
			sourceReg = executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfAdd);
		}
		if((srcSize == 64  || srcSize == 128 ) && src.isMemory() ){
			desStr = registers.get(des);
			srcStr = memory.read(src, desSize);
			sourceReg = executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfAdd);
		}
	}
	if(des.isMemory()){
		if(desSize == srcSize && ( srcSize == 64  || srcSize == 128 ) && src.isRegister() ){
			desStr = memory.read(des, desSize);
			srcStr = registers.get(src);
			sourceReg = executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfAdd);
		}
	}
}

String executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfAdd){
	String resultingAdd = "";
	for(int x = 0; x < srcSize / 4; x + sizeOfHex){
		StringBuilder strToBuildDes = new StringBuilder();
		StringBuilder strToBuildSrc = new StringBuilder();

		for(int y = 0; y < sizeOfHex; y++){
			strToBuildDes.append(desStr.charAt(x));
			strToBuildSrc.append(srcStr.charAt(x));
		}

		BigInteger destination = new BigInteger(strToBuildDes).toString(),16);
		BigInteger source = new BigInteger(strToBuildSrc).toString(),16);
		destination = destination.add(source);
		resultingAdd += c.hexZeroExtend( destination.toString(16).substring(destination.toString(16).length() % sizeOfHex), sizeOfHex);
	}
	return resultingAdd;
}