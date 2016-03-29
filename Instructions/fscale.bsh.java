execute(registers, memory) {
    String st0 = registers.get("ST0");
    String st1 = registers.get("ST1");
    // scale st0 by st1
    // st0 = st0 * 2 ^ roundtowardszero(st1)
    
    registers.set("ST0", st0);
}
