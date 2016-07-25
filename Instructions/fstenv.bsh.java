execute(des, registers, memory) {
    registers.x87().status().set("C0", '0');
    registers.x87().status().set("C1", '0');
    registers.x87().status().set("C2", '0');
    registers.x87().status().set("C3", '0');
    if ( des.isMemory() ) {
        int size = memory.getBitSize(des);
        String control = registers.x87().control().getValue();
        String status = registers.x87().status().getValue();
        String tag = registers.x87().tag().getValue();

        String env = tag + status + control;
        memory.write(des, env, 48);

    }
}
