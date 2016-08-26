execute(des, src, registers, memory) {
    String source;

    if ( src.isRegister() ) {
        source = registers.get(src);
    } else if ( src.isMemory() ) {
        source = memory.read(src, 128);
    }

    Converter con1 = new Converter(source.substring(24));
    Float f1 = con1.toSinglePrecision();
    int i1 = f1.intValue();
    con1 = new Converter(i1 + "");
    String r1 = con1.to32BitSignedIntegerHex();

    Converter con2 = new Converter(source.substring(16, 24));
    Float f2 = con2.toSinglePrecision();
    int i2 = f2.intValue();
    con2 = new Converter(i2 + "");
    String r2 = con2.to32BitSignedIntegerHex();

    Converter con3 = new Converter(source.substring(8, 16));
    Float f3 = con3.toSinglePrecision();
    int i3 = f3.intValue();
    con3 = new Converter(i3 + "");
    String r3 = con3.to32BitSignedIntegerHex();

    Converter con4 = new Converter(source.substring(0, 8));
    Float f4 = con4.toSinglePrecision();
    int i4 = f4.intValue();
    con4 = new Converter(i4 + "");
    String r4 = con4.to32BitSignedIntegerHex();

    registers.set(des, r4 + r3 + r2 + r1);
}
