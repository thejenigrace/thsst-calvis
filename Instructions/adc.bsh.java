execute(des, src, registers, memory) {
	String numberOfF = "";
	String resultingValue = "";
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

 	if ( des.isRegister() ){
		int desSize = registers.getBitSize(des);
 		if (src.isRegister() && desSize == registers.getBitSize(src) ){
 			System.out.println("ADC register to register");

            String x = registers.get(src);
            String y = registers.get(des);

			if(registers.getBitSize(des) == registers.getBitSize(src)){
                Calculator c = new Calculator(registers,memory);
                EFlags ef = registers.getEFlags();

				// Addition in Binary Format
				BigInteger biX=new BigInteger(x,16);
				BigInteger biY=new BigInteger(y,16);
				BigInteger result=biX.add(biY);
                if(ef.getCarryFlag().equals("1")) {
					BigInteger addPlusOne = BigInteger.valueOf(new Integer(1).intValue());
					result = result.add(addPlusOne);
                }
				if(result.toString(16).length() > registers.getBitSize(src)/4){
					resultingValue = result.toString(2).substring(1);
				}
				else{
					resultingValue = result.toString(2);
				}
				registers.set(des,c.binaryToHexString(resultingValue,des));
				BigInteger biC=new BigInteger(numberOfF,16);
				if(result.compareTo(biC) == 1)
					ef.setCarryFlag("1");
				else
					ef.setCarryFlag("0");

				ef.setParityFlag(c.checkParity(result.toString(2)));

				ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));

				if(result.testBit(desSize - 1))
					ef.setSignFlag("1");
				else
					ef.setSignFlag("0");

				if(result.equals(BigInteger.ZERO))
				ef.setZeroFlag("1");
				else
				ef.setZeroFlag("0");

		ef.setOverflowFlag(c.checkOverflowAddWithFlag(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0),
		c.binaryZeroExtend(resultingValue, src).charAt(0), ef.getCarryFlag()));
			}
 		}
 		else if ( src.isHex() ) {
            System.out.println("ADC immediate to register");

            String x = src.getValue();
		System.out.println(":(");
            String y = registers.get(des);
			Calculator c = new Calculator(registers,memory);
			EFlags ef = registers.getEFlags();
		System.out.println("ADC immediate to register");

		// Addition in Binary Format
            BigInteger biX=new BigInteger(x,16);
			BigInteger biY=new BigInteger(y,16);
			BigInteger result=biX.add(biY);

            if(ef.getCarryFlag().equals("1")){
				BigInteger addPlusOne = BigInteger.valueOf(new Integer(1).intValue());
				result = result.add(addPlusOne);
			}

			if(result.toString(16).length() > registers.getBitSize(des)/4){
				resultingValue = result.toString(2).substring(1);
			}
			else{
				resultingValue = result.toString(2);
			}
			registers.set(des,c.binaryToHexString(resultingValue,des));

                BigInteger biC=new BigInteger(numberOfF,16);
                if(result.compareTo(biC)==1)
                    ef.setCarryFlag("1");
                else
                    ef.setCarryFlag("0");

                ef.setParityFlag(c.checkParity(result.toString(2)));

				ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));

				if(result.testBit(desSize - 1))
				ef.setSignFlag("1");
				else
				ef.setSignFlag("0");


			if(result.equals(BigInteger.ZERO))
	                    ef.setZeroFlag("1");
	                else
	                    ef.setZeroFlag("0");

		ef.setOverflowFlag(c.checkOverflowAddWithFlag(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0),
		c.binaryZeroExtend(resultingValue, src).charAt(0), ef.getCarryFlag()));
 		}
 		else if ( src.isMemory() ){
		int srcSize = memory.getBitSize(src);
		String x = memory.read(src, srcSize);
		String y = registers.get(des);
		Calculator c = new Calculator(registers,memory);
		EFlags ef = registers.getEFlags();

		// Addition in Binary Format
		BigInteger biX=new BigInteger(x,16);
		BigInteger biY=new BigInteger(y,16);
		BigInteger result=biX.add(biY);

		if(ef.getCarryFlag().equals("1")){
			BigInteger addPlusOne = BigInteger.valueOf(new Integer(1).intValue());
			result = result.add(addPlusOne);
		}

		registers.set(des,c.binaryToHexString(result.toString(2),des));

		BigInteger biC=new BigInteger(numberOfF,16);
		if(result.compareTo(biC)==1)
			ef.setCarryFlag("1");
		else
			ef.setCarryFlag("0");

		ef.setParityFlag(c.checkParity(result.toString(2)));

		ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));

		if(result.testBit(desSize - 1))
		ef.setSignFlag("1");
		else
		ef.setSignFlag("0");


		if(result.equals(BigInteger.ZERO))
			ef.setZeroFlag("1");
		else
			ef.setZeroFlag("0");

		ef.setOverflowFlag(c.checkOverflowAddWithFlag(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0),
		c.binaryZeroExtend(resultingValue, src).charAt(0), ef.getCarryFlag()));
 		}
 	}
 	else if ( des.isMemory() ){


 		if ( src.isRegister()){
			System.out.println("ADC register to register");
			int desSize = registers.getBitSize(src);
			String x = registers.get(src);
			String y = memory.read(des, desSize);
			Calculator c = new Calculator(registers,memory);
			EFlags ef = registers.getEFlags();

			// Addition in Binary Format
			BigInteger biX=new BigInteger(x,16);
			BigInteger biY=new BigInteger(y,16);
			BigInteger result=biX.add(biY);

			if(ef.getCarryFlag().equals("1")) {
				BigInteger addPlusOne = BigInteger.valueOf(new Integer(1).intValue());
				result = result.add(addPlusOne);
			}
			if(result.toString(16).length() > registers.getBitSize(src)/4){
				resultingValue = result.toString(2).substring(1);
			}
			else{
				resultingValue = result.toString(2);
			}
			if(result.toString(16).length() > registers.getBitSize(src)/4){
				resultingValue = result.toString(2).substring(1);
			}
			else{
				resultingValue = result.toString(2);
			}
			memory.write(des,c.binaryToHexString(resultingValue, src), desSize);
			System.out.println(numberOfF);
			BigInteger biC = new BigInteger(numberOfF,16);
			if(result.compareTo(biC)==1)
				ef.setCarryFlag("1");
			else
				ef.setCarryFlag("0");

			ef.setParityFlag(c.checkParity(result.toString(2)));

			ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));

			if(result.testBit(desSize - 1))
				ef.setSignFlag("1");
			else
				ef.setSignFlag("0");

			if(result.equals(BigInteger.ZERO))
				ef.setZeroFlag("1");
			else
				ef.setZeroFlag("0");

		ef.setOverflowFlag(c.checkOverflowAddWithFlag(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0),
		c.binaryZeroExtend(resultingValue, src).charAt(0), ef.getCarryFlag()));
 		}
 		else if ( src.isHex() ){
			System.out.println("ADC immediate to memory");
			int desSize = memory.getBitSize(des);
			String x = src.getValue();
			String y = memory.read(des, desSize);
			Calculator c = new Calculator(registers,memory);
			EFlags ef = registers.getEFlags();

			// Addition in Binary Format
			BigInteger biX=new BigInteger(x,16);
			BigInteger biY=new BigInteger(y,16);
			BigInteger result=biX.add(biY);

			if(ef.getCarryFlag().equals("1")){
			BigInteger addPlusOne = BigInteger.valueOf(new Integer(1).intValue());
			result = result.add(addPlusOne);
			}

			if(result.toString(16).length() > memory.getBitSize(des)/4){
				resultingValue = result.toString(2).substring(1);
			}
			else{
				resultingValue = result.toString(2);
			}

			memory.write(des,c.binaryToHexString(resultingValue,des), memory.getBitSize(des));

			BigInteger biC=new BigInteger(numberOfF,16);
			if(result.compareTo(biC)==1)
			ef.setCarryFlag("1");
			else
			ef.setCarryFlag("0");

			ef.setParityFlag(c.checkParity(result.toString(2)));

			ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));

			if(result.testBit(desSize - 1))
				ef.setSignFlag("1");
			else
				ef.setSignFlag("0");


			if(result.equals(BigInteger.ZERO))
				ef.setZeroFlag("1");
			else
				ef.setZeroFlag("0");

		ef.setOverflowFlag(c.checkOverflowAddWithFlag(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0),
		c.binaryZeroExtend(resultingValue, src).charAt(0), ef.getCarryFlag()));
		}
 	}
 }