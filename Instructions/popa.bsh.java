execute(registers, memory) {
    int size = 16;
    String stackPointer = registers.get("ESP");

    String ax = "AX";
    String cx = "CX";
    String dx = "DX";
    String bx = "BX";
    String sp = "SP";
    String bp = "BP";
    String si = "SI";
    String di = "DI";

    String[] generalRegisters = {di, si, bp, sp, bx, dx, cx, ax};

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
            registers.set("ESP",stackAddress.toString(16));
        }
    }

}
