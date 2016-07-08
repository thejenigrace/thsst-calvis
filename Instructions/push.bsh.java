execute(src, registers, memory) {
	int size = Memory.MAX_ADDRESS_SIZE; // 32
	String stackPointer = registers.get("ESP");
	String val = "";

	if ( src.isRegister() ) {
		size = registers.getBitSize(src);
		val = registers.get(src);
	} else if ( src.isMemory() ) {
		size = memory.getBitSize(src);
		val = memory.read(src, size);
	} else if ( src.isHex() ) {
		val = src.getValue();
	} else if ( src.isLabel() ) {
		val = memory.getFromVariableMap(src.getValue());
	}

	BigInteger stackAddress = new BigInteger(stackPointer, 16);
	BigInteger offset = new BigInteger(size + "");
	offset = offset.divide(new BigInteger("8"));
	stackAddress = stackAddress.subtract(offset);
	
	if ( stackAddress.compareTo(new BigInteger("0", 16)) == -1 ) {
		throw new StackPushException();
	} else {
		registers.set("ESP",stackAddress.toString(16));
		String stackEntry = registers.get("ESP");
		memory.write(stackEntry, val, size);
	}
}
