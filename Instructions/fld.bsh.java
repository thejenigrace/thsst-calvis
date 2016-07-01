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
        Calculator cal = new Calculator(registers, memory);
        double fpu;
        if ( size == 32 ) {
            // conversion to extended precision
            fpu = cal.convertHexToSinglePrecision(value);
        } else if ( size == 64 ) {
            // conversion
            fpu = cal.convertHexToDoublePrecision(value);

        } else if ( size == 80 ) {
            // conversion
            value = value.substring(4);
            fpu = cal.convertHexToDoublePrecision(value);
        }
        registers.x87().push(String.valueOf(fpu));
    }
}
