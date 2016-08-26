execute(registers, memory) {
    Calculator c = new Calculator(registers, memory);
    String st0 = registers.get("ST0");
    String st1 = registers.get("ST1");
    // scale st0 by st1
    // st0 = st0 * 2 ^ roundtowardszero(st1)
    double exponent = Double.parseDouble(st1);
    double mantissa = Double.parseDouble(st0);
    double resultPow = Math.pow(2, exponent.intValue());
    double resultingAnswer = resultPow * mantissa;
    boolean isException = c.generateFPUExceptions(registers, resultingAnswer);
    if(!isException) {
        registers.set("ST0", resultingAnswer + "");
    }
    registers.x87().status().set("C3",'0');
    registers.x87().status().set("C0",'0');
}
