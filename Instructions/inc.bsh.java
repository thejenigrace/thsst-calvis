execute(des, registers, memory) {
 	if ( des.isRegister() ){
        System.out.println("des = " + registers.get(des));
        String x = registers.get(des);

        Calculator c = new Calculator(registers, memory);
        EFlags ef = registers.getEFlags();

		BigInteger biX = new BigInteger(x,16);
		BigInteger result = biX.add(new BigInteger("1"));

		registers.set(des,c.binaryToHexString(result.toString(2),des));

		// Debugging
		System.out.println("x = " + biX.toString(2));
		System.out.println("r = " + result.toString(2));

		System.out.println("x = " + biX.toString(16));
		System.out.println("r = " + c.binaryToHexString(result.toString(2),des));

		ef.setCarryFlag("0");

		ef.setParityFlag(c.checkParity(result.toString(2),des));

		ef.setAuxiliaryFlag("0");

		String sign=""+result.toString(2).charAt(0);
		ef.setSignFlag(sign);

		if(result.equals(BigInteger.ZERO))
			ef.setZeroFlag("1");
		else
			ef.setZeroFlag("0");

		ef.setOverflowFlag("0");

		System.out.println("CF: "+ef.getCarryFlag()+
			"\nPF: "+ef.getParityFlag()+
			"\nAF: "+ef.getAuxiliaryFlag()+
			"\nSF: "+ef.getSignFlag()+
			"\nZF: "+ef.getZeroFlag()+
			"\nOF: "+ef.getOverflowFlag());
 	}
// 	else if ( des.isMemory() ){
//
// 	}
 }