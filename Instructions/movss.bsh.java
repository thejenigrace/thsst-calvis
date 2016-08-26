execute(des,src,registers,memory) {
    int DWORD = 32;
    String srcValue;
    Calculator calculator = new Calculator(registers,memory);

    if(src.isRegister()) {
        srcValue = registers.get(src);
        srcValue = calculator.cutToCertainHexSize("getLower",srcValue,DWORD/4);
    }

    if(des.isRegister() && src.isRegister()) {
        String desValue = registers.get(des);
        desValue = calculator.cutToCertainHexSize("getUpper",desValue,DWORD*3/4);
        srcValue = desValue.concat(srcValue);
        registers.set(des, srcValue);

    } else if(des.isRegister() && src.isMemory()) {
        srcValue = memory.read(src, DWORD);
        registers.set(des, srcValue);
    } else if(des.isMemory() && src.isRegister()) {
        memory.write(des, srcValue, DWORD);
    }
}
