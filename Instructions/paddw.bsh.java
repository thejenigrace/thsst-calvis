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
//			System.out.println(desStr + " des");
//			System.out.println(srcStr + " src");

			sourceReg = executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex);
		}
		if((srcSize == 64  || srcSize == 128 ) && src.isMemory() ){
			desStr = registers.get(des);
			srcStr = memory.read(src, desSize);
			sourceReg = executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex);
		}
		registers.set(des, sourceReg);
	}
	if(des.isMemory()){
		if(desSize == srcSize && ( srcSize == 64  || srcSize == 128 ) && src.isRegister() ){
			desStr = memory.read(des, desSize);
			srcStr = registers.get(src);
			sourceReg = executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex);
		}
	}
}

String executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex){
	String resultingAdd = "";
	for(int x = 0; x < srcSize / 4; x = x + sizeOfHex){
//		System.out.println(x + " x");
		StringBuilder strToBuildDes = new StringBuilder();
		StringBuilder strToBuildSrc = new StringBuilder();

		for(int y = 0; y < sizeOfHex; y++){
			strToBuildDes.append(desStr.charAt(x + y));
			strToBuildSrc.append(srcStr.charAt(x + y));


		}
//		System.out.println(strToBuildDes.toString());
//		System.out.println(strToBuildSrc.toString());
//		System.out.println("end");
		BigInteger destination = new BigInteger((strToBuildDes).toString(),16);
		BigInteger source = new BigInteger((strToBuildSrc).toString(),16);
//		System.out.println(destination.toString(16) + " dest");
		destination = destination.add(source);

//		System.out.println(source.toString(16) + " source");
//		System.out.println(destination.toString(16) + " result");
		resultingAdd += c.hexZeroExtend( destination.toString(16).substring(destination.toString(16).length() % sizeOfHex), sizeOfHex);
		//System.out.println(resultingAdd + " result");
	}
	System.out.println(resultingAdd);
	return resultingAdd;
}