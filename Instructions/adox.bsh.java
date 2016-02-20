execute(des, src, registers, memory) {
 	if ( des.isRegister() ){
 		if ( src.isRegister() ){
 			System.out.println("ADOX register to register");

            String x = registers.get(src);
            String y = registers.get(des);

			if(registers.getBitSize(des) == registers.getBitSize(src) && registers.getBitSize(des) == 32){
                Calculator c = new Calculator(registers,memory);
                EFlags ef = registers.getEFlags();

				// Addition in Binary Format
				BigInteger biX=new BigInteger(x,16);
				BigInteger biY=new BigInteger(y,16);
				BigInteger result=biX.add(biY);

                if(ef.getOverflowFlag().equals("1")) {
                    System.out.println("Add Overflow Flag");
                    result = result.add(new BigInteger("1"));
                }

				registers.set(des,c.binaryToHexString(result.toString(2),des));

				ef.setOverflowFlag("0");

			}
 		}

 		else if ( src.isMemory() && registers.getBitSize(des) == memory.getBitSize(src) && memory.getBitSize(src) == 32){
			System.out.println("ADOX immediate to register");
			int srcSize = memory.getBitSize(src);
			String x = memory.read(srcSize);
			String y = registers.get(des);

			if(x.length() == y.length()){
			Calculator c = new Calculator(registers,memory);
			EFlags ef = registers.getEFlags();

			// Addition in Binary Format
			BigInteger biX=new BigInteger(x,16);
			BigInteger biY=new BigInteger(y,16);
			BigInteger result=biX.add(biY);

			if(ef.getOverflowFlag().equals("1"))
				result = result.add(new BigInteger("1"));

			registers.set(des,c.binaryToHexString(result.toString(2),des));
			ef.setOverflowFlag(c.checkOverflowAddWithFlag(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0),
			c.binaryZeroExtend(resultingValue, src).charAt(0), ef.getOverflowFlag()));
			}
 		}
 	}
 	else if ( des.isMemory() ){
		int desSize = memory.getBitSize(des);
 		if ( src.isRegister() && registers.getBitSize(src) == 32){
			String x = registers.get(src);
			String y = memory.read(des, desSize);

			if(desSize == registers.getBitSize(src)){
			Calculator c = new Calculator(registers,memory);
			EFlags ef = registers.getEFlags();

			// Addition in Binary Format
			BigInteger biX=new BigInteger(x,16);
			BigInteger biY=new BigInteger(y,16);
			BigInteger result=biX.add(biY);

			if(ef.getOverflowFlag().equals("1")) {
			System.out.println("Add Overflow Flag");
				result = result.add(new BigInteger("1"));
			}

			memory.write(des,c.binaryToHexString(result.toString(2),des), desSize);
			ef.setOverflowFlag(c.checkOverflowAddWithFlag(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0),
			c.binaryZeroExtend(resultingValue, src).charAt(0), ef.getOverflowFlag()));
			}
 		}
 	}
 }