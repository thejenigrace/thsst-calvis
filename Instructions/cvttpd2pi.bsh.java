execute(des, src, registers, memory) {
    String source;

    if ( src.isRegister() ) {
        source = registers.get(src);
    } else if ( src.isMemory() ) {
        source = memory.read(src, 128);
    }

    Converter con1 = new Converter(source.substring(16));
    Double lLower = con1.toDoublePrecision();
    int iLower = lLower.intValue();
    con1 = new Converter(iLower + "");
    String rLower = con1.to32BitSignedIntegerHex();

    Converter con2 = new Converter(source.substring(0, 16));
    Double lUpper = con2.toDoublePrecision();
    int iUpper = lUpper.intValue();
    con2 = new Converter(iUpper + "");
    String rUpper = con2.to32BitSignedIntegerHex();

    registers.set(des, rUpper + rLower);
}