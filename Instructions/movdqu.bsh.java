execute(des, src, registers, memory) {
	Calculator c = new Calculator(registers, memory);
	boolean isRegisterSrc = false;
	boolean isRegisterDes = false;
	int desSize = 0;
	int srcSize = 0;
	String destination = "";
	String source = "";
	String resultingHex = "";
	if(des.isRegister()){
		desSize = registers.getBitSize(des);
		isRegisterDes = true;
		
	}
	else{
		desSize = memory.getBitSize(des);
		if(desSize == 0){
			desSize = registers.getBitSize(src);
		}
	}

	if(src.isRegister()){
		srcSize = registers.getBitSize(src);
		isRegisterSrc = true;
		source = registers.get(src);
	}
	else{
		source = memory.read(src, desSize);
		
	}

	
	
	if(src.isMemory()){
		srcSize = desSize;
	}

	if(desSize == srcSize && srcSize == 128){

			if(isRegisterDes){
					registers.set(des, source);
			}
			else {
					memory.write(des, source, desSize);
			}

	}

	
	
	
}
