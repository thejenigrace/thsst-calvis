execute(des, registers, memory) {
    String value = registers.x87().peek();
    if ( des.isRegister() ) {
        if ( registers.getBitSize(des) == 80 ) {
            registers.set(des, value);
        }
    }
    else if ( des.isMemory() ) {
        int size = memory.getBitSize(des); 
        if ( size == 32 ) {
            // conversion to single precision
            value = value.substring(12);

        } else if ( size == 64 ) {
            // conversion to double precision
            value = value.substring(4);
        }
        memory.write(des, value, des);
    }
}
