execute(des, src, registers, memory) {
    if( des.isRegister() ) {
        String source;
        if ( src.isRegister() ) {
            source = registers.get(src).substring(24);
        } else if ( src.isMemory() ) {
            source = memory.read(src, 32);
        }

        Converter con1 = new Converter(source);
        Float lLower = con1.toSinglePrecision();
        Double iLower = lLower.doubleValue();
        con1 = new Converter(iLower + "");
        String rLower = con1.toDoublePrecisionHex();

        String originalValue = registers.get(des);
        originalValue = originalValue.substring(0, 16) + rLower;

        registers.set(des, originalValue);
    }
}
