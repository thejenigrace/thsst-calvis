execute(des,src,registers,memory) {
    int desBitSize = registers.getBitSize(des);
    String desValue = registers.get(des);

    if( src.isRegister() ) {
        System.out.println("ADCX register to register");
        String srcValue = registers.get(src);

        addWithCarry(register,memory,desValue,srcValue);
    } else if( src.isMemory() ) {
        System.out.println("ADCX memory to register");
        String srcValue = memory.read(src, desBitSize);

        addWithCarry(register,memory,desValue,srcValue);
    }
}

addWithCarry(registers,memory,desValue,srcValue) {
    Calculator calculator = new Calculator(register,memory);
    EFlags eFlags = register.getEFlags();

    // Addition in Hex Format
    BigInteger biDesValue = new BigInteger(desValue,16);
    BigInteger biSrcValue = new BigInteger(srcValue,16);
    BigInteger biResult = biSrcValue.add(biDesValue);

    if ( eFlags.getCarryFlag().equals("1") ) {
        BigInteger biCarryFlag = biCarryFlag.valueOf(new Integer(1).intValue());
        biResult = biResult.add(biCarryFlag);
    }

    String resultingValue = "";
    if(biResult.toString(16).length() > registers.getHexSize(src)) {
        resultingValue = biResult.toString(2).substring(1);
    } else {
        resultingValue = biResult.toString(2);
    }

    // Set the content of the destination
    register.set(des,
    calculator.binaryToHexString(calculator.binaryZeroExtend(resultingValue,des),des));

    // Check the Carry Flag
    BigInteger biCompare = new BigInteger("FFFFFFFF", 16);
    if(biResult.compareTo(biCompare) == 1)
        eFlags.setCarryFlag("1");
    else
        eFlags.setCarryFlag("0");
}
