execute(src, registers, memory) {
    String st = src.getValue();
    if ( src.isRegister() && registers.getBitSize(src) == 80) {
        registers.x87().tag().setTag(st, "11");
        registers.x87().status().set("C0", '0');  
        registers.x87().status().set("C1", '0');  
        registers.x87().status().set("C2", '0');  
        registers.x87().status().set("C3", '0');  
    }
}
