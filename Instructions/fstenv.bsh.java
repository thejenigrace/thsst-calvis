execute(des, registers, memory) {
    registers.x87().status().set("C0", '0');
    registers.x87().status().set("C1", '0');
    registers.x87().status().set("C2", '0');
    registers.x87().status().set("C3", '0');
    if ( des.isMemory() ) {
        int size = memory.getBitSize(des);
        String control = registers.x87().control().toHexString();
        String status = registers.x87().status().toHexString();
        String tag = registers.x87().tag().toHexString();

        String env = tag + status + control;

        if ( size == 16 ) {
            memory.write(des, env, 48);
        } else if ( size == 32 ) {
            // special commands
            memory.write(des, env, 48);
        }
    }    
}
