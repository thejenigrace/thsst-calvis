execute(des,src,registers,memory) {
    int srcBitSize;
    int srcHexSize;
    String srcValue;
    String desValue = registers.get(des);
    Calculator calculator = new Calculator(registers,memory);

    if (src.isRegister()) {
        srcBitSize = registers.getBitSize(src);
        srcHexSize = registers.getHexSize(src);
        srcValue = registers.get(src);
    } else if (src.isMemory()) {
        srcBitSize = memory.getBitSize(src);
        srcHexSize = memory.getHexSize(src);
        srcValue = memory.read(src,srcBitSize);
    }

    // src == mm || src == xmm
    if (srcBitSize == 64 || srcBitSize == 128) {
        String result = calculator.cutBySizeAndCompare(desValue, srcValue, srcHexSize, 8, 'e');
        registers.set(des, result);
    }
}
