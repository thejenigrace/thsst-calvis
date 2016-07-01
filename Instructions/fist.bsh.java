execute(des, registers, memory) throws Exception {
    String value = registers.x87().peek();
    if ( des.isMemory() ) {
        int size = memory.getBitSize(des); 

        //
        Double alpha = Double.parseDouble(value);
        Long l = alpha.longValue();
        String hex = Long.toHexString(l);

        while ( hex.length() % 4 != 0 ) {
            hex = "0" + hex;
        }
        //

        if (size == 16 ) {
            // need to convert extended precision to 16 bit integer
            if ( l > 32767 || l < -32768) {
                throw new InvalidArithmeticOperandException(l, size);
            } else {
                if ( hex.length() == 4 ) {
                    value = hex;
                } else {
                    value = hex.substring(hex.length() - 4);
                }
                memory.write(des, value, des);
            }
        }
        else if ( size == 32 ) {
            // need to convert extended precision to 32 bit integer
            if ( l.compareTo(2147483647L) == 1 || l.compareTo(-2147483648L) == -1) {
                throw new InvalidArithmeticOperandException(l, size);
            } else {
                if ( hex.length() == 8 ) {
                    value = hex;
                } else {
                    value = hex.substring(hex.length() - 8);
                }
                memory.write(des, value, des);
            }
        }
        
    }
}
