execute(des,src,registers,memory) {
    int DWORD = 32;
    String srcValue;
    Calculator calculator = new Calculator(registers,memory);

    if(src.isRegister()) {
        srcValue = registers.get(src);

        // Get the lov double-word of the register
        srcValue = calculator.cutToCertainHexSize("reverse", srcValue, DWORD/4);
    } else if(src.isMemory()) {
        srcValue = memory.read(src, DWORD);
    }

    //TODO Need to set value into lower 32-bit of XMM Register
    //TODO XMM to XMM: Destination-High DWORD should NOT changed
    //Memory to XMM: Destination-High DWORD cleared to 0
    //XMM to Memory: Destination-High DWORD should NOT changed
    if(des.isRegister()) {
        registers.set(des, srcValue);
    } else if(des.isMemory() && src.isRegister()) {
        memory.write(des, srcValue, DWORD);
    }
}
