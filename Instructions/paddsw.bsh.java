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
	String saturateStrAbove = "7";
	String saturateStrBelow = "8";
	String sourceReg = "";
	String desStr = "";
	String srcStr = "";
	for(int x = 1; x < sizeOfHex; x++){
		saturateStrAbove += "F";
		saturateStrBelow += "0";
	}
	
	String sourceReg = "";
	String desStr = "";
	String srcStr = "";
	int sizeOfAdd = 0;
	///end of defining sizes///
	if(des.isRegister()){
		if(desSize == srcSize && (srcSize == 64  || srcSize == 128 ) && src.isRegister() ){
			desStr = registers.get(des);
			srcStr = registers.get(src);
			sourceReg = executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex, saturateStrAbove, saturateStrBelow);
		}
		if((srcSize == 64  || srcSize == 128 ) && src.isMemory() ){
			desStr = registers.get(des);

			srcStr = memory.read(src, desSize);
			sourceReg = executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex, saturateStrAbove, saturateStrBelow);
		}
		registers.set(des, sourceReg);
	}
	if(des.isMemory()){
		if(desSize == srcSize && ( srcSize == 64  || srcSize == 128 ) && src.isRegister() ){
			desStr = memory.read(des, desSize);
			srcStr = registers.get(src);
			sourceReg = executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex, saturateStrAbove, saturateStrBelow);
		}
	}
}

String executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex, saturateStrAbove, saturateStrBelow){
	String resultingAdd = "";
	for(int x = 0; x < srcSize / 4; x = x + sizeOfHex){
		StringBuilder strToBuildDes = new StringBuilder();
		StringBuilder strToBuildSrc = new StringBuilder();
		int intSrc = 0;
		int intDes = 0;
		
		for(int y = 0; y < sizeOfHex; y++){
			strToBuildDes.append(desStr.charAt(x + y));
			strToBuildSrc.append(srcStr.charAt(x + y));
		}

		BigInteger destination = new BigInteger(strToBuildDes.toString(),16);
		BigInteger source = new BigInteger(strToBuildSrc.toString(),16);
		 
		 System.out.println(c.hexZeroExtend(destination.toString(2), sizeOfHex * 4));
		 System.out.println(c.hexZeroExtend(source.toString(2), sizeOfHex * 4) + " nice");
		 
		if(c.hexZeroExtend(destination.toString(2), sizeOfHex * 4).charAt(0) == '1'){
			intDes = Integer.parseInt(negate(des, src, registers, memory, c, sizeOfHex, c.hexZeroExtend(destination.toString(16), sizeOfHex)), 16) * -1;
		}
		else{
			intDes = Integer.parseInt( c.hexZeroExtend(destination.toString(16), sizeOfHex), 16);
		}
		
		if(c.hexZeroExtend(source.toString(2), sizeOfHex * 4).charAt(0) == '1'){
			intSrc = Integer.parseInt(negate(des, src, registers, memory, c, sizeOfHex, c.hexZeroExtend(source.toString(16), sizeOfHex)), 16) * -1;
		}
		else{
			intSrc = Integer.parseInt( c.hexZeroExtend(source.toString(16), sizeOfHex), 16);
		}
		
		
		int result = intSrc + intDes;
		System.out.println(intSrc);
		System.out.println(intDes);
		System.out.println(result + " result");
		if(result > Integer.parseInt(saturateStrAbove, 16)){
			resultingAdd += saturateStrAbove;
		}
		else if(result < Integer.parseInt(saturateStrBelow, 16) * -1){
			resultingAdd += saturateStrBelow;
		}
		else if(result < 0){
			resultingAdd += negate(des, src, registers, memory, c, sizeOfHex, c.hexZeroExtend(new BigInteger(String.valueOf(result * -1), 10).toString(16), sizeOfHex));
		}
		else{
			resultingAdd += c.hexZeroExtend(new BigInteger(String.valueOf(result), 10).toString(16), sizeOfHex);
		}
		
	}
	return resultingAdd;
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