execute(registers, memory) {
	int size = 32;
	String stackPointer = registers.get("ESP");
	
	String eax = registers.get("EAX");
	String ecx = registers.get("ECX");
	String edx = registers.get("EDX");
	String ebx = registers.get("EBX");
	String esp = registers.get("ESP");
	String ebp = registers.get("EBP");
	String esi = registers.get("ESI");
	String edi = registers.get("EDI");

	String[] generalRegisters = {eax, ecx, edx, ebx, esp, ebp, esi, edi};

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
			memory.writeToStack(stackEntry, generalRegisters[i], size);
		}
	}
	
}
