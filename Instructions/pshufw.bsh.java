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
		countStr = new BigInteger(ctr.getValue(), 16).toString(2);
		String toBeZeroes = "";

		for(int x =0; x < 8 - countStr.length(); x++){
			toBeZeroes += "0";
		}
		countStr = toBeZeroes + countStr;
		System.out.println(countStr);
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
			srcSize = memory.getBitSize(src);
			source = memory.read(src, 64);
	}

	int a = 0;
	if(des.isRegister() && desSize == 64){
		int countDistance = 16;
		String resultingStr = "";
		if(isHex){
			for(int x = 0; x < countImmediate; x++){
				countArr[x] = countStr.substring(x * 2, x * 2 + 2);
				System.out.println(countArr[x] + "count");
			}

			for(int x = 0; x < 4; x++){
				int sourceIndex = Integer.parseInt(countArr[x], 2);
				System.out.println(sourceIndex + " value");
				resultingStr += source.substring( (srcSize / 4 ) - (sourceIndex * countImmediate) - 4, (srcSize / 4 ) - sourceIndex * countImmediate );

			}

			registers.set(des, resultingStr);
		}
	}
}
