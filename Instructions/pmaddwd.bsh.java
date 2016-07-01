import java.util.ArrayList;
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
			sourceReg = executeMultiplyAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex);
		}
		if((srcSize == 64  || srcSize == 128 ) && src.isMemory() ){
			desStr = registers.get(des);
			srcStr = memory.read(src, desSize);
			sourceReg = executeMultiplyAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex);
		}
		registers.set(des, sourceReg);
	}
	if(des.isMemory()){
		if(desSize == srcSize && ( srcSize == 64  || srcSize == 128 ) && src.isRegister() ){
			desStr = memory.read(des, desSize);
			srcStr = registers.get(src);
			sourceReg = executeMultiplyAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex);
		}
	}
}

String executeMultiplyAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex){
	String resultingMultiplyAdd = "";
	String[] arrBi = new String[12];
	for(int x = 0; x < srcSize / 4; x = x + sizeOfHex){
		boolean isNegSrc = false;
		boolean isNegDes = false;
		String strDes = desStr.substring(x, x + sizeOfHex);
		String strSrc = srcStr.substring(x, x + sizeOfHex);

		BigInteger destination;
		BigInteger source;
		if(c.binaryZeroExtend(new BigInteger(strSrc, 16).toString(2), sizeOfHex * 4).charAt(0) == '1'){
			source = new BigInteger(c.hexZeroExtend(negate(des, src, registers, memory, c, sizeOfHex, strSrc), sizeOfHex), 16).negate();
			isNegSrc = true;
		}
		else{
			source = new BigInteger(strSrc, 16);
		}

		if(c.binaryZeroExtend(new BigInteger(strDes, 16).toString(2), sizeOfHex * 4).charAt(0) == '1'){
			destination = new BigInteger(c.hexZeroExtend(negate(des, src, registers, memory, c, sizeOfHex, strDes), sizeOfHex), 16).negate();
			isNegDes = true;
		}
		else{
			destination = new BigInteger(strDes, 16);
		}
//		System.out.println(source.toString(16) + " source");
//		System.out.println(destination.toString(16) + " destination");
		destination = destination.multiply(source);
//		System.out.println(destination.toString(16));
		if(destination.toString(16).charAt(0) == '-'){
			destination = new BigInteger(c.hexZeroExtend(negate(des, src, registers, memory, c, sizeOfHex, destination.toString(16).substring(1)), sizeOfHex), 16);
		}

		if(isNegDes && !isNegSrc || !isNegDes && isNegSrc){

			arrBi[((x + sizeOfHex)/ sizeOfHex) - 1] = (c.hexFExtend(destination.toString(16), sizeOfHex * 2));
		}
		else{
			arrBi[((x + sizeOfHex)/ sizeOfHex) - 1] = (c.hexZeroExtend(destination.toString(16), sizeOfHex * 2));
		}

	}

	for(int x = 0; x < desSize / 16; x = x + 2){
		String toBePlaced = c.hexZeroExtend(new BigInteger(arrBi[x], 16).add(new BigInteger(arrBi[x + 1], 16)).toString(16), sizeOfHex * 2);
		resultingMultiplyAdd += toBePlaced.substring(toBePlaced.length() - sizeOfHex * 2);
	}
	return resultingMultiplyAdd;
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