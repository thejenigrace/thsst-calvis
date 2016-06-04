execute(src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    
    if ( src.isMemory() ) {
        int size = memory.getBitSize(src);
        String value = memory.read(src, size);
        double convertedValue;

        if ( size == 16 ) {
            // conversion to extended precision

        } else if ( size == 32 ) {
            // conversion
            convertedValue = calculator.convertHexToSinglePrecision(value);
            value = calculator.convertSinglePrecisionToHexString(convertedValue);

        } else if ( size == 64 ) {
            // conversion
            convertedValue = calculator.convertHexToDoublePrecision(value);
            value = calculator.convertDoublePrecisionToHexString(convertedValue);
        }
        registers.x87().push(value);
    }
}
