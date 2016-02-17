execute(des,src,registers,memory){
		if(des.isRegister()){
		int desSize=registers.getBitSize(des);
		System.out.println(desSize + "wtf. . . . ");
		if(src.isRegister() &&desSize==registers.getBitSize(src)){
		System.out.println("ADD register to register");
		String x=registers.get(src);
		String y=registers.get(des);

		if(registers.getBitSize(des)==registers.getBitSize(src)){
		// Addition in Binary Format
		BigInteger biX=new BigInteger(x,16);
		BigInteger biY=new BigInteger(y,16);
		BigInteger result=biY.add(biX);

		Calculator c=new Calculator(registers,memory);
		registers.set(des,c.binaryToHexString(result.toString(2),des));

		// Debugging
		System.out.println("x = "+biX.toString(2));
		System.out.println("y = "+biY.toString(2));
		System.out.println("r = "+result.toString(2));

		System.out.println("x = "+biX.toString(16));
		System.out.println("y = "+biY.toString(16));
		System.out.println("r = "+c.binaryToHexString(result.toString(2),des));

		EFlags ef=registers.getEFlags();
		BigInteger biC = new BigInteger("FFFFFFFF",16);
		if(result.compareTo(biC)==1)
			ef.setCarryFlag("1");
		else
			ef.setCarryFlag("0");

		ef.setParityFlag(c.checkParity(result.toString(2)));

		ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16),biY.toString(16)));

		if(result.testBit(desSize - 1))
		ef.setSignFlag("1");
		else
		ef.setSignFlag("0");


		if(result.equals(BigInteger.ZERO))
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
		else if(src.isHex()){
		String x=src.getValue();
		String y=registers.get(des);

		// Addition in Binary Format
		BigInteger biX=new BigInteger(x,16);
		BigInteger biY=new BigInteger(y,16);
		BigInteger result=biY.add(biX);
		System.out.println("result"+result);
		Calculator c=new Calculator(registers,memory);
		registers.set(des,c.binaryToHexString(result.toString(2),des));
		System.out.println(c.binaryToHexString(result.toString(2),des)+"nice pass");
		EFlags ef=registers.getEFlags();
		BigInteger biC=new BigInteger("FFFFFFFF",16);
		if(result.compareTo(biC)==1)
		ef.setCarryFlag("1");
		else
		ef.setCarryFlag("0");

		ef.setParityFlag(c.checkParity(result.toString(2)));

		ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16),biY.toString(16)));
		System.out.println(result.toString(2) + " :result");


		if(result.testBit(desSize - 1))
			ef.setSignFlag("1");
		else
			ef.setSignFlag("0");

		if(result.equals(BigInteger.ZERO))
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
		else if(src.isMemory()){
		System.out.println("wow");
		String x=memory.read(src,desSize);
		String y=registers.get(des);

		// Addition in Binary Format
		BigInteger biX=new BigInteger(x,16);
		BigInteger biY=new BigInteger(y,16);
		BigInteger result=biY.add(biX);

		Calculator c=new Calculator(registers,memory);
		registers.set(des,c.binaryToHexString(result.toString(2),des));

		EFlags ef=registers.getEFlags();
		BigInteger biC=new BigInteger("FFFFFFFF",16);
		if(result.compareTo(biC)==1)
		ef.setCarryFlag("1");
		else
		ef.setCarryFlag("0");

		ef.setParityFlag(c.checkParity(result.toString(2)));

		ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16),biY.toString(16)));


		if(result.testBit(desSize - 1))
		ef.setSignFlag("1");
		else
		ef.setSignFlag("0");


		if(result.equals(BigInteger.ZERO))
		ef.setZeroFlag("1");
		else
		ef.setZeroFlag("0");

		ef.setOverflowFlag("0");
		}
		}
		else if(des.isMemory()){
		int desSize = memory.getBitSize(des);
		System.out.println("wtf");
		if(src.isRegister() && registers.getBitSize(src) == memory.getBitSize(des)){
		System.out.println("ADD register to register");
		String x=registers.get(src);
		String y=memory.read(des, desSize);

		if(memory.getBitSize(des)==registers.getBitSize(src)){
		// Addition in Binary Format
		BigInteger biX=new BigInteger(x,16);
		BigInteger biY=new BigInteger(y,16);
		BigInteger result=biY.add(biX);
		System.out.println("passed");
		Calculator c=new Calculator(registers,memory);
		memory.write(des,c.binaryToHexString(result.toString(2),des), desSize);

		EFlags ef=registers.getEFlags();
		BigInteger biC = new BigInteger("FFFFFFFF",16);
		if(result.compareTo(biC)==1)
			ef.setCarryFlag("1");
		else
			ef.setCarryFlag("0");

		ef.setParityFlag(c.checkParity(result.toString(2)));

		ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16),biY.toString(16)));

		if(result.testBit(desSize - 1))
			ef.setSignFlag("1");
		else
			ef.setSignFlag("0");


		if(result.equals(BigInteger.ZERO))
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
		else if(src.isHex()){
		System.out.println("ADD register to register");
		String x=src.getValue();
		String y=memory.read(des, desSize);

		// Addition in Binary Format
		BigInteger biX=new BigInteger(x,16);
		BigInteger biY=new BigInteger(y,16);
		BigInteger result=biY.add(biX);
		System.out.println("passed");
		Calculator c=new Calculator(registers,memory);
		memory.write(des,c.binaryToHexString(result.toString(2),des), desSize);

		EFlags ef=registers.getEFlags();
		BigInteger biC=new BigInteger("FFFFFFFF",16);
		if(result.compareTo(biC)==1)
		ef.setCarryFlag("1");
		else
		ef.setCarryFlag("0");

		ef.setParityFlag(c.checkParity(result.toString(2)));

		ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16),biY.toString(16)));

		if(result.testBit(desSize - 1))
		ef.setSignFlag("1");
		else
		ef.setSignFlag("0");


		if(result.equals(BigInteger.ZERO))
		ef.setZeroFlag("1");
		else
		ef.setZeroFlag("0");

		ef.setOverflowFlag("0");

		}
		}
		}