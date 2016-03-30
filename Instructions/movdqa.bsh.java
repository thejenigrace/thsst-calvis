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
		desSize = memory.getBitSize(src);
		
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

	if(registers.getBitSize(des) == 128 && desSize == srcSize){
		if(isRegisterDes){
			if(source.charAt(srcSize/4 -1) != '0'){
				registers.set(des, source);
			}
		}
		else {
			if(source.charAt(srcSize/4 - 1) != '0'){
				memory.write(src, source, desSize);
			}
		}
		

	}
	
	
	
}
