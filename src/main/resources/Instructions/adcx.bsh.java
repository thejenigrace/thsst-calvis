execute(des, src, registers, memory) {
	String numberOfF = "";
	int bitSize = 0;
	if( des.isRegister() )
		bitSize = registers.getBitSize(des);
	else if( des.isMemory() )
		bitSize = memory.getBitSize(des);
	for( int x = 0; x < bitSize; x++ ){
		numberOfF += "F";
	}
 	if ( des.isRegister() ){
 		if ( src.isRegister() ){
 			System.out.println("ADCX register to register");

            String x = registers.get(src);
            String y = registers.get(des);

			if(registers.getBitSize(des) == registers.getBitSize(src) && registers.getBitSize(des) == 32){
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

				registers.set(des,c.binaryToHexString(result.toString(2),des));

				// Debugging
				System.out.println("x = "+biX.toString(2));
				System.out.println("y = "+biY.toString(2));
				System.out.println("r = "+result.toString(2));

				System.out.println("x = "+biX.toString(16));
				System.out.println("y = "+biY.toString(16));
				System.out.println("r = "+c.binaryToHexString(result.toString(2),des));

				BigInteger biC=new BigInteger(numberOfF,16);
				if(result.compareTo(biC)==1)
					ef.setCarryFlag("1");
				else
					ef.setCarryFlag("0");

				System.out.println("CF: "+ef.getCarryFlag()+
						"\nPF: "+ef.getParityFlag()+
						"\nAF: "+ef.getAuxiliaryFlag()+
						"\nSF: "+ef.getSignFlag()+
						"\nZF: "+ef.getZeroFlag()+
						"\nOF: "+ef.getOverflowFlag());
			}
 		}
// 		else if ( src.isHex() ) {
//            System.out.println("ADCX immediate to register");
//
//            String x = src.getValue();
//            String y = registers.get(des);
//
//                Calculator c = new Calculator(registers,memory);
//                EFlags ef = registers.getEFlags();
//
//                // Addition in Binary Format
//                BigInteger biX=new BigInteger(x,16);
//                BigInteger biY=new BigInteger(y,16);
//                BigInteger result=biX.add(biY);
//
//                if(ef.getCarryFlag() == "1")
//                    result.add(new BigInteger("1"));
//
//                registers.set(des,c.binaryToHexString(result.toString(2),des));
//
//                // Debugging
//                System.out.println("x = "+biX.toString(2));
//                System.out.println("y = "+biY.toString(2));
//                System.out.println("r = "+result.toString(2));
//
//                System.out.println("x = "+biX.toString(16));
//                System.out.println("y = "+biY.toString(16));
//                System.out.println("r = "+c.binaryToHexString(result.toString(2),des));
//
//                BigInteger biC=new BigInteger("FFFFFFFF",16);
//                if(result.compareTo(biC)==1)
//                ef.setCarryFlag("1");
//                else
//                ef.setCarryFlag("0");
//
//                ef.setParityFlag(c.checkParity(result.toString(2)));
//
//				ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));
//
//                String sign=""+result.toString(2).charAt(0);
//                ef.setSignFlag(sign);
//
//                if(result.equals(BigInteger.ZERO))
//                ef.setZeroFlag("1");
//                else
//                ef.setZeroFlag("0");
//
//                ef.setOverflowFlag("0");
//
//                System.out.println("CF: "+ef.getCarryFlag()+
//                "\nPF: "+ef.getParityFlag()+
//                "\nAF: "+ef.getAuxiliaryFlag()+
//                "\nSF: "+ef.getSignFlag()+
//                "\nZF: "+ef.getZeroFlag()+
//                "\nOF: "+ef.getOverflowFlag());
// 		}
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
				BigInteger addPlusOne=BigInteger.valueOf(new Integer(1).intValue());
				result=result.add(addPlusOne);
			}

			memory.write(des,c.binaryToHexString(result.toString(2),des), srcSize);
			// Debugging
			System.out.println("x = "+biX.toString(2));
			System.out.println("y = "+biY.toString(2));
			System.out.println("r = "+result.toString(2));

			System.out.println("x = "+biX.toString(16));
			System.out.println("y = "+biY.toString(16));
			System.out.println("r = "+c.binaryToHexString(result.toString(2),des));

			BigInteger biC=new BigInteger(numberOfF,16);
			if(result.compareTo(biC)==1)
			ef.setCarryFlag("1");
			else
			ef.setCarryFlag("0");


			System.out.println("CF: "+ef.getCarryFlag()+
			"\nPF: "+ef.getParityFlag()+
			"\nAF: "+ef.getAuxiliaryFlag()+
			"\nSF: "+ef.getSignFlag()+
			"\nZF: "+ef.getZeroFlag()+
			"\nOF: "+ef.getOverflowFlag());
 		}
 	}
 	else if ( des.isMemory() ){
		int desSize = memory.getBitSize(des);
 		if ( src.isRegister() ){
			System.out.println("ADCX register to register");

			String x = registers.get(src);
			String y = memory.read(des, desSize);

			if(desSize == registers.getBitSize(src) && desSize == 32){
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

			memory.write(des,c.binaryToHexString(result.toString(2),des), desSize);
			// Debugging
			System.out.println("x = "+biX.toString(2));
			System.out.println("y = "+biY.toString(2));
			System.out.println("r = "+result.toString(2));

			System.out.println("x = "+biX.toString(16));
			System.out.println("y = "+biY.toString(16));
			System.out.println("r = "+c.binaryToHexString(result.toString(2),des));

			BigInteger biC=new BigInteger(numberOfF,16);
			if(result.compareTo(biC)==1)
			ef.setCarryFlag("1");
			else
			ef.setCarryFlag("0");

			System.out.println("CF: "+ef.getCarryFlag()+
			"\nPF: "+ef.getParityFlag()+
			"\nAF: "+ef.getAuxiliaryFlag()+
			"\nSF: "+ef.getSignFlag()+
			"\nZF: "+ef.getZeroFlag()+
			"\nOF: "+ef.getOverflowFlag());
			}
 		}
// 		else if ( src.isHex() ){
//			System.out.println("ADCX register to register");
//
//			String x = src.getValue();
//			String y = memory.read(des, desSize);
//
//			if(desSize == registers.getBitSize(src) && desSize == 32){
//			Calculator c = new Calculator(registers,memory);
//			EFlags ef = registers.getEFlags();
//
//			// Addition in Binary Format
//			BigInteger biX=new BigInteger(x,16);
//			BigInteger biY=new BigInteger(y,16);
//			BigInteger result=biX.add(biY);
//
//			if(ef.getCarryFlag().equals("1")) {
//			System.out.println("Add Carry Flag");
//			result.add(new BigInteger("1"));
//			System.out.println(result.toString(2) + "resulting add value");
//			}
//
//			memory.write(des,c.binaryToHexString(result.toString(2),des), desSize);
//			// Debugging
//			System.out.println("x = "+biX.toString(2));
//			System.out.println("y = "+biY.toString(2));
//			System.out.println("r = "+result.toString(2));
//
//			System.out.println("x = "+biX.toString(16));
//			System.out.println("y = "+biY.toString(16));
//			System.out.println("r = "+c.binaryToHexString(result.toString(2),des));
//
//			BigInteger biC=new BigInteger("FFFFFFFF",16);
//			if(result.compareTo(biC)==1)
//			ef.setCarryFlag("1");
//			else
//			ef.setCarryFlag("0");
//
//			System.out.println("CF: "+ef.getCarryFlag()+
//			"\nPF: "+ef.getParityFlag()+
//			"\nAF: "+ef.getAuxiliaryFlag()+
//			"\nSF: "+ef.getSignFlag()+
//			"\nZF: "+ef.getZeroFlag()+
//			"\nOF: "+ef.getOverflowFlag());
//			}
// 		}
 	}
 }