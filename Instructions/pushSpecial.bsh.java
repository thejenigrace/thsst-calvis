execute(src, des, registers, memory) throws Exception {
	int size = Memory.MAX_ADDRESS_SIZE; // 32
	String stackPointer = registers.getStackPointer();
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
		registers.setStackPointer(stackAddress.toString(16));
		String stackEntry = registers.getStackPointer();
		memory.writeToStack(stackEntry, val, size);
	}
	// proceed to store string
	String registerValue = registers.get(des);
	String decimalValue = new BigInteger(registerValue, 16).toString(10) + "";
	char[] chars = decimalValue.toCharArray();
	String sbHex = "";
	for (int i = 0; i < chars.length; i++)
	{
		sbHex += Integer.toHexString((int) chars[i]);
	}
	int regSize = registers.getBitSize(des);
	System.out.println("SBHEX: " + sbHex);
	memory.write(val, sbHex, sbHex.length() * 8);
}
