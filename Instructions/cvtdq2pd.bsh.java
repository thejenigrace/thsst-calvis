execute(des, src, registers, memory) {
    String source;

    if ( src.isRegister() ) {
        source = registers.get(src).substring(16);
    } else if ( src.isMemory() ) {
        source = memory.read(src, 64);
    }

    BigInteger b1 = new BigInteger(source.substring(8), 16);
    Converter con1 = new Converter(b1.intValue() + "");
    String r1 = con1.toDoublePrecisionHex();

    BigInteger b2 = new BigInteger(source.substring(0, 8), 16);
    Converter con2 = new Converter(b2.intValue() + "");
    String r2 = con2.toDoublePrecisionHex();

    registers.set(des, r2 + r1);
}
