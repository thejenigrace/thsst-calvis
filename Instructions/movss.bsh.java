execute(des,src,registers,memory) {
    int DWORD = 32;
    String srcValue;
    Calculator calculator = new Calculator(registers,memory);

    if(src.isRegister()) {
        srcValue = registers.get(src);

        // Get the low double-word of the register
        srcValue = calculator.cutToCertainHexSize("getLower",srcValue,DWORD/4);
    }

    //TODO Need to set value into lower 32-bit of XMM Register
    //XMM to XMM: Destination-High DWORD should NOT changed
    if(des.isRegister() && src.isRegister()) {
        String desValue = registers.get(des);
        // int desHexSize = registers.getHexSize(des);

        desValue = calculator.cutToCertainHexSize("getUpper",desValue,DWORD*3/4);

        srcValue = desValue.concat(srcValue);
        registers.set(des, srcValue);
    //Memory to XMM: Destination-High DWORD cleared to 0
    } else if(des.isRegister() && src.isMemory()) {
        srcValue = memory.read(src, DWORD);
        registers.set(des, srcValue);
    //XMM to Memory: Destination-High DWORD should NOT changed
    } else if(des.isMemory() && src.isRegister()) {
        memory.write(des, srcValue, DWORD);
    }
}
