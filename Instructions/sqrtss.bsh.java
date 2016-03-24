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


        String strSource = source.substring(3 * 8, 3 * 8 + 8);
        Long i = Long.parseLong(strSource, 16);
        Float floatSource = Float.intBitsToFloat(i.intValue());
		float result = Math.sqrt(floatSource);

			resultingHex += Integer.toHexString(Float.floatToIntBits(result)).toUpperCase();
		
		registers.set(des, registers.get(des).substring(0, 24) + resultingHex);
	}
}