execute(des, src, registers, memory) {
		Calculator calculator = new Calculator(registers, memory);

		if ( des.isRegister() ) {
		if( src.isRegister() && src.getValue().equals("CL") ) {
		System.out.println("SHL register and CL");

		//get size of des
		int desSize = registers.getBitSize(des);
		String originalDes = calculator.hexToBinaryString(registers.get(des), des);
		String originalSign = originalDes.charAt(0) + "";

		boolean checkSize = false;
		for(int a : registers.getAvailableSizes()) {
		if(a == desSize) {
		checkSize = true;
		}
		}

		BigInteger count = new BigInteger(registers.get(src), 16);
		int limit = count.intValue();
		if( checkSize && (limit >= 0 && limit <= 31) ) {
		String destination = calculator.hexToBinaryString(registers.get(des), des);
		String destination = calculator.hexToBinaryString(registers.get(src), src);

		BigInteger biDes = new BigInteger(destination, 2);
		BigInteger biSrc = new BigInteger(source, 2);
		boolean bitSet = false;
		//BigInteger biResult = biDes.shiftLeft(count.intValue());
		int carryFlag = flags.getCarryFlag();
		for(int x = 0; x < limit; x++){
		bitSet = biDes.testBit(desSize - 1);
		if(bitSet){
		carryFlagValue = "1";
		}
		else{
		carryFlagValue = "0";
		}
		}



		String result = calculator.binaryToHexString(biResult.toString(2), des);
		if(result.length() > 8) {
		int cut = result.length() - 8;
		String t = result.substring(cut);
		registers.set(des, t);
		}
		else {
		registers.set(des, result);
		}

		//FLAGS
		EFlags flags = registers.getEFlags();
		if (limit == 0) {
		//flags not affected
		}
		else {
		if(biResult.equals(BigInteger.ZERO)) {
		flags.setZeroFlag("1");
		}
		else {
		flags.setZeroFlag("0");
		}

		String r = calculator.hexToBinaryString(registers.get(des), des);
		String sign = "" + r.charAt(0);
		flags.setSignFlag(sign);

		String parity = calculator.checkParity(r, des);
		flags.setParityFlag(parity);

		if(limit == 1 && originalSign.equals(sign)) {
		flags.setOverflowFlag("0");
		}
		else if(limit == 1 && !originalSign.equals(sign)) {
		flags.setOverflowFlag("1");
		}
		else {
		// flags.setOverflowFlag(undefined);
		}

		flags.setCarryFlag(originalDes.charAt(limit - 1).toString());

		//flags.setAuxiliaryFlag(undefined)
		}
		}
		}
		else if ( src.isHex() && src.getValue().length() <= 2){
		System.out.println("SHL register and i8");

		//get size of des
		int desSize = registers.getBitSize(des);
		String originalDes = calculator.hexToBinaryString(registers.get(des), des);
		String originalSign = originalDes.charAt(0) + "";

		boolean checkSize = false;
		for(int a : registers.getAvailableSizes()) {
		if(a == desSize) {
		checkSize = true;
		}
		}

		BigInteger count = new BigInteger(src.getValue(), 16);
		int limit = count.intValue();
		if( checkSize && (limit >= 0 && limit <= 31) ) {
		String destination = calculator.hexToBinaryString(registers.get(des), des);

		BigInteger biDes = new BigInteger(destination, 2);
		BigInteger biResult = biDes.shiftLeft(count.intValue());

		String result = calculator.binaryToHexString(biResult.toString(2), des);
		if(result.length() > 8) {
		int cut = result.length() - 8;
		String t = result.substring(cut);
		registers.set(des, t);
		}
		else {
		registers.set(des, result);
		}

		//FLAGS
		EFlags flags = registers.getEFlags();
		if (limit == 0) {
		//flags not affected
		}
		else {
		if(biResult.equals(BigInteger.ZERO)) {
		flags.setZeroFlag("1");
		}
		else {
		flags.setZeroFlag("0");
		}

		String r = calculator.hexToBinaryString(registers.get(des), des);
		String sign = "" + r.charAt(0);
		flags.setSignFlag(sign);

		String parity = calculator.checkParity(r, des);
		flags.setParityFlag(parity);

		if(limit == 1 && originalSign.equals(sign)) {
		flags.setOverflowFlag("0");
		}
		else if(limit == 1 && !originalSign.equals(sign)) {
		flags.setOverflowFlag("1");
		}
		else {
		// flags.setOverflowFlag(undefined);
		}

		flags.setCarryFlag(originalDes.charAt(limit - 1).toString());

		//flags.setAuxiliaryFlag(undefined)
		}
		}
		}
		}
		else if ( des.isMemory() ){
		if( src.isRegister() && src.getValue().equals("CL") ) {
		System.out.println("SHL memory and CL");

		}
		else if ( src.isHex() && registers.get(src).length() == 2){
		System.out.println("SHL memory and i8");

		}
		}
		}
