execute(registers, memory) {
    String st0 = registers.get("ST0");
    String first = st0.charAt(0) + "";

    Calculator cal = new Calculator(registers, memory);
    String binaryString  = cal.hexToBinaryString(first, 4);
    
    char[] array = binaryString.toCharArray();
    if ( array[0] == '0' ) {
    	array[0] = '1';
    } else {
    	array[0] = '0';
    }

    Integer number = Integer.parseInt(new String(array), 2);
    st0 = Integer.toHexString(number) + st0.substring(1);
    registers.set("ST0", st0);
}
