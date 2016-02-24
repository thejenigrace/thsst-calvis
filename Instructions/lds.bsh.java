execute(des, src, registers, memory) {
	if(des.isRegister()){
		if(src.isMemory ){
			if(registers.getBitSize(des) == 16){
				doStoreFarPointer(des, src, registers, memory, 16, 32);
			}
			else if(registers.getBitSize(des) == 32){
				doStoreFarPointer(des, src, registers, memory, 32, 64);
			}
		}
	}
}

void doStoreFarPointer(des, src, registers, memory, desSize, srcSize){
	String memoryData = memory.read(src, srcSize);
	switch(srcSize){
		case 32:
			registers.set(registers.get("DS"), memoryData.subString(4));
			registers.set(registers.get(des), memoryData.subString(0,3));
		case 64:
			registers.set(registers.get("DS"), memoryData.subString(8));
			registers.set(registers.get(des), memoryData.subString(0,7));
		break;
	}
}