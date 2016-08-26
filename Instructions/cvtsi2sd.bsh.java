execute(des, src, registers, memory) {
    if( des.isRegister() ) {
        String source;
        if ( src.isRegister() ) {
            source = registers.get(src);
        } else if ( src.isMemory() ) {
            source = memory.read(src, 32);
        }

        BigInteger bLower = new BigInteger(source, 16);
        Converter con1 = new Converter(bLower.intValue() + "");
        String lLower = con1.toDoublePrecisionHex();

        String originalValue = registers.get(des);
        originalValue = originalValue.substring(0, 16) + lLower;

        registers.set(des, originalValue);
    }
}
