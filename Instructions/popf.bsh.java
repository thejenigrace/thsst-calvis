execute(registers, memory) {
    int size = 16;
    String stackPointer = registers.getStackPointer();
    BigInteger stackAddress = new BigInteger(stackPointer, 16);
    BigInteger offset = new BigInteger(size + "");
    offset = offset.divide(new BigInteger("8"));
    stackAddress = stackAddress.add(offset);

    if ( stackAddress.compareTo(new BigInteger("FFFE", 16)) == 1 ) {
        throw new StackPopException(offset.intValue());
    } else {
        String val = "0000" + memory.read(stackPointer, size);
        registers.getEFlags().setValue(val);
        registers.setStackPointer(stackAddress.toString(16));
    }
}
