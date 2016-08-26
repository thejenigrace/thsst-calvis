execute(registers, memory) {
    String value = registers.get("ST0");
    Calculator c = new Calculator(registers, memory);
    double regVal = Double.parseDouble(value);
    double resultVal = Math.cos(regVal);

    if ( resultVal > Math.pow(2,64) || resultVal < Math.pow(2, 64) * -1 ) {
        registers.x87().status().set("C2",'1');
        registers.x87().status().set("C1",'0');
    } else if( c.isNan(resultVal) || c.isInfinite(resultVal) ) {
        c.setInvalidOperation(registers, "ST0");
    } else {
        registers.x87().status().set("C2",'0');
        String hexConvertedVal = "" + (resultVal);
        registers.set("ST0", hexConvertedVal);
    }
    registers.x87().status().set("C3",'0');
    registers.x87().status().set("C0",'0');
}
