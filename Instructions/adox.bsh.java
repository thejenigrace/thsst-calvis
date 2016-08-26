execute(des, src, registers, memory) {
    int DWORD = 32;
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

    // Addition of Unsigned plus Overflow Flag
    BigInteger biDesValue = new BigInteger(desValue, 16);
    BigInteger biSrcValue = new BigInteger(srcValue, 16);
    BigInteger biResult = biDesValue.add(biSrcValue);




    // Check if Overflow Flag == 1
    if ( eFlags.getOverflowFlag().equals("1") ) {
        BigInteger biAddPlusOne = BigInteger.valueOf(new Integer(1).intValue());
        biResult = biResult.add(biAddPlusOne);


    }



    // Checks the length of the result to fit the destination
    if ( biResult.toString(16).length() > DWORD/4 ) {
        registers.set( des, biResult.toString(16).substring(1) );
    } else {
        registers.set( des, biResult.toString(16) );
    }

    // Checks if Overflow Flag is affected
    String desBinaryValue = biDesValue.toString(2);
    String srcBinaryValue = biSrcValue.toString(2);
    String resultBinaryValue = biResult.toString(2);
    char desMSB = calculator.binaryZeroExtend(desBinaryValue, des).charAt(0);
    char srcMSB = calculator.binaryZeroExtend(srcBinaryValue, src).charAt(0);
    char resultMSB = calculator.binaryZeroExtend(resultBinaryValue, des).charAt(0);

    String overflowFlag = calculator.checkOverflowAddWithFlag( srcMSB, desMSB, resultMSB, eFlags.getOverflowFlag() );
    eFlags.setOverflowFlag( overflowFlag );
}
