execute(des, src, registers, memory) {
	int desSize = 0;
	int srcSize = 0;
	int sizeOfHex = 16;
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
			sourceReg = executeSub(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex);
		}
		if((srcSize == 64  || srcSize == 128 ) && src.isMemory() ){
			desStr = registers.get(des);
			srcStr = memory.read(src, desSize);
			sourceReg = executeSub(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex);
		}
		registers.set(des, sourceReg);
	}
	if(des.isMemory()){
		if(desSize == srcSize && ( srcSize == 64  || srcSize == 128 ) && src.isRegister() ){
			desStr = memory.read(des, desSize);
			srcStr = registers.get(src);
			sourceReg = executeSub(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex);
		}
	}
}

String executeSub(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex){
	String resultingSub = "";
	for(int x = 0; x < srcSize / 4; x = x + sizeOfHex){
		StringBuilder strToBuildDes = new StringBuilder();
		StringBuilder strToBuildSrc = new StringBuilder();

		for(int y = 0; y < sizeOfHex; y++){
			strToBuildDes.append(desStr.charAt(x + y));
			strToBuildSrc.append(srcStr.charAt(x + y));
		}

		BigInteger destination = new BigInteger((strToBuildDes).toString(),16);
		BigInteger source = new BigInteger((strToBuildSrc).toString(),16);
//		System.out.println("result sub source: " + new BigInteger(negate(des, src, registers, memory, c, sizeOfHex, desStr), 2).toString(16));
		destination = destination.subtract(source);
		destination = destination.multiply(new BigInteger("-1", 10));
		resultingSub += c.hexZeroExtend(negate(des, src, registers, memory, c, sizeOfHex, destination.toString(16)), sizeOfHex);
		//System.out.println(resultingSub + " resultsss");
//		resultingSub += c.hexZeroExtend( destination.toString(16).substring(destination.toString(16).length() % sizeOfHex), sizeOfHex);
	}
	return resultingSub;
}

String negate(des, src, registers, memory, c, sizeOfHex, desStr){
	String source = "";
	int borrow = 0;
	String destination = c.hexToBinaryString(desStr, sizeOfHex);
	destination = c.binaryZeroExtend(destination, sizeOfHex * 4);
	StringBuilder sb = new StringBuilder(destination);
	
	for(int x = 0; x < destination.length(); x++){
		if(sb.charAt(x) == '1'){
			sb.setCharAt(x, '0');
		}
		else{
			sb.setCharAt(x, '1');
		}
	}
	
	return new BigInteger(sb.toString(), 2).add(new BigInteger("1")).toString(16);

}