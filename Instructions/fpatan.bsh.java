execute(registers, memory) {
    String valueZero = registers.get("ST0");
    String valueOne = registers.get("ST1");
    Calculator c = new Calculator(registers, memory);
    double regValZero = Double.parseDouble(valueZero);
    double regValOne = Double.parseDouble(valueOne);
    double resultVal = Math.atan(regValOne/regValZero);

    if(resultVal < Math.pow(2, 64) * -1 || resultVal < Math.pow(2, 64) * -1) {
        c.generateFPUExceptions(registers, resultVal, "ST1");
    }
    else{
        registers.set("ST1", resultVal + "");
    }
    registers.x87().status().set("C2",'0');
    registers.x87().status().set("C3",'0');
    registers.x87().status().set("C0",'0');
    registers.x87().status().set("C2",'0');
    registers.x87().pop();
}
