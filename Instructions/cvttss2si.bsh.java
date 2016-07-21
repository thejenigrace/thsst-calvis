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
        int iLower = lLower.intValue();
        con1 = new Converter(iLower + "");
        String rLower = con1.to32BitSignedIntegerHex();

        registers.set(des, rLower);
    }
}