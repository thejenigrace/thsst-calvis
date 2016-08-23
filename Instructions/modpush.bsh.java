execute(des, src, registers, memory) throws Exception {
	String memAddress = des.getValue();
	int size = Memory.MAX_ADDRESS_SIZE; // 32
	String stackPointer = registers.getStackPointer();
	String val = "";
	StringBuilder sbHex = new StringBuilder("");
	if ( src.isRegister() ) {
		size = registers.getBitSize(src);
		val = registers.get(src);
		val = new BigInteger(val, 16).toString(10);
		System.out.println("val " + val);
		char[] chars = val.toCharArray();
		for (int i = 0; i < chars.length; i++)
		{
			sbHex.append(Integer.toHexString((int) chars[i]));
		}
//		System.out.println(sbHex.toString() + " result");
	}

	memory.write(des, sbHex.toString(), size * 2);
	System.out.println(memory.read(des, size * 2));

	BigInteger stackAddress = new BigInteger(stackPointer, 16);
	BigInteger offset = new BigInteger((size / 2) + "");
	offset = offset.divide(new BigInteger("8"));
	stackAddress = stackAddress.subtract(offset);

	if ( stackAddress.compareTo(new BigInteger("0", 16)) == -1 ) {
		throw new StackPushException();
	} else {
		registers.setStackPointer(stackAddress.toString(16));
		String stackEntry = registers.getStackPointer();
		memory.writeToStack(stackEntry, memAddress, size);
	}
}
