execute(des,src,registers,memory){
		String numberOfF = "";
		String resultingValue = "";
		int sizeOfF = 0;
		if(des.isRegister())
			sizeOfF = registers.getBitSize(des);
		else if(des.isMemory()){
			if(src.isRegister())
				sizeOfF = registers.getBitSize(src);
			else
				sizeOfF = memory.getBitSize(des);
		}
		for(int x = 0; x < sizeOfF/4; x++){
			numberOfF += "F";
		}

		if(des.isRegister()){
			int desSize=registers.getBitSize(des);
			if(src.isRegister() &&desSize==registers.getBitSize(src)){
				System.out.println("ADD register to register");
				String x = registers.get(src);
				String y = registers.get(des);

				if(registers.getBitSize(des)==registers.getBitSize(src)){
					// Addition in Binary Format
					BigInteger biX = new BigInteger(x,16);
					BigInteger biY = new BigInteger(y,16);
					BigInteger addResult = biY.add(biX);

					Calculator c = new Calculator(registers,memory);
					registers.set(src, c.binaryToHexString(biY.toString(2), src));

					if(addResult.toString(16).length() > registers.getBitSize(src)/4){
						resultingValue = addResult.toString(2).substring(1);
					}
					else{
						resultingValue = addResult.toString(2);
					}
					registers.set(des,c.binaryToHexString(resultingValue, des));

					EFlags ef=registers.getEFlags();
					BigInteger biC=new BigInteger(numberOfF,16);
					if(addResult.compareTo(biC)==1)
						ef.setCarryFlag("1");
					else
						ef.setCarryFlag("0");

					ef.setParityFlag(c.checkParity(addResult.toString(2)));

					ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16),biY.toString(16)));

					if(addResult.testBit(desSize - 1))
						ef.setSignFlag("1");
					else
						ef.setSignFlag("0");


					if(addResult.equals(BigInteger.ZERO))
						ef.setZeroFlag("1");
					else
						ef.setZeroFlag("0");

					ef.setOverflowFlag(c.checkOverflowAdd(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0),
					c.binaryZeroExtend(resultingValue, des).charAt(0)));
				}
			}
			else if(src.isMemory()){
				String x = memory.read(src,desSize);
				String y = registers.get(des);

				// Addition in Binary Format
				BigInteger biX = new BigInteger(x,16);
				BigInteger biY = new BigInteger(y,16);
				BigInteger addResult = biY.add(biX);

				Calculator c=new Calculator(registers,memory);
				memory.write(src,c.binaryToHexString(registers.get(des), des), registers.getBitSize(des));
				if(addResult.toString(16).length() > registers.getBitSize(src)/4){
					resultingValue = addResult.toString(2).substring(1);
				}
				else{
					resultingValue = addResult.toString(2);
				}
				registers.set(des,c.binaryToHexString(resultingValue, des));

				EFlags ef = registers.getEFlags();
				BigInteger biC = new BigInteger(numberOfF,16);
				if(addResult.compareTo(biC)==1)
					ef.setCarryFlag("1");
				else
					ef.setCarryFlag("0");

				ef.setParityFlag(c.checkParity(addResult.toString(2)));

				ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16),biY.toString(16)));

				if(addResult.testBit(desSize - 1))
				ef.setSignFlag("1");
				else
				ef.setSignFlag("0");


				if(addResult.equals(BigInteger.ZERO))
					ef.setZeroFlag("1");
				else
					ef.setZeroFlag("0");


				ef.setOverflowFlag(c.checkOverflowAdd(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0),
				c.binaryZeroExtend(resultingValue, des).charAt(0)));
			}
		}
}