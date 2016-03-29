execute(registers, memory) {   
    String st0 = registers.get("ST0");
    String st1 = registers.get("ST1");
    
    // st1 = st1 - st0
    String result = st0;
    // set ST1 to result
    registers.set("ST1", result);
    // pop
    registers.x87().pop();
}

execute(des, src, registers, memory) {
    if ( des.isRegister() && src.isRegister() ) {
        int desSize = registers.getBitSize(des);
        int srcSize = registers.getBitSize(src);
        if ( desSize == srcSize && desSize == 80 ) {
            String desValue = registers.get(des);
            String srcValue = registers.get(src);
            // des = des - src
            String result = desValue;
            // store result to des
            registers.set(des, result);
            // pop
            registers.x87().pop();
        }
    }
}