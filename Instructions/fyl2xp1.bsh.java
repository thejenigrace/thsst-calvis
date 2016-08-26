execute(registers, memory) {
    String valueZero = registers.get("ST0");
    String valueOne = registers.get("ST1");
    Calculator c = new Calculator(registers, memory);

    double regValZero = Double.parseDouble(valueZero);
    double regValOne = Double.parseDouble(valueOne);
    double resultVal = 0.0;

    if( -(1 - Math.sqrt(2)/2 ) > regValZero || (1 - Math.sqrt(2)/2 ) < regValZero) {
        resultVal = Double.NaN;
    }else{
        resultVal = regValOne * (Math.log(regValZero + 1.0) / Math.log(2) );
    }

    boolean isException = c.generateFPUExceptions(registers, resultVal, "ST1");
    if(!isException) {
        registers.set("ST1", resultVal + "");
    }
    registers.x87().status().set("C3",'0');
    registers.x87().status().set("C0",'0');
    registers.x87().status().set("C2",'0');
    registers.x87().pop();
}
