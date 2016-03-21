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
		if(desSize == 128){
			if( srcSize == 64 && src.isRegister() ){
				sourceReg = c.hexZeroExtend(registers.get(des), des);
			}
			else if (src.isMemory()){
				sourceReg = c.hexZeroExtend(memory.read(src, desSize / 2), des);
			}
		}
		else if(desSize == 64){
			if( srcSize == 128 && src.isRegister() ){
				int midpoint = str.length() / 2;
				sourceReg = c.hexZeroExtend(registers.get(src).substring(midpoint), des);
			}
			else if ( src.isMemory() ){
				sourceReg = c.hexZeroExtend(memory.read(src, desSize / 2), des);
			}
		}
		registers.set(des, sourceReg);
	}
	else if ( des.isMemory() ) {
		String sourceReg = "";
		if(desSize == 128){
			if( ( srcSize == 64 || srcSize == 32 ) && src.isRegister() ){
				sourceReg = c.hexZeroExtend(registers.get(des), des);
			}
		}
		else if(desSize == 64){
			if( srcSize == 128 && src.isRegister() ){
				int midpoint = str.length() / 2;
				sourceReg = c.hexZeroExtend(registers.get(src).substring(midpoint), des);
			}
		}
		memory.write(des, sourceReg, des);
	}
}
