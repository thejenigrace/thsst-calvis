execute(des, src, registers, memory) {
    String source;

    if ( src.isRegister() ) {
        source = registers.get(src);
    } else if ( src.isMemory() ) {
        source = memory.read(src, 64);
    }

    BigInteger bLower = new BigInteger(source.substring(8), 16);
    Converter con1 = new Converter(bLower.intValue() + "");
    String lLower = con1.toDoublePrecisionHex();

    BigInteger bUpper = new BigInteger(source.substring(0, 8), 16);
    Converter con2 = new Converter(bUpper.intValue() + "");
    String lUpper = con2.toDoublePrecisionHex();

    registers.set(des, lUpper + lLower);
}
