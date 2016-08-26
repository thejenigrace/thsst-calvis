execute(des, src, registers, memory) {
    String source;

    if ( src.isRegister() ) {
        source = registers.get(src);
    } else if ( src.isMemory() ) {
        source = memory.read(src, 128);
    }

    Converter con1 = new Converter(source.substring(16));
    Double dLower = con1.toDoublePrecision();
    float fLower = dLower.floatValue();
    con1 = new Converter(fLower + "");
    String rLower = con1.toSinglePrecisionHex();

    Converter con2 = new Converter(source.substring(0, 16));
    Double dUpper = con2.toDoublePrecision();
    float fUpper = dUpper.floatValue();
    con2 = new Converter(fUpper + "");
    String rUpper = con2.toSinglePrecisionHex();

    registers.set(des, rUpper + rLower);
}
