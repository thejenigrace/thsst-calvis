execute(registers, memory) {
	int size = 16;
	String stackPointer = registers.getStackPointer();
	String val = registers.getEFlags().getValue();
	val = val.substring(4);

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
}
