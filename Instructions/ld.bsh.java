execute(des, src, registers, memory) {
	if ( des.isRegister()) {
		if ( src.isMemory() ) {
			String[] arrReturn = getRegisterProper(des.getValue(), registers, memory);
			int desRegSize = registers.getBitSize(des);
			String x = memory.read(src, desRegSize);
			registers.set(arrReturn[0], x);
			registers.set(arrReturn[1], x);
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
