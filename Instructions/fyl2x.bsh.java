execute(registers, memory) {
    String valueZero = registers.get("ST0");
    String valueOne = registers.get("ST1");
    Calculator c = new Calculator(registers, memory);

    double regValZero = Double.parseDouble(valueZero);
    double regValOne = Double.parseDouble(valueOne);
    double resultVal = 0.0;
    if(regValZero == 0.0 && registers.x87().control().getFlag("ZM") == 1) {
        if(regValOne >= 0) {
            resultVal = Double.NEGATIVE_INFINITY;
        }
        else{
            resultVal = Double.POSITIVE_INFINITY;
        }
    }else{
        resultVal = regValOne * (Math.log(regValZero) / Math.log(2) );
    }

    boolean isException = c.generateFPUExceptions(registers, resultVal, "ST1");
    boolean isZero = Double.parseDouble(registers.get("ST0")) == 0;

    if(!isException && !isZero) {

        registers.set("ST1", resultVal + "");
    }
    registers.x87().status().set("C3",'0');
    registers.x87().status().set("C0",'0');
    registers.x87().status().set("C2",'0');
    registers.x87().pop();
}
