execute(des, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    
    String value = registers.x87().peek();
    double convertedValue;

    if ( des.isMemory() ) {
        int size = memory.getBitSize(des);
        if (size == 16 ) {
            // need to convert extended precision to 16 bit integer
            value = value.substring(16);
        }
        else if ( size == 32 ) {
            // need to convert extended precision to 32 bit integer
            convertedValue = calculator.convertHexToSinglePrecision(value);
            value = calculator.convertSinglePrecisionToHexString(convertedValue);

            value = value.substring(12);
        }
        memory.write(des, value, des);
    }
}
