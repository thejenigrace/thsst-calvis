execute(des, src, registers, memory) {
    String source;

    if ( src.isRegister() ) {
        source = registers.get(src).substring(16);
    } else if ( src.isMemory() ) {
        source = memory.read(src, 64);
    }

    Converter con1 = new Converter(source.substring(8));
    Float lLower = con1.toSinglePrecision();
    int iLower = lLower.intValue();
    con1 = new Converter(iLower + "");
    String rLower = con1.to32BitSignedIntegerHex();

    Converter con2 = new Converter(source.substring(0, 8));
    Float lUpper = con2.toSinglePrecision();
    int iUpper = lUpper.intValue();
    con2 = new Converter(iUpper + "");
    String rUpper = con2.to32BitSignedIntegerHex();

    registers.set(des, rUpper + rLower);
}