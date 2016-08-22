execute(rel, registers, memory) throws Exception {
	// Determine which address to go to because of CALL
	String toAddress = "";
	if ( rel.isRegister() ) {
		toAddress = registers.get(rel);
	}
	else if ( rel.isMemory() ) {
		toAddress = memory.read(rel, rel);
	}
	else if ( rel.isLabel() ) {
		toAddress = memory.getFromLabelMap(rel.getValue());
	}

	toAddress = Memory.reformatAddress(toAddress);
	String fromAddress = registers.getInstructionPointer();
	BigInteger from = new BigInteger(fromAddress, 16);
	BigInteger to = new BigInteger(toAddress, 16);
	BigInteger result = from.subtract(to);

	// default is rel 16
	Calculator cal = new Calculator(registers, memory);
	if ( cal.isWithinBounds(result, Memory.DEFAULT_RELATIVE_SIZE) ) {
		// Push current EIP to stack
		String stackPointer = registers.getStackPointer();
		BigInteger stackAddress = new BigInteger(stackPointer, 16);
		BigInteger offset = new BigInteger("32");
		offset = offset.divide(new BigInteger("8"));
		stackAddress = stackAddress.subtract(offset);

		registers.setStackPointer(stackAddress.toString(16));
		String stackEntry = registers.getStackPointer();
		memory.writeToStack(stackEntry, fromAddress, 32);
		// Set EIP to determined address
		registers.setInstructionPointer(toAddress);
	}
	else {
		throw new JumpOutOfBoundsException(from, to);
	}
}
