execute(registers, memory) {
    String st0 = registers.get("ST0");
    
    // examine st0
    String first = st0.charAt(0) + "";

    Calculator cal = new Calculator(registers, memory);
    String binaryString  = cal.hexToBinaryString(first, 4);
    
    char[] array = binaryString.toCharArray();
    registers.x87().set("C1", array[0]);

    switch ( st0 ) {
    	case "Unsupported":
    	    registers.x87().set("C3", '0');
    	    registers.x87().set("C2", '0');
    	    registers.x87().set("C0", '0');
    	    break;
    	case "NaN":
    	    registers.x87().set("C3", '0');
    	    registers.x87().set("C2", '0');
    	    registers.x87().set("C0", '1');
    	    break;
        case "Normal Finite Number":
    	    registers.x87().set("C3", '0');
    	    registers.x87().set("C2", '1');
    	    registers.x87().set("C0", '0');
    	    break;
    	case "Infinity":
    	    registers.x87().set("C3", '0');
    	    registers.x87().set("C2", '1');
    	    registers.x87().set("C0", '1');
    	    break;
    	case "00000000000000000000": //Zero":
    	    registers.x87().set("C3", '1');
    	    registers.x87().set("C2", '0');
    	    registers.x87().set("C0", '0');
    	    break;
    	case "Empty":
    	    registers.x87().set("C3", '1');
    	    registers.x87().set("C2", '0');
    	    registers.x87().set("C0", '1');
    	    break;
    	case "Denormal Number":
    	    registers.x87().set("C3", '1');
    	    registers.x87().set("C2", '1');
    	    registers.x87().set("C0", '0');
    	    break;
    }
    
}
