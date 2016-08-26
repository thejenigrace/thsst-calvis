execute(des,src,registers,memory) {
    int QWORD = 64;
    Calculator calculator = new Calculator(registers,memory);

    if(src.isRegister() && des.isRegister()) {
        String desValue = registers.get(des);
        String srcValue = registers.get(src);

        desValue = calculator.cutToCertainHexSize("getLower", desValue, QWORD/4);
        srcValue = calculator.cutToCertainHexSize("getLower", srcValue, QWORD/4);

        srcValue = srcValue.concat(desValue);

        registers.set(des, srcValue);
    }
}
