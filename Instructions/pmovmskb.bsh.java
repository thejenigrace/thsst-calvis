execute(des, src, registers, memory) {
	Calculator c = new Calculator(registers, memory);
	String destination;
	String source;
	int desSize = 0;
	int srcSize = 0;
	if(des.isRegister()){
		destination = registers.get(des);
		desSize = registers.getBitSize(des);
	}
	if(src.isRegister()){
		if(registers.getBitSize(src) == 64 || registers.getBitSize(src) == 128){
			srcSize = registers.getBitSize(src);
			source = registers.get(src);
		}
	}
	int a = 0;
	if(des.isRegister() && desSize == 32){
		String desIntBits = c.binaryZeroExtend(new BigInteger("0", 16).toString(2), desSize);
		String sourceInBits = c.binaryZeroExtend(new BigInteger(source, 16).toString(2), srcSize);
		StringBuilder myResultString = new StringBuilder(desIntBits);
		for(int y = 0; y < (srcSize / 4) / 2; y++){
			myResultString.setCharAt(y, sourceInBits.charAt( 7 + a ));
			a += 8;
		}

		registers.set(des, new BigInteger(myResultString.toString(), 2).toString(16));
	}
}