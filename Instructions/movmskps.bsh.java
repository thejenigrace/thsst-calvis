execute(des,src,registers,memory) {
    int DWORD = 32;
    Calculator calculator = new Calculator(registers,memory);

    //TODO Need to set value into high 64-bit of XMM Register
    //XMM to XMM: Destination-High DWORD should NOT changed
    if(src.isRegister() && des.isRegister()) {
        String desValue = registers.get(des);
        String srcValue = registers.get(src);

        Long longValue = Long.parseLong(srcValue,16);
        String binaryValue = Long.toBinaryString(longValue);
        srcValue = "";
        srcValue = srcValue.concat(binaryValue.charAt(127));
        srcValue = srcValue.concat(binaryValue.charAt(95));
        srcValue = srcValue.concat(binaryValue.charAt(63));
        srcValue = srcValue.concat(binaryValue.charAt(31));

        int missingZeroes = DWORD - srcValue.length();
        for(int i = 0; i < missingZeroes; i++) {
            srcValue = "0" + srcValue;
        }

        registers.set(des, srcValue);
    }
}
