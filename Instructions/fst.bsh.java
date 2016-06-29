execute(des, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    
    String value = registers.x87().peek();
    if ( des.isRegister() ) {
        if ( registers.getBitSize(des) == 80 ) {
            registers.set(des, value);
        }
    }
    else if ( des.isMemory() ) {
        int size = memory.getBitSize(des);
        double convertedValue;

        if ( size == 32 ) {
            // conversion to single precision
            convertedValue = calculator.convertHexToSinglePrecision(value);
            value = calculator.convertSinglePrecisionToHexString(convertedValue);

            value = value.substring(12);

        } else if ( size == 64 ) {
            // conversion to double precision
            convertedValue = calculator.convertHexToDoublePrecision(value);
            value = calculator.convertDoublePrecisionToHexString(convertedValue);

            value = value.substring(4);
        }
        memory.write(des, value, des);
    }
}
