execute(des,src,registers,memory) {
    int QWORD = 64;
    Calculator calculator = new Calculator(registers,memory);

    //TODO Need to set value into high 64-bit of XMM Register
    //XMM to XMM: Destination-High DWORD should NOT changed
    if(src.isRegister() && des.isRegister()) {
        String desValue = registers.get(des);
        String srcValue = registers.get(src);

        desValue = calculator.cutToCertainHexSize("getLower", desValue, QWORD/4);
        // Get the low double-word of the register
        srcValue = calculator.cutToCertainHexSize("getLower", srcValue, QWORD/4);

        srcValue = srcValue.concat(desValue);

        registers.set(des, srcValue);
    }
}
