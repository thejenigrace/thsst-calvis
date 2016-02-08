execute(rel, registers, memory) throws Exception {
	Calculator cal = new Calculator(registers, memory);
	String toAddress = "";
	if ( rel.isRegister() ){
		toAddress = registers.get(rel);
	}
	else if ( rel.isMemory() ){
		toAddress = memory.read(rel, rel);
	}
	else if ( rel.isLabel() ){
		System.out.println("Finding: " + rel.getValue());
		toAddress = memory.getFromLabelMap(rel.getValue());
	}

	toAddress = Memory.reformatAddress(toAddress);
	String fromAddress = registers.getInstructionPointer();
	BigInteger from = new BigInteger(fromAddress, 16);
	BigInteger to = new BigInteger(toAddress, 16);
	BigInteger result = from.subtract(to);

	// default is rel 16
	if ( cal.isWithinBounds(result, Memory.DEFAULT_RELATIVE_SIZE) ){
		System.out.println("Jumping from: " + fromAddress + " to " + toAddress);
		registers.setInstructionPointer(toAddress);
	}
	else {
		throw new JumpOutOfBoundsException(from, to);
	}
}
