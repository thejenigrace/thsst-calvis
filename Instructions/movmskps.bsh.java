execute(des,src,registers,memory) {
    int DWORD = 32;
    Calculator calculator = new Calculator(registers,memory);

    //TODO Need to set value into high 64-bit of XMM Register
    //XMM to XMM: Destination-High DWORD should NOT changed
    if(src.isRegister() && des.isRegister()) {
        String desValue = registers.get(des);
        String srcValue = registers.get(src);

        String srcBin = calculator.binaryZeroExtend(new BigInteger(srcValue, 16).toString(2), 128);
        srcValue = "";
        srcValue = Character.toString(srcBin.charAt(127)) + Character.toString(srcBin.charAt(95))
		+ Character.toString(srcBin.charAt(63)) + Character.toString(srcBin.charAt(31));

		System.out.println(srcValue + " wow");
		srcValue = calculator.binaryZeroExtend(srcValue, 32);

        registers.set(des, calculator.hexZeroExtend(new BigInteger(srcValue, 2).toString(2), des));
    }
}
