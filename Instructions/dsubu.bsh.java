execute(des, src,immediate, registers, memory) throws Exception{
if (!immediate.isHex() && !des.isHex() && !src.isHex()) {
		Calculator c = new Calculator(registers, memory);
		int desSize = registers.getBitSize(des);
        String x = registers.get(immediate);
        String y = registers.get(src);
        // Addition in Binary Format
        String strX = c.binaryZeroExtend(new BigInteger(x, 16).toString(2), desSize);
        String strY = c.binaryZeroExtend(new BigInteger(y, 16).toString(2), desSize);
//        BigInteger result = biY.add(biX);
		String resultingValue = "0";

		if(des.getValue().equals("R0") || des.getValue().equals("$0")){
			
		}
//		else if(biX.compareTo(new BigInteger("FFFFFFFFFFFFFFFF", 16)) == 1){
//			throw new DataOutOfRangeException("Immediate Value");
//		}
//		else if(biY.compareTo(new BigInteger("FFFFFFFFFFFFFFFF", 16)) == 1){
//			throw new DataOutOfRangeException(src.getValue());
//		}
		else{
//			if (result.toString(16).length() > registers.getBitSize(des) / 4) {
//				resultingValue = result.toString(2).substring(1);
//			} else {
//				resultingValue = result.toString(2);
//			}
			String[] arrReturn = getRegisterProper(des.getValue(), registers, memory);
			performSubRegister(strY, strX, registers, c, des, desSize, arrReturn);

		}
}

}

String[] getRegisterProper(reg, registers,memory){
	String[] arrayReturnables = new String[2];
	switch(reg){
		case "R29":
			arrayReturnables[0] = "R29";
			arrayReturnables[1] = "R29";
			return arrayReturnables;
		case "R0":
		case "$ZERO":
			arrayReturnables[0] = "R0";
			arrayReturnables[1] = "$ZERO";
			return arrayReturnables;
		case "R1":
		case "$AT":
			arrayReturnables[0] = "R1";
			arrayReturnables[1] = "$AT";
			return arrayReturnables;
		case "R2":
		case "$V0":
			arrayReturnables[0] = "R2";
			arrayReturnables[1] = "$V0";
			return arrayReturnables;
		case "R3":
		case "$V1":
			arrayReturnables[0] = "R3";
			arrayReturnables[1] = "$V1";
			return arrayReturnables;
		case "R4":
		case "$A0":
			arrayReturnables[0] = "R4";
			arrayReturnables[1] = "$A0";
			return arrayReturnables;
		case "R5":
		case "$A1":
			arrayReturnables[0] = "R5";
			arrayReturnables[1] = "$A1";
			return arrayReturnables;
		case "R6":
		case "$A2":
			arrayReturnables[0] = "R6";
			arrayReturnables[1] = "$A2";
			return arrayReturnables;
		case "R7":
		case "$A3":
			arrayReturnables[0] = "R7";
			arrayReturnables[1] = "$A3";
			return arrayReturnables;
		case "R8":
		case "$T0":
			arrayReturnables[0] = "R8";
			arrayReturnables[1] = "$T0";
			return arrayReturnables;
		case "R9":
		case "$T1":
			arrayReturnables[0] = "R9";
			arrayReturnables[1] = "$T1";
			return arrayReturnables;
		case "R10":
		case "#T2":
			arrayReturnables[0] = "R10";
			arrayReturnables[1] = "T1T2";
			return arrayReturnables;
	}
}

performSubRegister(destination, source, registers, calculator, des, desSize, arrReturn) {
		System.out.println(source + " 1st src");
		System.out.println(destination + " 2nd src");
	String result = "";
	int r = 0;
	int borrow = 0;
	int carry = 0;
	int overflow = 0;

	for(int i = desSize - 1; i >= 0; i--) {

	r = Integer.parseInt(String.valueOf(destination.charAt(i)))
	- Integer.parseInt(String.valueOf(source.charAt(i)))
	- borrow;

	if( r < 0 ) {
		borrow = 1;
		r += 2;
		result = result.concat(r.toString());

		if( i == 0 ) {
			carry = 1;
		}
		if( i == 0 || i == 1 ) {
			overflow++;
		}
	}
	else {
		borrow = 0;
		result = result.concat(r.toString());
		}
	}


	String d = new StringBuffer(result).reverse().toString();
		System.out.println("result: " + d);
	registers.set(arrReturn[0], calculator.binaryToHexString(d, des));
	registers.set(arrReturn[1], calculator.binaryToHexString(d, des));

}