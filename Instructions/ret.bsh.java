execute(src, registers, memory) throws Exception {
    int size = Memory.MAX_ADDRESS_SIZE; // 32
    String stackPointer = registers.getStackPointer();

    BigInteger stackAddress = new BigInteger(stackPointer, 16);
    BigInteger offset = new BigInteger(size + "");
    offset = offset.divide(new BigInteger("8"));
    stackAddress = stackAddress.add(offset);

    // add src to stackAddress to pop it
    if ( src.isHex() ) {
        String extraPopped = src.getValue();
        stackAddress = stackAddress.add(new BigInteger(extraPopped, 16));
    }

    if ( stackAddress.compareTo(new BigInteger("FFFE", 16)) == 1 ) {
        throw new StackPopException(offset.intValue());
    } else {
        String previousInstructionAddress = memory.read(stackPointer, size);
        registers.setStackPointer(stackAddress.toString(16));

        // set EIP to the next instruction
        BigInteger nextInstruction = new BigInteger(previousInstructionAddress, 16);
        nextInstruction = nextInstruction.add(new BigInteger("1"));
        registers.setInstructionPointer(nextInstruction.toString(16));
    }

}

execute(registers, memory) throws Exception {
    int size = Memory.MAX_ADDRESS_SIZE; // 32
    String stackPointer = registers.getStackPointer();

    BigInteger stackAddress = new BigInteger(stackPointer, 16);
    BigInteger offset = new BigInteger(size + "");
    offset = offset.divide(new BigInteger("8"));
    stackAddress = stackAddress.add(offset);

    if ( stackAddress.compareTo(new BigInteger("FFFE", 16)) == 1 ) {
        throw new StackPopException(offset.intValue());
    } else {
        String previousInstructionAddress = memory.read(stackPointer, size);
        registers.setStackPointer(stackAddress.toString(16));

        // set EIP to the next instruction
        BigInteger nextInstruction = new BigInteger(previousInstructionAddress, 16);
        nextInstruction = nextInstruction.add(new BigInteger("1"));
        registers.setInstructionPointer(nextInstruction.toString(16));
    }

}
