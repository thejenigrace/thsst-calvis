execute(des, src, registers, memory) {
	Calculator c = new Calculator(registers, memory);
	boolean isRegisterSrc = false;
	int desSize = 0;
	int srcSize = 0;
	String resultingHex = "";
	if(des.isRegister()){
		desSize = registers.getBitSize(des);
	}

	if(src.isRegister()){
		srcSize = registers.getBitSize(src);
		isRegisterSrc = true;
	}

	else if(src.isMemory()){
		srcSize = desSize;
	}

	if( (registers.getBitSize(des) == 64 || registers.getBitSize(des) == 128)  && desSize == srcSize){
		String destination = registers.get(des);
		String resultingHexAdd = "";
		String source = "";

		if(isRegisterSrc){
			source = registers.get(src);
		}

		else{
			source = memory.read(src, desSize);
		}
		
		for(int x = 0; x < (srcSize/4) / 2; x++){
			String subSource = source.substring(x * 2, x * 2 + 2);
			String subDes = destination.substring(x * 2, x * 2 + 2);
			String sourceBits = new BigInteger(subSource, 16).toString(2);
			sourceBits = c.binaryZeroExtend(sourceBits, 8);
			String desBits = new BigInteger(subDes, 16).toString(2);
			sourceBits = c.binaryZeroExtend(sourceBits, 8);
			int sourceConverted = Integer.parseInt(getUnSigned(sourceBits, c));
			int desConverted = Integer.parseInt(getUnSigned(desBits, c));
			
			if(sourceConverted >= desConverted){
				resultingHex += subSource;
			}
			else{
				resultingHex += subDes;
			}
			
		}
		
		registers.set(des, resultingHex);
	}
	
	
	
}

String getUnSigned(stringBits, calculator){
	StringBuilder tempBit = new StringBuilder(stringBits);
	String returnable = "";
	
	if(tempBit.charAt(0) == '1'){
		tempBit.setCharAt(0, '0');
		tempBit.insert(1, "1");
		BigInteger bi = new BigInteger(tempBit.toString(), 2);
		returnable = bi.toString(10);
		
	}
	else{
		BigInteger bi = new BigInteger(tempBit.toString(), 2);
		returnable = bi.toString(10);
	}
	return returnable;
		
}