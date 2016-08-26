execute(registers, memory) {
    /*String st0 = registers.get("ST0");
       // st0 = 2 ^ (st0) - 1
       registers.set("ST0", st0);*/
    String valueZero = registers.get("ST0");
    String valueOne = registers.get("ST1");
    Calculator c = new Calculator(registers, memory);

    double regValZero = Double.parseDouble(valueZero);
    double regValOne = Double.parseDouble(valueOne);
    double resultVal = Math.pow(2, regValZero) - 1;
    if(regValZero != 0) {
        registers.x87().status().set("C2",'0');
    }
    else{
        c.setDivideByZeroOperation(registers, regValZero, regValOne, "ST1");
    }
    registers.set("ST1", resultVal + "");
    registers.x87().status().set("C3",'0');
    registers.x87().status().set("C0",'0');
    registers.x87().status().set("C2",'0');

}
