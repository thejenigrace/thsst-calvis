execute(des, src, registers, memory) {
	int desSize = 0;
	int srcSize = 0;
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
	///end of defining sizes///
	if ( des.isRegister() ) {
		String sourceReg = "";
		if(desSize == 128 || desSize == 64 ){
			if( srcSize == 32  && src.isRegister() ){
				sourceReg = c.hexZeroExtend(registers.get(src), des);
			}
			else if (src.isMemory()){
				sourceReg = c.hexZeroExtend(memory.read(src, desSize).substring((desSize / 4) - 8, (desSize / 4)), des);
			}
		}
		else if(desSize == 32){
			if( (srcSize == 128 || srcSize == 64) && src.isRegister()){
				sourceReg = registers.get(src).substring(srcSize/4 - 8);
			}
		}
		registers.set(des, sourceReg);
	}
	else if ( des.isMemory() ) {
		String sourceReg = "";
		if(desSize == 32 || desSize == 0){
			if( (srcSize == 128 || srcSize == 64) && src.isRegister()){
				sourceReg = registers.get(src).substring(srcSize/4 - 8);
			}
		}
		memory.write(des, sourceReg, 32);
	}
}
