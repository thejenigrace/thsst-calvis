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
			if (src.isMemory()){
				sourceReg = c.hexZeroExtend(memory.read(src, 64), des);
			}
			else{
				sourceReg = c.hexZeroExtend(registers.get(src), des);
			}
		}
		registers.set(des, sourceReg);
	}
	else if ( des.isMemory() ) {
		String sourceReg = "";
		if(desSize == 64 || desSize == 0){
			if( (srcSize == 128 || srcSize == 64) && src.isRegister()){
				sourceReg = registers.get(src).substring(srcSize/4 - 16);
			}
		}
		memory.write(des, sourceReg, 64);
	}
}
