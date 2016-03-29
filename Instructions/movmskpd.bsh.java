execute(des,src,registers,memory) {
    int DWORD = 32;
    Calculator calculator = new Calculator(registers,memory);

    //TODO Need to set value into high 64-bit of XMM Register
    //XMM to REG32: Destination-High DWORD should NOT changed
    if(src.isRegister() && des.isRegister()) {
        String desValue = registers.get(des);
        String srcValue = registers.get(src);

        String firstHex = "" + srcValue.charAt(24);
        String secondHex = "" + srcValue.charAt(0);

        Long longFirstHex = Long.parseLong(firstHex, 16);
        String binaryFirstHex = Long.toBinaryString(longFirstHex);

        Long longSecondHex = Long.parseLong(secondHex, 16);
        String binarySecondHex = Long.toBinaryString(longSecondHex);

        // Long longValue = Long.parseLong(srcValue,16);
        // BigInteger biValue = new BigInteger(srcValue, 16);
        // String binaryValue = Long.toBinaryString(longValue);
        // String binaryValue = biValue.toString(2);
        srcValue = "";
        srcValue = srcValue.concat("" + binaryFirstHex.charAt(0));
        srcValue = srcValue.concat("" + binarySecondHex.charAt(0));

        int missingZeroes = DWORD/4 - srcValue.length();
        for(int i = 0; i < missingZeroes; i++) {
            srcValue = "0" + srcValue;
        }

        registers.set(des, srcValue);
    }
}
