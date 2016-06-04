execute(src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    
    if ( src.isMemory() ) {
        int size = memory.getBitSize(src);
        String value = memory.read(src, size);
        if ( size == 80 ) {
            // convert 80 bit BCD to extended floating point
        }
        registers.x87().push(value);
    }
}
