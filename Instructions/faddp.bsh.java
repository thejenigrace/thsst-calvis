execute(des, src, registers, memory) {
    Calculator c = new Calculator(registers, memory);
    if ( des.isRegister() && src.isRegister() ) {
        if ( registers.getBitSize(des) == registers.getBitSize(src) ) {
            String desValue = registers.get(des);
            String srcValue = registers.get(src);

            if(src.getValue().equals("ST0")) {

            }
            else{
                throw new IncorrectParameterException("FADDP");
            }

            double dbDes = Double.parseDouble(desValue);
            double dbSrc = Double.parseDouble(srcValue);

            double resultingValue = dbSrc + dbDes;
            boolean isException = c.generateFPUExceptions(registers, resultingValue, des.getValue());
            if (!isException) {
                registers.set(des.getValue(), "" + resultingValue);
            }

            registers.x87().status().set("C3",'0');
            registers.x87().status().set("C2",'0');
            registers.x87().status().set("C0",'0');
            registers.x87().pop();
        }
    }
}

execute(registers, memory) {
    Calculator c = new Calculator(registers, memory);
    String desValue = registers.get("ST1");
    String srcValue = registers.get("ST0");
    double dbDes = Double.parseDouble(desValue);
    double dbSrc = Double.parseDouble(srcValue);

    double resultingValue = dbSrc + dbDes;
    boolean isException = c.generateFPUExceptions(registers, resultingValue, "ST1");
    if (!isException) {
        registers.set("ST1", Double.toString(resultingValue));
    }

    registers.x87().status().set("C3",'0');
    registers.x87().status().set("C2",'0');
    registers.x87().status().set("C0",'0');
    registers.x87().pop();


}
