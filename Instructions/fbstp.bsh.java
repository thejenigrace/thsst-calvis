execute(des, registers, memory) {
    String value = registers.x87().pop();
    if ( des.isMemory() ) {
        int size = memory.getBitSize(des);
        if ( size == 80 ) {
            Converter converter = new Converter(value);
            value = converter.toBCDHexString();
        }
        memory.write(des, value, des);
    }
}
