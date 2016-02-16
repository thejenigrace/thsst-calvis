execute(des, src, registers, memory) {
	if(des.isRegister()){
		String EAXRegister;
		switch(registers.getBitSize(des)){
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
		if ( src.isRegister() ){
			if(src.isRegister() && registers.getBitSize(src) == registers.getBitSize(des)){
				if(isEqual(des, src, registers, memory, EAXRegister)){

					registers.set(des, registers.get(src));
				}
				else{
					registers.set(des, EAXRegister);

				}
			}
		}
		if ( src.isMemory() ){
			System.out.println("Memory Mode");
			if(src.isMemory() ){
				if(isEqual(des, src, registers, memory, EAXRegister)){
					System.out.println("pasok rinto");
					registers.set(des, memory.read(src, registers.getBitSize(des)));
				}
				else{
		System.out.println("pasok asdasdasds");
					registers.set(des, EAXRegister);
				}
			}
		}
	}
}


boolean isEqual(des, src, registers, memory, eaxReg){
	int desSize = registers.getBitSize(des);

	Calculator calculator = new Calculator(registers, memory);
	int i = Integer.parseInt(eaxReg, 16);
	String EAXRegister = Integer.toBinaryString(i);
	int missingZeroes = desSize - EAXRegister.length();
	for(int x = 0; x < missingZeroes; x++){
		EAXRegister = "0" + EAXRegister;
	}
	String source;
	System.out.println(source = calculator.hexToBinaryString(memory.read(src, desSize), src) + " wtf . . ");
	if(src.isMemory())
		source = calculator.hexToBinaryString(memory.read(src, desSize), src);
	else
		source = calculator.hexToBinaryString(registers.get(src), src);
	String destination = calculator.hexToBinaryString(registers.get(des), des);
	String result = "";

	int r = 0;
	int borrow = 0;
	int carry = 0;
	int overflow = 0;

	for(int i = desSize - 1; i >= 0; i--) {
		r = Integer.parseInt(String.valueOf(destination.charAt(i))) - Integer.parseInt(String.valueOf(EAXRegister.charAt(i))) - borrow;

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
		System.out.println("123123");
	String sign = "" + compareToZero.toString(2).charAt(0);
	flags.setSignFlag(sign);
		System.out.println("asswooo");
	String parity = calculator.checkParity(compareToZero.toString(2));
	flags.setParityFlag(parity);

	String auxiliary = calculator.checkAuxiliarySub(destination, EAXRegister);
	flags.setAuxiliaryFlag(auxiliary);
		System.out.println("awooo");
	if(d.matches("[0]+")) {
		flags.setZeroFlag("1");
		return true;
	}
	else {
		flags.setZeroFlag("0");
		return false;
	}
}
 /*
 	CONCERN: Where do we put the logic for CC
 */