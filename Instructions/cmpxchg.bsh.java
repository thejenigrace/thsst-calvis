execute(des, src, registers, memory) {
		System.out.println("wtf");
		if(src.isRegister()){
		String EAXRegister;
		int size = registers.getBitSize(src);
		switch(size){
		case 8:
		EAXRegister = registers.get("AL");
		break;
		case 16:
		EAXRegister = registers.get("AX");
		break;
		case 32:
		EAXRegister = registers.get("EAX");
		break;
		}
		if ( des.isRegister() ){
		if(des.isRegister() && registers.getBitSize(des) == size){
		if(isEqual(des, src, registers, memory, EAXRegister)){
		registers.set(des, registers.get(src));
		}
		else{
		switch(size){
		case 8:
		EAXRegister = registers.get("AL");
		registers.set("AL", registers.get(des));
		break;
		case 16:
		EAXRegister = registers.get("AX");
		registers.set("AX", registers.get(des));
		break;
		case 32:
		EAXRegister = registers.get("EAX");
		registers.set("EAX", registers.get(des));
		break;
		}
		}
		}
		}
		if ( des.isMemory() ){
		if(des.isMemory() ){
		if(isEqual(des, src, registers, memory, EAXRegister)){
		memory.write(des, registers.get(src), size);
		}
		else{
		switch(size){
		case 8:
		EAXRegister = registers.get("AL");
		registers.set("AL", memory.read(des, size));
		break;
		case 16:
		EAXRegister = registers.get("AX");
		registers.set("AX", memory.read(des, size));
		break;
		case 32:
		EAXRegister = registers.get("EAX");
		registers.set("EAX", memory.read(des, size));
		break;
		}
		}
		}
		}
		}
		}



		boolean isEqual(des, src, registers, memory, eaxReg){
		int desSize = registers.getBitSize(src);

		Calculator calculator = new Calculator(registers, memory);
		BigInteger i = new BigInteger(eaxReg, 16);
		String EAXRegister = i.toString(2);
		int missingZeroesEAX = desSize - EAXRegister.length();
		String destination = "";

		if(des.isRegister())
		destination = calculator.hexToBinaryString(registers.get(des), des);
		else
		destination = calculator.hexToBinaryString(memory.read(des, desSize), des);


		destination = zeroExtend(destination, desSize);
		EAXRegister = zeroExtend(EAXRegister, desSize);
		System.out.println(destination);
		System.out.println(EAXRegister);
		String result = "";
		int r = 0;
		int borrow = 0;
		int carry = 0;
		int overflow = 0;

		for(int i = desSize - 1; i >= 0; i--) {
		r = Integer.parseInt(String.valueOf(destination.charAt(i))) - Integer.parseInt(String.valueOf(EAXRegister.charAt(i))) - borrow;
		System.out.println(EAXRegister);
		if( r < 0 ) {
		borrow = 1;
		r += 2;
		result = result.concat(r.toString());

		if( i == 0 ) {
		carry = 1;
		}
		if( i == 0 || i == 1) {
		overflow++;
		}
		}
		else {
		borrow = 0;
		result = result.concat(r.toString());
		}
		}

		String d = new StringBuffer(result).reverse().toString();
		System.out.println(d + " result");
		BigInteger compareToZero = new BigInteger(d, 2);
		//FLAGS
		EFlags flags = registers.getEFlags();
		flags.setCarryFlag(carry.toString());
		System.out.println("123sdsd");
		if(overflow == 1) {
		flags.setOverflowFlag("1");
		}
		else {
		flags.setOverflowFlag("0");
		}
		String sign = "" + compareToZero.toString(2).charAt(0);
		flags.setSignFlag(sign);
		String parity = calculator.checkParity(compareToZero.toString(2));
		flags.setParityFlag(parity);

		String auxiliary = calculator.checkAuxiliarySub(destination, EAXRegister);
		flags.setAuxiliaryFlag(auxiliary);
		if(d.matches("[0]+")) {
		flags.setZeroFlag("1");
		return true;
		}
		else {
		flags.setZeroFlag("0");
		return false;
		}
		}

		String zeroExtend(str, size){
		int missingZeroes = size - str.length();
		for(int x = 0; x < missingZeroes; x++){
		str = "0" + str;
		}
		return str;
		}
 /*
 	CONCERN: Where do we put the logic for CC
 */