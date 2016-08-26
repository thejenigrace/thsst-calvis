execute(registers, memory) {
    int size = 32;
    String stackPointer = registers.getStackPointer();

    String eax = "EAX";
    String ecx = "ECX";
    String edx = "EDX";
    String ebx = "EBX";
    String esp = "ESP";
    String ebp = "EBP";
    String esi = "ESI";
    String edi = "EDI";

    String[] generalRegisters = {edi, esi, ebp, esp, ebx, edx, ecx, eax};

    BigInteger stackAddress = new BigInteger(stackPointer, 16);
    BigInteger offset = new BigInteger(size + "");
    offset = offset.divide(new BigInteger("8"));

    for ( int i = 0; i < generalRegisters.length; i++ ) {
        if ( stackAddress.compareTo(new BigInteger("FFFE", 16)) == 1 ) {
            throw new StackPopException(offset.intValue());
        } else {
            val = memory.read(stackAddress.toString(16), size);
            registers.set(generalRegisters[i], val);
            stackAddress = stackAddress.add(offset);
            registers.setStackPointer(stackAddress.toString(16));
        }
    }

}
