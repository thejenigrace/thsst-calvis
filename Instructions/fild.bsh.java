execute(src, registers, memory) {
    if ( src.isMemory() ) {
        int size = memory.getBitSize(src);
        String value = memory.read(src, size);

        BigInteger convertedValue = new BigInteger(value, 16);
        String binaryValue = convertedValue.toString(2);

        // make the binary value's length divisble by 4 
        while ( binaryValue.length() % 4 != 0 ) {
            binaryValue = "0" + binaryValue;
        }

        // get the sign of the binary value
        String sign = binaryValue.charAt(0) + "";

        // sign extend to 64 bits
        while ( binaryValue.length() < 64 ) {
            binaryValue = sign + binaryValue;
        }

        long l = new BigInteger(binaryValue, 2).longValue();

        registers.x87().push(l + "");
    }
}
