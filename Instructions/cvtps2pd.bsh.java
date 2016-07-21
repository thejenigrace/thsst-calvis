execute(des, src, registers, memory) {
    String source;

    if ( src.isRegister() ) {
        source = registers.get(src).substring(16);
    } else if ( src.isMemory() ) {
        source = memory.read(src, 64);
    }

    Converter con1 = new Converter(source.substring(8));
    Float fLower = con1.toSinglePrecision();
    Double dLower = fLower.doubleValue();
    con1 = new Converter(dLower + "");
    String rLower = con1.toDoublePrecisionHex();

    Converter con2 = new Converter(source.substring(0, 8));
    Float fUpper = con2.toSinglePrecision();
    Double dUpper = fUpper.doubleValue();
    con2 = new Converter(dUpper + "");
    String rUpper = con2.toDoublePrecisionHex();

    registers.set(des, rUpper + rLower);
}