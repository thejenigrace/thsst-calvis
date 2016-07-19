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
        Converter cal = new Converter(value);
        double fpu;
        if ( size == 32 ) {
            fpu = cal.toSinglePrecision();
        } else if ( size == 64 ) {
            fpu = cal.toDoublePrecision();
        } else if ( size == 80 ) {
            value = value.substring(4);
            fpu = cal.toDoublePrecision();
        }
        registers.x87().push(String.valueOf(fpu));
    }
}
