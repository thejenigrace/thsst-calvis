execute(des, src, registers, memory) {
	Float[] floatValuesDes = new Float[4];
	Float[] floatValuesSrc = new Float[4];
	Float[] floatValuesResult = new Float[4];
	Calculator c = new Calculator(registers, memory);
	boolean isRegisterSrc = false;
	int desSize = 0;
	int srcSize = 0;

	if(des.isRegister()){
		desSize = registers.getBitSize(des);
	}

	if(src.isRegister()){
		srcSize = registers.getBitSize(src);
		isRegisterSrc = true;
	}

	else if(src.isMemory()){
			srcSize = memory.getBitSize(src);
	}

	if(des.isRegister()){
		String destination = registers.get(des);
		String resultingHexAdd = "";
		String source = "";
		floatValuesDes = c.HexToSinglePrecisionFloatingPoint(destination);

		if(isRegisterSrc){
			source = registers.get(src);
		}

		else{
			source = memory.read(src, desSize);
		}

		floatValuesSrc = c.HexToSinglePrecisionFloatingPoint(source);

		for(int x = 0; x < floatValuesDes.length; x++){
			floatValuesResult[x] = (floatValuesDes[x] / floatValuesSrc[x]);
		}

		resultingHexAdd = c.SinglePrecisionFloatingPointToHex(floatValuesResult);
		registers.set(des, resultingHexAdd);
	}
}