execute(des, src, registers, memory) {
	Calculator c = new Calculator(registers, memory);
	boolean isRegisterSrc = false;
	boolean isRegisterDes = false;
	int desSize = 0;
	int counter = 0;
	String destination = "";
	String source = "";
	String resultingHex = "";
		System.out.println(des);
		System.out.println(src);
	if(des.isRegister()){
		desSize = registers.getBitSize(des);
		isRegisterDes = true;
		destination = registers.get(des);
		
	}


	if(src.isHex()){
		counter = new BigInteger(src.getValue(), 16).intValue();
	}

	if(desSize == 128){
		resultingHex = destination;
		String temp;
		for(int x = 0; x < (counter); x++){;
			temp = resultingHex.substring(2 , 32);
			StringBuilder sb = new StringBuilder(temp);
			sb.append("00");
			resultingHex = sb.toString();
		System.out.println(resultingHex);
		}


		registers.set(des, resultingHex);
	}
	
	
	
}
