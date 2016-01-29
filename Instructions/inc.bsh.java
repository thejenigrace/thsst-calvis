execute(des, registers, memory) {
 	if ( des.isRegister() ){
        System.out.println("des = " + registers.get(des));
        String x = registers.get(des);

        Calculator c = new Calculator(registers, memory);
        EFlags ef = registers.getEFlags();

        x = c.hexToBinaryString(x, des);
        int result = Integer.parseInt(x, 2) + 1;
        registers.set(des, c.binaryToHexString(Integer.toBinaryString(result), des));

        // Debugging
        System.out.println("x = " + Integer.parseInt(x, 2));
        System.out.println("result = " + Integer.toBinaryString(result));
 	}
// 	else if ( des.isMemory() ){
//
// 	}
 }