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
	String saturateStr = "";
	String sourceReg = "";
	String desStr = "";
	String srcStr = "";
	///end of defining sizes///
	for(int x = 1; x < sizeOfHex; x++){
		saturateStr += "0";
	}
	if(des.isRegister()){
		if(desSize == srcSize && (srcSize == 64  || srcSize == 128 ) && src.isRegister() ){
			desStr = registers.get(des);
			srcStr = registers.get(src);
			sourceReg = executeSub(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfAdd);
		}
		if((srcSize == 64  || srcSize == 128 ) && src.isMemory() ){
			desStr = registers.get(des);
			srcStr = memory.read(src, desSize);
			sourceReg = executeSub(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfAdd);
		}
	}
	if(des.isMemory()){
		if(desSize == srcSize && ( srcSize == 64  || srcSize == 128 ) && src.isRegister() ){
			desStr = memory.read(des, desSize);
			srcStr = registers.get(src);
			sourceReg = executeSub(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfAdd);
		}
	}
}

String executeSub(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfAdd){
	String resultingSub = "";
	for(int x = 0; x < srcSize / 4; x + sizeOfHex){
		StringBuilder strToBuildDes = new StringBuilder();
		StringBuilder strToBuildSrc = new StringBuilder();

		for(int y = 0; y < sizeOfHex; y++){
			strToBuildDes.append(desStr.charAt(x));
			strToBuildSrc.append(srcStr.charAt(x));
		}

		BigInteger destination = new BigInteger(strToBuildDes).toString(),16);
		BigInteger source = new BigInteger(strToBuildSrc).toString(),16);
		destination = destination.sub(source);
		if(destination < new BigInteger("0")){
			resultingSub += saturateStr;
		}
		else{
			resultingSub += c.hexZeroExtend( destination.toString(16), sizeOfHex);
		}
		//resultingSub += c.hexZeroExtend( destination.toString(2).substring(destination.toString(2).length() % sizeOfHex), sizeOfHex);
	}
	return resultingSub;
}
/*
String negate(des, src, registers, memory, c, sizeOfHex, desStr){
	String source = "";
	int borrow = 0;
	String destination = c.hexToBinaryString(destStr, sizeOfHex);
	String result = "";
	int r = 0;

	for(int x = 0; x < sizeOfHex; x++){
		source += "0";
	}

	for(int i = sizeOfHex - 1; i >= 0; i--) {
		r = Integer.parseInt(String.valueOf(source.charAt(i))) - Integer.parseInt(String.valueOf(destination.charAt(i)))  - borrow;
		if( r < 0 ) {
			r += 2;
			result = result.concat(r.toString());

			if( i == 0 ) {
				carry = 1;
			}
		}
		else {
			borrow = 0;
			result = result.concat(r.toString());
		}
	}
	return new StringBuffer(result).reverse().toString();
}
*/