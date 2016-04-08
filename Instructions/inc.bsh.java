execute(des, registers, memory) {
 	if ( des.isRegister() ){
        String x = registers.get(des);

        Calculator c = new Calculator(registers, memory);
        EFlags ef = registers.getEFlags();

		BigInteger biX = new BigInteger(x,16);
		BigInteger result = biX.add(new BigInteger("1"));

		registers.set(des,c.binaryToHexString(result.toString(2),des));
		ef.setParityFlag(c.checkParity(c.binaryZeroExtend(result.toString(2), des)));

		ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), new BigInteger("1").toString(16)));

		String sign = "" + c.binaryZeroExtend(result.toString(2), des).charAt(0);
		ef.setSignFlag(sign);

		if (c.binaryZeroExtend(resultingValue, des).equals(c.binaryZeroExtend("0", des)))
			ef.setZeroFlag("1");
		else
			ef.setZeroFlag("0");

		ef.setOverflowFlag(c.checkOverflowAdd('0', c.binaryZeroExtend(biX.toString(2), des).charAt(0), c.binaryZeroExtend(result.toString(2), des).charAt(0)));

 	}else if ( des.isMemory() ){
		int desSize = memory.getBitSize(des);
		String x = memory.read(des, desSize);

		Calculator c = new Calculator(registers, memory);
		EFlags ef = registers.getEFlags();

		BigInteger biX = new BigInteger(x,16);
		BigInteger result = biX.add(new BigInteger("1"));

		memory.write(des,c.binaryToHexString(result.toString(2),des), desSize);

		ef.setParityFlag(c.checkParity(result.toString(2)));

		String sign = "" + c.binaryZeroExtend(result.toString(2), des).charAt(0);
		ef.setSignFlag(sign);

		if (c.binaryZeroExtend(resultingValue, des).equals(c.binaryZeroExtend("0", des)))
			ef.setZeroFlag("1");
		else
			ef.setZeroFlag("0");
		ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), new BigInteger("1").toString(16)));
		ef.setOverflowFlag(c.checkOverflowAdd('0', c.binaryZeroExtend(biX.toString(2), des).charAt(0), c.binaryZeroExtend(result.toString(2), des).charAt(0)));
		}
 }