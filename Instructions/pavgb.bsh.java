execute(des,src,registers,memory) {
    Calculator c = new Calculator(registers, memory);
    boolean isRegisterSrc = false;
    int desSize = 0;
    int srcSize = 0;
    String source;
		System.out.println(des + "wut");
    String destination;
    if(des.isRegister()){
        desSize = registers.getBitSize(des);
        destination = registers.get(des);
    }

    if(src.isRegister()){
        srcSize = registers.getBitSize(src);
        isRegisterSrc = true;
        source = registers.get(src);
    }

    else if(src.isMemory()){
        srcSize = memory.getBitSize(src);
        source = memory.read(src, desSize);
    }

    if(des.isRegister()){
		if(desSize == 64){
			if((src.isRegister() && srcSize == 64) || src.isMemory() && (srcSize == 0 || srcSize == 64)){
                registers.set(des, c.computeAveragePackedHex(destination, source, 8));
			}
		}
		else if(desSize == 128){
			if((src.isRegister() && srcSize == 128) || src.isMemory() && (srcSize == 0 || srcSize == 128)){
			registers.set(des, c.computeAveragePackedHex(destination, source, 8));
			}
		}

//        else if(desSize == 64){
//            if((src.isRegister() && srcSize == 128) || src.isMemory() && (srcSize == 0 || srcSize == 64)){
//                registers.set(des, c.computeAveragePackedHex(destination, source, 16));
//            }
//        }
	}
}