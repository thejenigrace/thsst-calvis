execute(registers, memory) {
    Calculator c = new Calculator(registers, memory);
    double st0 = Double.parseDouble(registers.get("ST0"));
    double st1 = Double.parseDouble(registers.get("ST1"));
    double quotient = st0 / st1;
    double modulo = (st0 % st1);
    double computation = st0 - (Math.floor(quotient) * st1);
    if(modulo == 0) {
        int sign = (int) Math.signum(st0);
        registers.set("ST0", (modulo * sign) + "");
    }
    else if(c.isInfinite(st1)) {
        registers.set("ST0",  st0 + "");
    }
    else{
        registers.set("ST0", modulo + "");
    }

    String HexResultOne = c.convertDoublePrecisionToHexString(quotient);
    char bitResultOne = c.binaryZeroExtend(new BigInteger(HexResultOne, 16).toString(2), 80).charAt(79 - 1);
    char bitResultTwo = c.binaryZeroExtend(new BigInteger(HexResultOne, 16).toString(2), 80).charAt(79 - 2);
    char bitResultZero = c.binaryZeroExtend(new BigInteger(HexResultOne, 16).toString(2), 80).charAt(79);

    registers.x87().status().set("C3", bitResultTwo);
    registers.x87().status().set("C2", '0');
    registers.x87().status().set("C1", '0');
    registers.x87().status().set("C0", bitResultTwo);

}
