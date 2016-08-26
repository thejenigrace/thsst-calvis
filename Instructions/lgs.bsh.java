execute(des, src, registers, memory) {
    Calculator c = new Calculator(registers, memory);
    if(des.isRegister()) {
        if(src.isMemory()) {
            if(registers.getBitSize(des) == 16) {
                registers.set(des, c.hexZeroExtend(memory.read(src, 16), 4));
                registers.set("GS", "0");
            }
            else if(registers.getBitSize(des) == 32) {
                registers.set(des, c.hexZeroExtend(memory.read(src, 32), 8));
                registers.set("GS", "0");
            }
        }
    }
}
