execute(des,src,registers,memory) {
    int desBitSize = registers.getBitSize(des);
    int desHexSize = registers.getHexSize(des);
    String desValue = registers.get(des);
    String srcValue;
    Calculator calculator = new Calculator(registers,memory);

    if (src.isRegister()) {
        srcValue = registers.get(src);
    } else if (src.isMemory()) {
        srcValue = memory.read(src,desBitSize);
    }


    if (desBitSize == 64 || desBitSize == 128) {
        // g == greater than
        String result = calculator.cutBySizeAndCompare(desValue, srcValue, desHexSize, 4, 'g');
        registers.set(des, result);
    }
}
