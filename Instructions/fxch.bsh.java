execute(src, registers, memory) {
    if ( src.isRegister() ) {
        if ( registers.getBitSize(src) == 80 ) {
            String stx = registers.get(src);
            String st0 = registers.get("ST0");
            registers.set(src, st0);
            registers.set("ST0", stx);
        }
    }
}

execute(registers, memory) {
    String st0 = registers.get("ST0");
    String st1 = registers.get("ST1");
    registers.set("ST1", st0);
    registers.set("ST0", st1);
}
