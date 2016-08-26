execute(des, registers, memory) {
    String status = registers.x87().status().getValue();
    if ( des.isRegister() ) {
        registers.set(des, status);
    } else if ( des.isMemory() ) {
        memory.write(des, status, des);
    }
    registers.x87().status().set("C0", '0');
    registers.x87().status().set("C1", '0');
    registers.x87().status().set("C2", '0');
    registers.x87().status().set("C3", '0');
}
