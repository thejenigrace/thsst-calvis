execute(des, src, registers, memory) {
    String source;

    if ( src.isRegister() ) {
        source = registers.get(src);
    } else if ( src.isMemory() ) {
        source = memory.read(src, 128);
    }

    Converter con1 = new Converter(source.substring(16));
    Double d1 = con1.toDoublePrecision();
    int i1 = d1.intValue();
    con1 = new Converter(i1 + "");
    String r1 = con1.to32BitSignedIntegerHex();

    Converter con2 = new Converter(source.substring(0, 16));
    Double d2 = con2.toDoublePrecision();
    int i2 = d2.intValue();
    con2 = new Converter(i2 + "");
    String r2 = con2.to32BitSignedIntegerHex();

    registers.set(des, r2 + r1);
}