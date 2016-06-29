execute(des,src,registers,memory) {
    int DWORD = 32;
    int HEX_DWORD = 8;
    Calculator calculator = new Calculator(registers,memory);

    //XMM to XMM: Upper-bits of the DESTINATION operand beyond the mask are filled with zeroes
    if(src.isRegister() && des.isRegister()) {
        String srcValue = registers.get(src);

        System.out.println("127: " + srcValue.substring(0,1));
        System.out.println("95: " + srcValue.substring(8,9));
        System.out.println("63: " + srcValue.substring(16,17));
        System.out.println("31: " + srcValue.substring(24,25));

        Long long127 = Long.parseLong(srcValue.substring(0,1), 16);
        Long long95 = Long.parseLong(srcValue.substring(8,9), 16);
        Long long63 = Long.parseLong(srcValue.substring(16,17), 16);
        Long long31 = Long.parseLong(srcValue.substring(24,25), 16);

        // Long longSrcValue = Long.parseLong(srcValue,16);
        // String binarySrcValue = Long.toBinaryString(longSrcValue);

        String binary127 = Long.toBinaryString(long127);
        String binary95 = Long.toBinaryString(long95);
        String binary63 = Long.toBinaryString(long63);
        String binary31 = Long.toBinaryString(long31);

        System.out.println(calculator.binaryZeroExtend(binary127, 4));
        System.out.println(calculator.binaryZeroExtend(binary95, 4));
        System.out.println(calculator.binaryZeroExtend(binary63, 4));
        System.out.println(calculator.binaryZeroExtend(binary31, 4));

        srcValue = "";
        srcValue = srcValue.concat(calculator.binaryZeroExtend(binary127, 4).substring(0,1));
        srcValue = srcValue.concat(calculator.binaryZeroExtend(binary95, 4).substring(0,1));
        srcValue = srcValue.concat(calculator.binaryZeroExtend(binary63, 4).substring(0,1));
        srcValue = srcValue.concat(calculator.binaryZeroExtend(binary31, 4).substring(0,1));

        Long longSrcValue = Long.parseLong(srcValue, 2);

        // int missingZeroes = HEX_DWORD - desValue.length();
        // for(int i = 0; i < missingZeroes; i++) {
        //     desValue = "0" + desValue;
        // }

        // Set the reg32 value
        registers.set(des, Long.toHexString(longSrcValue));
    }
}
