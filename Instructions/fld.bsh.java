execute(src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    
    if ( src.isRegister() ) {
        if ( registers.getBitSize(src) == 80 ) {
            String value = registers.get(src);
            registers.x87().push(value);

            System.out.println("value: " + value);
        }
    }
    else if ( src.isMemory() ) {
        int size = memory.getBitSize(src);
        String value = memory.read(src, size);

        double convertedValue;

        System.out.println("value: " + value);

        if ( size == 32 ) {
            // conversion to extended precision
            convertedValue = calculator.convertHexToSinglePrecision(value);
            value = calculator.convertSinglePrecisionToHexString(convertedValue);
        } else if ( size == 64 ) {
            // conversion
            convertedValue = calculator.convertHexToDoublePrecision(value);
            value = calculator.convertDoublePrecisionToHexString(convertedValue);
        } else if ( size == 80 ) {
            // conversion
            convertedValue = calculator.convertHexToDoublePrecision(value);
            value = calculator.convertDoublePrecisionToHexString(convertedValue);
        }

        registers.x87().push(value);
    }
}
