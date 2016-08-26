execute(registers, memory) {
    String st0 = registers.get("ST0");
    String value = registers.get("ST0");
    Calculator c = new Calculator(registers, memory);
    double regVal = Double.parseDouble(value);
    double resultCosVal = Math.cos(regVal);
    double resultSinVal = Math.sin(regVal);

    if( (resultCosVal > Math.pow(2,64) || resultCosVal < Math.pow(2, 64) * -1) ||
        (resultSinVal > Math.pow(2,64) || resultSinVal < Math.pow(2, 64) * -1)) {
        registers.x87().status().set("C2",'1');
        if(resultCosVal < Math.pow(2, 64) * -1 ||
           resultSinVal < Math.pow(2, 64) * -1) {
            registers.x87().status().set("UE", '1');
        }
    }
    else{
        registers.x87().status().set("C2",'0');
        registers.x87().pop();
        registers.x87().push(resultCosVal + "");
        registers.x87().push(resultSinVal + "");
    }
    registers.x87().status().set("C3",'0');
    registers.x87().status().set("C0",'0');

}
