execute(registers, memory) {
	int size = 32;
	String stackPointer = registers.get("ESP");
	String val = registers.getEFlags().getValue();	

	BigInteger stackAddress = new BigInteger(stackPointer, 16);
	BigInteger offset = new BigInteger(size + "");
	offset = offset.divide(new BigInteger("8"));
	stackAddress = stackAddress.subtract(offset);
	
	if ( stackAddress.compareTo(new BigInteger("0", 16)) == -1 ) {
		throw new StackPushException();
	} else {	
		registers.set("ESP", stackAddress.toString(16));
		String stackEntry = registers.get("ESP");
		memory.writeToStack(stackEntry, val, size);
	}
}
