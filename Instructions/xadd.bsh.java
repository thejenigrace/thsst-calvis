execute(des,src,registers,memory){
		if(des.isRegister()){
			int desSize=registers.getBitSize(des);
			System.out.println(desSize + "wtf. . . . ");
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
					registers.set(des,c.binaryToHexString(addResult.toString(2), des));

					EFlags ef=registers.getEFlags();
					BigInteger biC=new BigInteger("FFFFFFFF",16);
					if(addResult.compareTo(biC)==1)
						ef.setCarryFlag("1");
					else
						ef.setCarryFlag("0");

					ef.setParityFlag(c.checkParity(addResult.toString(2)));

					ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16),biY.toString(16)));

					if(result.testBit(desSize - 1))
					ef.setSignFlag("1");
					else
					ef.setSignFlag("0");


		if(addResult.equals(BigInteger.ZERO))
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
			}
			else if(src.isMemory()){
				System.out.println("wow");
				String x = memory.read(src,desSize);
				String y = registers.get(des);

				// Addition in Binary Format
				BigInteger biX = new BigInteger(x,16);
				BigInteger biY = new BigInteger(y,16);
				BigInteger addResult = biY.add(biX);

				Calculator c=new Calculator(registers,memory);
				memory.write(src,c.binaryToHexString(registers.get(des), des), registers.getBitSize(des));
				registers.set(des,c.binaryToHexString(addResult.toString(2), des));

				EFlags ef = registers.getEFlags();
				BigInteger biC = new BigInteger("FFFFFFFF",16);
				if(addResult.compareTo(biC)==1)
					ef.setCarryFlag("1");
				else
					ef.setCarryFlag("0");

				ef.setParityFlag(c.checkParity(addResult.toString(2)));

				ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16),biY.toString(16)));

				if(result.testBit(desSize - 1))
				ef.setSignFlag("1");
				else
				ef.setSignFlag("0");


				if(addResult.equals(BigInteger.ZERO))
					ef.setZeroFlag("1");
				else
					ef.setZeroFlag("0");

				ef.setOverflowFlag("0");
			}
		}
}