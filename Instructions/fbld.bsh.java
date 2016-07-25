execute(src, registers, memory) {
    if ( src.isMemory() ) {
        int size = memory.getBitSize(src);
        String value = memory.read(src, size);
        if ( size == 80 ) {
            // convert 80 bit BCD to extended floating point
            Converter convert = new Converter(value);
            // value = 20
            value = convert.toBCD();
        }
        registers.x87().push(value);
    }
}
