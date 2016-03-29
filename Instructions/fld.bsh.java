execute(src, registers, memory) {
    if ( src.isRegister() ) {
        if ( registers.getBitSize(src) == 80 ) {
            String value = registers.get(src);
            registers.x87().push(value);
        }
    }
    else if ( src.isMemory() ) {
        int size = memory.getBitSize(src);
        String value = memory.read(src, size);
        if ( size == 32 ) {
            // conversion to extended precision

        } else if ( size == 64 ) {
            // conversion

        } else if ( size == 80 ) {
            // conversion
        }
        registers.x87().push(value);
    }
}
