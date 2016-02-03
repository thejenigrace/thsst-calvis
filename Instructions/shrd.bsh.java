execute(des, src, cnt, registers, memory) {
		Calculator calculator = new Calculator(registers, memory);
		EFlags flags = registers.getEFlags();
//		int bitMode = registers.MAX_SIZE;
		int bitMode = 32;
		if ( des.isRegister() ) {
		if( src.isRegister() && cnt.getValue().equals("CL") ) {
		System.out.println("SHLD register and CL");
//
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

		int count = new BigInteger(registers.get(cnt), 16).intValue() % bitMode;
		int limit = count;
		if( checkSize && (limit >= 0 && limit <= bitMode - 1) ) {
		String destination = calculator.hexToBinaryString(registers.get(des), des);
		String source = calculator.hexToBinaryString(registers.get(src), src);
//
//
		BigInteger biSrc = new BigInteger(source, 2);
		BigInteger biDes = new BigInteger(destination, 2);
		BigInteger biResult = biDes;
		boolean bitSet = false;
		boolean srcBitSet = false;
		//BigInteger biResult = biDes.shiftLeft(count.intValue());
		String carryFlag = flags.getCarryFlag();
//
//
		for(int x = 0; x < limit; x++){
		bitSet = biDes.testBit(0);
		biResult = biResult.shiftRight(x + 1);

		if(bitSet){
		carryFlagValue = "1";
		}
		else{
		carryFlagValue = "0";
		}
//		//check sign bit of source then transfer to lsb of destination
		srcBitSet = biSrc.testBit(x);
		switch(srcBitSet){
		case true:
		biResult = biResult.setBit(desSize - 1);
		break;
		case false:
		biResult = biResult.clearBit(desSize - 1);
		break;
		}
		}

		flags.setCarryFlag(carryFlagValue);
		String result = calculator.binaryToHexString(biResult.toString(2), des);
		System.out.println("result " + result);
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
		if(biDes.equals(BigInteger.ZERO)) {
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
		else if ( cnt.isHex() && cnt.getValue().length() <= 2){
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

		int count = new BigInteger(cnt.getValue(), 16).intValue() % bitMode;
		int limit = count.intValue();

		if( checkSize && (limit >= 0 && limit <= bitMode - 1) ) {
		String destination = calculator.hexToBinaryString(registers.get(des), des);
		String source = calculator.hexToBinaryString(registers.get(src), src);

		BigInteger biSrc = new BigInteger(source, 2);
		BigInteger biDes = new BigInteger(destination, 2);
		BigInteger biResult = biDes;

		boolean bitSet = false;
		boolean srcBitSet = false;
		String carryFlag = flags.getCarryFlag();

		for(int x = 0; x < limit; x++){
		bitSet = biDes.testBit(0);
		biResult = biResult.shiftLeft(x + 1);

		if(bitSet){
		carryFlagValue = "1";
		}
		else{
		carryFlagValue = "0";
		}
//		//check sign bit of source then transfer to lsb of destination
		srcBitSet = biSrc.testBit(x);

		switch(srcBitSet){
		case true:
		biResult = biResult.setBit(desSize - 1);
		break;
		case false:
		biResult = biResult.clearBit(desSize - 1);
		break;
		}
		}

		flags.setCarryFlag(carryFlagValue);


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
