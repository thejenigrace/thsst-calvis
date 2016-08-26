execute(des, src, registers, memory) {
    if ( des.isRegister() ) {
        if ( src.isRegister() ) {

            String x = registers.get(src);
            String y = registers.get(des);
            registers.set(des, x);
            registers.set(src, y);
        }
        else if ( src.isMemory() ) {

            Calculator cal = new Calculator(registers, memory);
            //String address = cal.computeMemoryAddress(src);
            int des_reg_size = registers.getBitSize(des);
            String x = memory.read(src, des_reg_size);

            // get value of registers
            String y = registers.get(des);
            // write value to memory
            memory.write(src, y, des_reg_size);
            // set register to value read from memory;
            registers.set(des, x);

        }
    }
    else if ( des.isMemory() ) {
        Calculator cal = new Calculator(registers, memory);
        if ( src.isRegister() ) {
            String x = registers.get(src);
            int src_reg_size = registers.getBitSize(src);
            // read from memory
            String y = memory.read(des, src_reg_size);
            // set register to value read from memory
            registers.set(src, y);
            // write register value to memory
            memory.write(des, x, src_reg_size);
        }
    }
}
