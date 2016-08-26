execute(des, src, registers, memory) {
    Calculator c = new Calculator(registers, memory);
    if ( des.isRegister() && src.isRegister() ) {
        if ( registers.getBitSize(des) == registers.getBitSize(src) ) {
            String desValue = registers.get(des);
            String srcValue = registers.get(src);

            if( !src.getValue().equals("ST0")) {
                throw new IncorrectParameterException("FSUBRP");
            }

            double dbDes = Double.parseDouble(desValue);
            double dbSrc = Double.parseDouble(srcValue);

            double resultingValue =  dbSrc - dbDes;
            boolean isException = c.generateFPUExceptions(registers, resultingValue, des.getValue());
            if(!isException) {
                registers.set(des.getValue(), "" + resultingValue);
            }

            registers.x87().status().set("C3",'0');
            registers.x87().status().set("C2",'0');
            registers.x87().status().set("C0",'0');
        }
    }
}


execute(registers, memory) {
    Calculator c = new Calculator(registers, memory);
    double dbDes = Double.parseDouble(registers.get("ST1"));
    double dbSrc = Double.parseDouble(registers.get("ST0"));

    double resultingValue = dbSrc - dbDes;
    boolean isException = c.generateFPUExceptions(registers, resultingValue, "ST1");
    if(!isException) {

        registers.set("ST1", resultingValue + "");
    }

    registers.x87().status().set("C3",'0');
    registers.x87().status().set("C2",'0');
    registers.x87().status().set("C0",'0');
    registers.x87().pop();
}
