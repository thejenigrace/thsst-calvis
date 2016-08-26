execute(des, src, registers, memory) {
    int desSize = 0;
    int srcSize = 0;
    Calculator c = new Calculator(registers, memory);
    if(des.isRegister()) {
        desSize = registers.getBitSize(des);
    }
    else{
        desSize = memory.getBitSize(des);
    }
    if(src.isRegister()) {
        srcSize = registers.getBitSize(src);
    }
    else{
        srcSize = memory.getBitSize(src);
        if(srcSize == 0 && desSize == 64) {
            srcSize = 128;
        }
        else if(srcSize == 0 && desSize == 128) {
            srcSize = 128;
        }
    }

    if ( des.isRegister() ) {
        String sourceReg = "";
        if(desSize == 128 || desSize == 64 ) {
            if (src.isMemory()) {
                sourceReg = c.hexZeroExtend(memory.read(src, srcSize/2), des);
            }
            else{
                if(desSize == 64) {
                    sourceReg = c.hexZeroExtend(registers.get(src),des);
                }
                else{
                    sourceReg = c.hexZeroExtend(registers.get(src).substring(16, 32), 128 / 16);
                }

            }

        }
        registers.set(des, sourceReg);
    }
    else if ( des.isMemory() ) {
        String sourceReg = "";
        if(desSize == 64 || desSize == 0) {
            if( (srcSize == 128 || srcSize == 64) && src.isRegister()) {
                sourceReg = registers.get(src).substring(srcSize/4 - 16);
            }
        }
        memory.write(des, sourceReg, 64);
    }
}
