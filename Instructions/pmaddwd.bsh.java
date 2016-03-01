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
			sourceReg = executeMultiplyAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfAdd);
		}
	}
}

String executeMultiplyAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfAdd){
	String resultingMultiplyAdd = "";
	for(int x = 0; x < srcSize / 4; x + sizeOfHex * 2){
		ArrayList<StringBuilder> strToBuildDes = new ArrayList<StringBuilder>();
		ArrayList<StringBuilder> strToBuildSrc = new ArrayList<StringBuilder>();
		ArrayList<BigInteger> resultingMultiplication = new ArrayList<BigInteger>();

		for(int y = 0; y < sizeOfHex; y++){
			strToBuildDes.get(0).append(desStr.charAt(x));
			strToBuildSrc.get(0).append(srcStr.charAt(x));
			strToBuildDes.get(1).append(desStr.charAt(x + sizeOfHex));
			strToBuildSrc.get(1).append(srcStr.charAt(x + sizeOfHex));
		}

		for(int z = 0; z < 2 ; z++){
			BigInteger destination = new BigInteger(strToBuildDes).toString(),16);
			BigInteger source = new BigInteger(strToBuildSrc).toString(),16);
			resultingMultiplication.add(destination.multiply(source));
		}
		String resultingAdd = resultingMultiplication.get(0).add(resultingMultiplication.get(1)).toString(16)
		resultingMultiplyAdd += c.hexZeroExtend(resultingAdd, sizeOfHex*2).substring(resultingAdd.length() % sizeOfHex * 2);
	}
	return resultingMultiplyAdd;
}