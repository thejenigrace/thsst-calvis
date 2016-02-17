execute(des, src, registers, memory) {
	if(des.isRegister()){
		if(src.isMemory ){
			if(memory.getBitSize(src) == 32 && registers.getBitSize(des) == 16){
			doStoreFarPointer(des, src, registers, memory, 16, 32);
			}
			else if(memory.getBitSize(src) == 48 && registers.getBitSize(des) == 32){
			doStoreFarPointer(des, src, registers, memory, 32, 48);
			}
			else if(memory.getBitSize(src) == 64 && registers.getBitSize(des) == 32){
			doStoreFarPointer(des, src, registers, memory, 32, 64);
			}
		}
	}
}

void doStoreFarPointer(des, src, registers, memory, desSize, srcSize){
	String memoryData = memory.read(src, srcSize);
	switch(srcSize){
		case 32:
		case 48:
		case 64:
		registers.set(registers.get("SS"), memoryData.subString(0, 4));
		registers.set(registers.get(des), memoryData.subString(4));
		break;
	}
}