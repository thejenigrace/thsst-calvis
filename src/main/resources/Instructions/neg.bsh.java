execute(des, registers, memory){
		EFlags ef = registers.getEFlags();
		Calculator calculator = new Calculator(registers, memory);
		String zeroExtend = "";
            if(des.isRegister()) {
System.out.println("SUB register register");

				  //get size of des, src
				  int desSize = registers.getBitSize(des);
				  int srcSize = desSize;

				  //check if des size is 8-, 16-, 32-bit
				  boolean checkSize = false;
				  for(int a : registers.getAvailableSizes()) {
					if(a == desSize) {
					  checkSize = true;
					}
				  }

				  if( (desSize == srcSize) && checkSize) {
					//get hex value of des, src then convert to binary
					String source = "";
					for(int x = 0; x < desSize; x++){
						source += "0";
					}
					String destination = calculator.hexToBinaryString(registers.get(des), des);

					String result = "";
					int r = 0;
					int borrow = 0;
					int carry = 0;
					int overflow = 0;

					for(int i = desSize - 1; i >= 0; i--) {
					  r = Integer.parseInt(String.valueOf(source.charAt(i))) - Integer.parseInt(String.valueOf(destination.charAt(i)))  - borrow;

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
					registers.set(des, calculator.binaryToHexString(d, des));

					//FLAGS
					EFlags flags = registers.getEFlags();
					String res = calculator.hexToBinaryString(registers.get(des), des);
					BigInteger biR = new BigInteger(res, 2);

					flags.setCarryFlag(carry.toString());

					if(overflow == 1) {
					  flags.setOverflowFlag("1");
					}
					else {
					  flags.setOverflowFlag("0");
					}

					if(biR.equals(BigInteger.ZERO)) {
					  flags.setZeroFlag("1");
					}
					else {
					  flags.setZeroFlag("0");
					}

					String sign = "" + res.charAt(0);
					flags.setSignFlag(sign);

					String parity = calculator.checkParity(res);
					flags.setParityFlag(parity);

					String auxiliary = calculator.checkAuxiliarySub(source, destination);
					flags.setAuxiliaryFlag(auxiliary);
				  }
            }
			else if(des.isMemory()) {
			//get size of des, src
				  int desSize = memory.getBitSize(des);
				  int srcSize = desSize;

				  //check if des size is 8-, 16-, 32-bit
				  boolean checkSize = false;
				  for(int a : registers.getAvailableSizes()) {
					if(a == desSize) {
					  checkSize = true;
					}
				  }

				  if( (desSize == srcSize) && checkSize) {
					//get hex value of des, src then convert to binary
					String source = "";
					for(int x = 0; x < desSize; x++){
						source += "0";
					}
					String destination = calculator.hexToBinaryString(memory.read(des, desSize), des);

					String result = "";
					int r = 0;
					int borrow = 0;
					int carry = 0;
					int overflow = 0;

					for(int i = desSize - 1; i >= 0; i--) {
					  r = Integer.parseInt(String.valueOf(source.charAt(i))) - Integer.parseInt(String.valueOf(destination.charAt(i)))  - borrow;

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
					memory.write(des, calculator.binaryToHexString(d, des), des);

					//FLAGS
					EFlags flags = registers.getEFlags();
					String res = calculator.hexToBinaryString(memory.read(des, desSize), des);
					BigInteger biR = new BigInteger(res, 2);

					flags.setCarryFlag(carry.toString());

					if(overflow == 1) {
					  flags.setOverflowFlag("1");
					}
					else {
					  flags.setOverflowFlag("0");
					}

					if(biR.equals(BigInteger.ZERO)) {
					  flags.setZeroFlag("1");
					}
					else {
					  flags.setZeroFlag("0");
					}

					String sign = "" + res.charAt(0);
					flags.setSignFlag(sign);

					String parity = calculator.checkParity(res);
					flags.setParityFlag(parity);

					String auxiliary = calculator.checkAuxiliarySub(source, destination);
					flags.setAuxiliaryFlag(auxiliary);
				  }
			}
}
