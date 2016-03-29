execute(des, registers, memory) {
    String value = registers.x87().pop();
    if ( des.isMemory() ) {
        int size = memory.getBitSize(des); 
        if (size == 80 ) {
            // need to convert extended precision to 80 bit BCD
        }
        memory.write(des, value, des);
    }
}
