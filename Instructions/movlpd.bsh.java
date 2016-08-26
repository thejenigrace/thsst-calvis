execute(des,src,registers,memory) {
    int QWORD = 64;
    String srcValue;
    Calculator calculator = new Calculator(registers,memory);

    if(src.isMemory() && des.isRegister()) {
        srcValue = memory.read(src, QWORD);
        String desValue = registers.get(des);
        desValue = calculator.cutToCertainHexSize("getUpper", desValue, QWORD/4);
        srcValue = desValue.concat(srcValue);
        registers.set(des, srcValue);
    } else if(src.isRegister() && des.isMemory()) {
        srcValue = registers.get(src);
        srcValue = calculator.cutToCertainHexSize("getLower", srcValue, QWORD/4);
        memory.write(des, srcValue, QWORD);
    }
}
