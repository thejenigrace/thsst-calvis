execute(registers, memory) {
	int size = 16;
	String stackPointer = registers.get("ESP");
	
	String ax = registers.get("AX");
	String cx = registers.get("CX");
	String dx = registers.get("DX");
	String bx = registers.get("BX");
	String sp = registers.get("SP");
	String bp = registers.get("BP");
	String si = registers.get("SI");
	String di = registers.get("DI");

	String[] generalRegisters = {ax, cx, dx, bx, sp, bp, si, di};

	BigInteger stackAddress = new BigInteger(stackPointer, 16);
	BigInteger offset = new BigInteger(size + "");
	offset = offset.divide(new BigInteger("8"));
	
	for ( int i = 0; i < generalRegisters.length; i++ ) {
		stackAddress = stackAddress.subtract(offset);
	
		if ( stackAddress.compareTo(new BigInteger("0", 16)) == -1 ) {
			throw new StackPushException();
		} else {	
			registers.set("ESP", stackAddress.toString(16));
			String stackEntry = registers.get("ESP");
			memory.write(stackEntry, generalRegisters[i], size);
		}
	}
	
}
