execute(registers, memory) {
	int size = 32;
	String stackPointer = registers.get("ESP");
	BigInteger stackAddress = new BigInteger(stackPointer, 16);
	BigInteger offset = new BigInteger(size + "");
	offset = offset.divide(new BigInteger("8"));
	stackAddress = stackAddress.add(offset);
	
	if ( stackAddress.compareTo(new BigInteger("FFFE", 16)) == 1 ) {
		throw new StackPopException(offset.intValue());
	} else {
		String val = memory.read(stackPointer, size);
		registers.getEFlags().setValue(val);
		registers.set("ESP",stackAddress.toString(16));
	}
}
