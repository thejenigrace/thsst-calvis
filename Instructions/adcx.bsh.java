execute(des, src, registers, memory) {
	String numberOfF = "";
	int bitSize = 0;
	String resultingValue = "";
	if( des.isRegister() )
		bitSize = registers.getBitSize(des);
	else if( des.isMemory() )
		bitSize = memory.getBitSize(des);
	for( int x = 0; x < bitSize/4; x++ ){
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
				if(result.toString(16).length() > registers.getHexSize(des)){
					resultingValue = result.toString(2).substring(1);
//					registers.set(des,c.binaryToHexString(c.binaryZeroExtend(resultingValue, des),des));
				}
				else{
					resultingValue = result.toString(2);
				}
				registers.set(des,c.binaryToHexString(c.binaryZeroExtend(resultingValue, des),des));
//				registers.set(des,c.binaryToHexString(c.binaryZeroExtend(resultingValue, des),des));

				BigInteger biC=new BigInteger(numberOfF,16);
				if(result.compareTo(biC)==1)
					ef.setCarryFlag("1");
				else
					ef.setCarryFlag("0");
//				ef.setOverflowFlag(c.checkOverflowAdd(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0), c.binaryZeroExtend(resultingValue, src).charAt(0)));
			}
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
				BigInteger addPlusOne=BigInteger.valueOf(new Integer(1).intValue());
				result=result.add(addPlusOne);
			}
			if(result.toString(16).length() > registers.getBitSize(src)/4){
				resultingValue = result.toString(2).substring(1);
			}
			else{
				resultingValue = result.toString(2);
			}
			registers.set(des,c.binaryToHexString(c.binaryZeroExtend(resultingValue, des),des));

			BigInteger biC=new BigInteger(numberOfF,16);
			if(result.compareTo(biC)==1)
				ef.setCarryFlag("1");
			else
				ef.setCarryFlag("0");

//		ef.setOverflowFlag(c.checkOverflowAddWithFlag(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0),
//		c.binaryZeroExtend(resultingValue, src).charAt(0), ef.getCarryFlag()));
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

			if(result.toString(16).length() > registers.getBitSize(src)/4){
				resultingValue = result.toString(2).substring(1);
			}
			else{
				resultingValue = result.toString(2);
			}

			memory.write(des,c.binaryToHexString(c.binaryZeroExtend(resultingValue, des),des), desSize);


			BigInteger biC=new BigInteger(numberOfF,16);
			if(result.compareTo(biC)==1)
				ef.setCarryFlag("1");
			else
				ef.setCarryFlag("0");
//		ef.setOverflowFlag(c.checkOverflowAddWithFlag(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0),
//		c.binaryZeroExtend(resultingValue, src).charAt(0), ef.getCarryFlag()));
				}
	        }
	    }
	}
// }