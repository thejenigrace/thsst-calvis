execute(des,src,registers,memory) {
    int QWORD = 64;
    int DQWORD = 128;
    String srcValue;
    Calculator calculator = new Calculator(registers,memory);

    if(src.isMemory() && des.isRegister()) {
        srcValue = memory.read(src, QWORD);
        String desValue = registers.get(des);
        desValue = calculator.cutToCertainHexSize("getLower", desValue, QWORD/4);
        srcValue = srcValue.concat(desValue);
        registers.set(des, srcValue);
    } else if(src.isRegister() && des.isMemory()) {
        srcValue = registers.get(src);
        srcValue = calculator.cutToCertainHexSize("getLower", srcValue, QWORD/4);
        String desValue = memory.read(des, DQWORD);
        desValue = calculator.cutToCertainHexSize("getLower", desValue, QWORD/4);
        srcValue = srcValue.concat(desValue);
        memory.write(des, srcValue, DQWORD);
    }
}
