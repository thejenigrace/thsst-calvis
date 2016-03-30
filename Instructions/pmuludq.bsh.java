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

	if((registers.getBitSize(des) == 64 || registers.getBitSize(des) == 128)  && desSize == srcSize){
		String destination = registers.get(des);
		String resultingHexAdd = "";
		String source = "";
		if(isRegisterSrc){
			source = registers.get(src);
		}
		else{
			source = memory.read(src, desSize);
		}
		switch(srcSize){
			case 64:
				BigInteger intSrc = new BigInteger(getUnSigned(c.binaryZeroExtend(new BigInteger(source, 16).toString(2), 64), c), 10);
				BigInteger intDes = new BigInteger(getUnSigned(c.binaryZeroExtend(new BigInteger(destination, 16).toString(2), 64), c), 10);
				resultingHex = intDes.multiply(intSrc).toString(16);
				if(resultingHex.length() > 16){
					int length = resultingHex.length();
					System.out.println(length);
					System.out.println(resultingHex);
					registers.set(des, resultingHex.substring(length - 16, length));
				}
				else{
					registers.set(des, resultingHex);
				}
			break;
			case 128:

				BigInteger intSrc = new BigInteger(getUnSigned(c.binaryZeroExtend(new BigInteger(source, 16).toString(2), 64), c), 10);
				BigInteger intDes = new BigInteger(getUnSigned(c.binaryZeroExtend(new BigInteger(destination, 16).toString(2), 64), c), 10);
				resultingHex = intDes.multiply(intSrc).toString(16);
				System.out.println(resultingHex);
				if(resultingHex.length() > 16){
					int length = resultingHex.length();

					registers.set(des, resultingHex.substring(length - 16, length));
				}
				else{
					registers.set(des, resultingHex);
				}
			break;
		}

	}
		
//	registers.set(des, resultingHex);
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