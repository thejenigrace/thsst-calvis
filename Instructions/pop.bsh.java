execute(des, registers, memory) throws Exception {
    int size = Memory.MAX_ADDRESS_SIZE; // 32
    String stackPointer = registers.getStackPointer();
    String val = "";

    if ( des.isRegister() ) {
        size = registers.getBitSize(des);
    } else if ( des.isMemory() ) {
        size = memory.getBitSize(des);
    }

    BigInteger stackAddress = new BigInteger(stackPointer, 16);
    BigInteger offset = new BigInteger(size + "");
    offset = offset.divide(new BigInteger("8"));
    stackAddress = stackAddress.add(offset);

    if ( stackAddress.compareTo(new BigInteger("FFFE", 16)) == 1 ) {
        throw new StackPopException(offset.intValue());
    } else {
        if ( des.isRegister() ) {
            val = memory.read(stackPointer, size);
            registers.set(des, val);
        } else if ( des.isMemory() ) {
            val = memory.read(stackPointer, size);
            memory.write(des, val, des);
        }

        registers.setStackPointer(stackAddress.toString(16));
    }

}
