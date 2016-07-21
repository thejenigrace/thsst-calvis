execute(des, src, registers, memory) {
    String source;

    if ( src.isRegister() ) {
        source = registers.get(src);
    } else if ( src.isMemory() ) {
        source = memory.read(src, 128);
    }

    BigInteger b1 = new BigInteger(source.substring(24), 16);
    Converter con1 = new Converter(b1.intValue() + "");
    String r1 = con1.toSinglePrecisionHex();

    BigInteger b2 = new BigInteger(source.substring(16, 24), 16);
    Converter con2 = new Converter(b2.intValue() + "");
    String r2 = con2.toSinglePrecisionHex();

    BigInteger b3 = new BigInteger(source.substring(8, 16), 16);
    Converter con3 = new Converter(b3.intValue() + "");
    String r3 = con3.toSinglePrecisionHex();

    BigInteger b4 = new BigInteger(source.substring(0, 8), 16);
    Converter con4 = new Converter(b4.intValue() + "");
    String r4 = con4.toSinglePrecisionHex();

    registers.set(des, r4 + r3 + r2 + r1);
}
