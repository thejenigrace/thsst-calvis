execute(src, registers, memory) {
    String control = registers.x87().control().getValue();
    memory.write(src, control, src);
    registers.x87().status().set("C0", '0');
    registers.x87().status().set("C1", '0');
    registers.x87().status().set("C2", '0');
    registers.x87().status().set("C3", '0');
}
