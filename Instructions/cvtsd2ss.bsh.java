execute(des, src, registers, memory) {
    if( des.isRegister() ) {
        String source;
        if ( src.isRegister() ) {
            source = registers.get(src).substring(16);
        } else if ( src.isMemory() ) {
            source = memory.read(src, 64);
        }

        Converter con1 = new Converter(source);
        Double lLower = con1.toDoublePrecision();
        Float iLower = lLower.floatValue();
        con1 = new Converter(iLower + "");
        String rLower = con1.toSinglePrecisionHex();

        String originalValue = registers.get(des);
        originalValue = originalValue.substring(0, 24) + rLower;

        registers.set(des, originalValue);
    }
}
