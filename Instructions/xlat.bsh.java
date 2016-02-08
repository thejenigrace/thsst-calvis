execute(registers, memory) {
	//get contents of registers BX and AL
	int maxSize = RegisterList.MAX_SIZE;

	String BX = registers.get("BX");
	String AL = registers.get("AL");

	switch ( maxSize ) {
		case 32:
			BX = registers.get("EBX");
			break;
	}

	// Add BX and AL and store in temp variable result
	BigInteger bx = new BigInteger(BX, 16);
	BigInteger al = new BigInteger(AL, 16);
	BigInteger result = bx.add(al);

	String hexStringResult = result.toString(16);
	hexStringResult = memory.reformatAddress(hexStringResult);
	System.out.println("XLAT result: " + hexStringResult);

	// memory byte to AL
	hexStringResult = memory.read(hexStringResult, 8);

	// finally set the new value of AL
	registers.set("AL", hexStringResult);

}