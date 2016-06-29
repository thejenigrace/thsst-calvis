execute(des, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);

    String value = registers.x87().pop();
    if ( des.isMemory() ) {
        int size = memory.getBitSize(des);
        if (size == 80 ) {
            // need to convert extended precision to 80 bit BCD
            convertedValue = calculator.convertHexToDoublePrecision(value);
            value = calculator.convertDoublePrecisionToHexString(convertedValue);
        }
        memory.write(des, value, des);
    }
}
