execute(src, registers, memory) throws Exception {
	int size = Memory.MAX_ADDRESS_SIZE; // 32
	String stackPointer = registers.getStackPointer();
	String val = "";
	StringBuilder sbHex = new StringBuilder("");
	if ( src.isRegister() ) {
		size = registers.getBitSize(src);
		val = registers.get(src);

		char[] chars = val.toCharArray();
		for (int i = 0; i < chars.length; i++)
		{
			sbHex.append(Integer.toHexString((int) chars[i]));
		}
		System.out.println(sbHex.toString() + " result");
		System.out.println(val + " val result");
	}

	BigInteger stackAddress = new BigInteger(stackPointer, 16);
	BigInteger offset = new BigInteger(size * 2 + "");
	offset = offset.divide(new BigInteger("8"));
	stackAddress = stackAddress.subtract(offset);

	if ( stackAddress.compareTo(new BigInteger("0", 16)) == -1 ) {
		throw new StackPushException();
	} else {
		registers.setStackPointer(stackAddress.toString(16));
		String stackEntry = registers.getStackPointer();
		memory.writeToStack(stackEntry, sbHex.toString(), size * 2);
	}
}
