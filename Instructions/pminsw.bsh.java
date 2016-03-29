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

	if(des.isRegister() && (registers.getBitSize(des) == 64 || registers.getBitSize(des) == 128)  && desSize == srcSize){
		String destination = registers.get(des);
		String resultingHexAdd = "";
		String source = "";

		if(isRegisterSrc){
			source = registers.get(src);
		}

		else{
			source = memory.read(src, desSize);
		}
		
		for(int x = 0; x < (srcSize/4) / 4; x++){
			short sourceSigned =  (short) Integer.parseInt(source.substring(x * 4, x * 4 + 4), 16);
			short desSigned =  (short) Integer.parseInt(destination.substring(x * 4, x * 4 + 4), 16);

			if(desSigned <= sourceSigned){
				resultingHex += destination.substring(x * 4, x * 4 + 4);
			}
			else{
				resultingHex += source.substring(x * 4, x * 4 + 4);
			}
			System.out.println(resultingHex);
		}

		registers.set(des, resultingHex);
	}
}