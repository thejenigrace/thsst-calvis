
execute(des, src,immediate, registers, memory) throws Exception{
if (immediate.isHex() && !des.isHex() && !src.isHex()) {
        String x = immediate.getValue();
        String y = registers.get(src);
        // Addition in Binary Format
        BigInteger biX = new BigInteger(x, 16);
        BigInteger biY = new BigInteger(y, 16);
        BigInteger result = biY.and(biX);
		String resultingValue = "0";
        Calculator c = new Calculator(registers, memory);
		if(des.getValue().equals("R0") || des.getValue().equals("$0")){
			
		}
		else if(des.getValue().equals("R1") || des.getValue().equals("$at")){
		
		}
		else{
			if (result.toString(16).length() > registers.getBitSize(des) / 4) {
				resultingValue = result.toString(2).substring(1);
			} else {
				resultingValue = result.toString(2);
			}
			String[] arrReturn = getRegisterProper(des.getValue(), registers, memory);
			registers.set(arrReturn[0], c.binaryToHexString(c.binaryZeroExtend(resultingValue, des), des));
			registers.set(arrReturn[1], c.binaryToHexString(c.binaryZeroExtend(resultingValue, des), des));
		}  
}
}
String[] getRegisterProper(reg, registers,memory){
	String[] arrayReturnables = new String[2];
	switch(reg){
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
