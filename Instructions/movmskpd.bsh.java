execute(des,src,registers,memory) {
    int DWORD = 32;
    Calculator calculator = new Calculator(registers,memory);

    //TODO Need to set value into high 64-bit of XMM Register
    //XMM to REG32: Destination-High DWORD should NOT changed
    if(src.isRegister() && des.isRegister()) {
        String desValue = registers.get(des);
        String srcValue = registers.get(src);

        String hex127 = "" + srcValue.charAt(0);
        String hex63 = "" + srcValue.charAt(16);

        Long long127 = Long.parseLong(hex127, 16);
        String binary127 = Long.toBinaryString(long127);

        Long long63 = Long.parseLong(hex63, 16);
        String binary63 = Long.toBinaryString(long63);

        // Long longValue = Long.parseLong(srcValue,16);
        // BigInteger biValue = new BigInteger(srcValue, 16);
        // String binaryValue = Long.toBinaryString(longValue);
        // String binaryValue = biValue.toString(2);
        srcValue = "";
        srcValue = srcValue.concat(calculator.binaryZeroExtend(binary127, 4).substring(0,1));
        srcValue = srcValue.concat(calculator.binaryZeroExtend(binary63, 4).substring(0,1));

        Long longSrcValue = Long.parseLong(srcValue, 2);

        registers.set(des, Long.toHexString(longSrcValue));
    }
}
