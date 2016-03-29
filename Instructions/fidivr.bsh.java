execute(src, registers, memory) {
    if ( src.isMemory() ) {
        int size = memory.getBitSize(src);
        String value = memory.read(src, size);
        if ( size == 16 ) {
            // conversion to extended precision

        } else if ( size == 32 ) {
            // conversion

        }
        String st0 = registers.get("ST0");
        String result = st0;
        // st0 = value / st0
        // store result to ST0;
        registers.set("ST0", result);
    }
}