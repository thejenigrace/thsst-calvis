execute(des, registers, memory) {
		if(des.isMemory()){
		String EAXRegister = registers.get("EAX");
		String EDXRegister = registers.get("EDX");
		String ECXRegister = registers.get("ECX");
		String EBXRegister = registers.get("EBX");
		EFlags ef = registers.getEFlags();
		int memSize = memory.getBitSize(des);
		String memoryData = memory.read(des, memSize);
		if ( memSize == 64 ){

			//if(src.isRegister() && registers.getBitSize(src) == registers.getBitSize(des)){
				if((EAXRegister + EDXRegister).equals(memoryData)){
					memory.write(des, ECXRegister + EBXRegister, memSize);
					ef.setZeroFlag("1");
				}
				else{
				registers.set("EDX", memoryData.substring(0, 8));
				registers.set("EAX", memoryData.substring(8, 16));
				ef.setZeroFlag("1");
				}
				//}
			}
		}
		}