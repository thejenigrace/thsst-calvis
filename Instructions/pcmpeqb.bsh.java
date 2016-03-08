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
        // e == equal
        String result = calculator.cutBySizeAndCompare(desValue, srcValue, desHexSize, 2, 'e');
        registers.set(des, result);
    }
}
