execute(src, registers, memory) {
    if ( src.isMemory() ) {
        int size = memory.getBitSize(src);
        String value = memory.read(src, size);
        if ( size == 16 ) {
            // conversion to extended precision

        } else if ( size == 32 ) {
            // conversion

        } else if ( size == 64 ) {
            // conversion
        }
        registers.x87().push(value);
    }
}
