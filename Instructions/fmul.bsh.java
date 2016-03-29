execute(src, registers, memory) {
    if ( src.isMemory() ) {
        int size = memory.getBitSize(src);
        String value = memory.read(src, size);
        if ( size == 32 ) {
            // conversion to extended precision

        } else if ( size == 64 ) {
            // conversion

        }
        String st0 = registers.get("ST0");
        String result = st0;
        // st0 = st0 * value
        // store result to ST0;
        registers.set("ST0", result);
    }
}

execute(des, src, registers, memory) {
    if ( des.isRegister() && src.isRegister() ) {
        if ( registers.getBitSize(des) == registers.getBitSize(src) ) {
            String desValue = registers.get(des);
            String srcValue = registers.get(src);
            // des = des * src
            String result = desValue;
            // store result to des
            registers.set(des, result);
        }
    }
}