execute(des, src, registers, memory) {
	Calculator c = new Calculator(registers, memory);
	boolean isRegisterSrc = false;
	int desSize = 0;
	int srcSize = 0;
	String resultingHex = "";
	if(des.isRegister()){
		desSize = registers.getBitSize(des);
	}

	if(src.isRegister()){
		srcSize = registers.getBitSize(src);
		isRegisterSrc = true;
	}

	else if(src.isMemory()){
		srcSize = desSize;
	}

	if(des.isRegister() && (registers.getBitSize(des) == 128  && desSize == srcSize)){
		String destination = registers.get(des);
		String resultingHexAdd = "";
		String source = "";

		if(isRegisterSrc){
			source = registers.get(src);
		}

		else{
			source = memory.read(src, desSize);
		}
//
        String resultingHex = "";
		String strSource = source.substring(1 * 16, 1 * 16 + 16);
		double longSource = c.hexToDoublePrecisionFloatingPoint(strSource);
	    double result = Math.sqrt(longSource);
		resultingHex += c.doublePrecisionFloatingPointToHex(result);
		System.out.println(resultingHex);
		registers.set(des, source.substring(0, 16) + resultingHex);
	}
}