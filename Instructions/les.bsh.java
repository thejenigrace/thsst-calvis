execute(des, src, registers, memory) {
	if(des.isRegister()){
		if(src.isMemory()){
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
		Calculator c = new Calculator(registers, memory);
		switch(srcSize){
		case 32:
		registers.set("ES", memoryData.substring(4));
//			System.out.println(memoryData.substring(0,4));
		registers.set(des, memoryData.substring(0,4));
		break;
		case 64:
//			registers.set("DS", memoryData.substring(8));
//			registers.set(des.getValue(), memoryData.substring(0,7));
		registers.set("ES", memoryData.substring(8));
		//			System.out.println(memoryData.substring(0,4));
		registers.set(des, memoryData.substring(0,8));
		break;
		}
		}