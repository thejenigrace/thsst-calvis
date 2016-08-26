execute(des, src, registers, memory) {
    int DWORD = 32;
    String maxHexValue = "FFFFFFFF";
    // Destination is always r32
    String desValue = registers.get(des);
    String srcValue = "";
    // Check if the src is either r32 or m32
    if ( src.isRegister() ) {

        srcValue = registers.get(src);
    } else if ( src.isMemory() ) {

        srcValue = memory.read(src, DWORD);
    }

    Calculator calculator = new Calculator(registers, memory);
    EFlags eFlags = registers.getEFlags();

    // Addition of Unsigned plus Carry Flag
    BigInteger biDesValue = new BigInteger(desValue, 16);
    BigInteger biSrcValue = new BigInteger(srcValue, 16);
    BigInteger biResult = biDesValue.add(biSrcValue);

    // Check if Carry Flag == 1
    if ( eFlags.getCarryFlag().equals("1") ) {
        BigInteger biAddPlusOne = BigInteger.valueOf(new Integer(1).intValue());
        biResult = biResult.add(biAddPlusOne);
    }

    // Checks the length of the result to fit the destination
    if ( biResult.toString(16).length() > DWORD/4 ) {
        registers.set( des, biResult.toString(16).substring(1) );
    } else {
        registers.set( des, biResult.toString(16) );
    }

    // Checks if Carry Flag is affected when resultValue > FFFFFFFF
    BigInteger biCF = new BigInteger(maxHexValue, 16);
    if ( biResult.compareTo(biCF) == 1 ) {
        eFlags.setCarryFlag("1");
    } else {
        eFlags.setCarryFlag("0");
    }
}
