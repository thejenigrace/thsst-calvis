execute(des,src,registers,memory) {
    int QWORD = 64;
    String srcValue;
    Calculator calculator = new Calculator(registers,memory);

    //TODO Need to set value into lower 64-bit of XMM Register
    //XMM to Memory: Destination-High DWORD should NOT changed
    if(src.isMemory() && des.isRegister()) {
        System.out.println("Memory to Register");
        srcValue = memory.read(src, QWORD);

        String desValue = registers.get(des);
        desValue = calculator.cutToCertainHexSize("getUpper", desValue, QWORD/4);

        srcValue = desValue.concat(srcValue);

        registers.set(des, srcValue);

    //Memory to XMM: Destination-High DWORD cleared to 0
    } else if(src.isRegister() && des.isMemory()) {
        System.out.println("Register to Memory");
        srcValue = registers.get(src);

        // Get the low quad-word of the register
        srcValue = calculator.cutToCertainHexSize("getLower", srcValue, QWORD/4);

        memory.write(des, srcValue, QWORD);
    }
}
