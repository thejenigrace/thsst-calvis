execute(des, src, ctr, registers, memory) {
	Calculator c = new Calculator(registers, memory);
	String destination;
	String source;
	int desSize = 0;
	int srcSize = 0;
	int countImmediate = 4;
	boolean isHex = false;
	String countStr;
	String[] countArr = new String[4];
	if(ctr.isHex()){
		countStr = new BigInteger(ctr.getValue(), "16").toString(2);
		isHex = true;
	}
	
	if(des.isRegister()){
		destination = registers.get(des);
		desSize = registers.getBitSize(des);
	}
	if(src.isRegister()){
		if(registers.getBitSize(src) == 64){
			srcSize = registers.getBitSize(src);
			source = registers.get(src);
		}
	}
	
	else if(src.isMemory()){
		if(memory.getBitSize(src) == 64){
			srcSize = memory.getBitSize(src);
			source = memory.read(src, srcSize);
		}
	}
	
	int a = 0;
	if(des.isRegister() && desSize == 64){
		int countDistance = 16;
		String resultingStr = "";
		if(isHex){
			for(int x = 0; x < countImmediate; x += 2){
				countArr[x] = countStr.substring(x, x + 2);
			}
			
			for(int x = 0; x < 4; x++){
				int sourceIndex = Integer.parseInt(countArr[x], 16);
				resultingStr += source.substring(sourceIndex * 4, sourceIndex * 4 + 4 );
			}
			
			registers.set(des, resultingStr);
		}
	}
}
