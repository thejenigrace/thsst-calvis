execute(des,src,registers,memory) {
    int QWORD = 64;
    String srcValue;
    Calculator calculator = new Calculator(registers,memory);

    //TODO Need to set value into lower 64-bit of XMM Register
    //Memory to XMM: Destination-High DWORD cleared to 0
    //XMM to Memory: Destination-High DWORD should NOT changed
    if(src.isMemory() && des.isRegister()) {
        srcValue = memory.read(src, QWORD);

        String desValue = registers.get(des);
        desValue = calculator.cutToCertainHexSize("getLower", desValue, QWORD);

        srcValue = srcValue.concat(desValue);

        registers.set(des, srcValue);
    } else if(src.isRegister() && des.isMemory()) {
        srcValue = registers.get(src);

        // Get the low quad-word of the register
        srcValue = calculator.cutToCertainHexSize("getLower", srcValue, QWORD/4);

        memory.write(des, srcValue, QWORD);
    }
}
