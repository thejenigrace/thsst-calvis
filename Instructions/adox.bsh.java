execute(des, src, registers, memory) {
 	if ( des.isRegister() ){
 		if ( src.isRegister() ){
 			System.out.println("ADOX register to register");

            String x = registers.get(src);
            String y = registers.get(des);

			if(registers.getBitSize(des) == registers.getBitSize(src)){
                Calculator c = new Calculator(registers,memory);
                EFlags ef = registers.getEFlags();

				// Addition in Binary Format
				BigInteger biX=new BigInteger(x,16);
				BigInteger biY=new BigInteger(y,16);
				BigInteger result=biX.add(biY);

                if(ef.getOverflowFlag() == "1") {
                    System.out.println("Add Overflow Flag");
                    result.add(new BigInteger("1"));
                }

				registers.set(des,c.binaryToHexString(result.toString(2),des));

				// Debugging
				System.out.println("x = "+biX.toString(2));
				System.out.println("y = "+biY.toString(2));
				System.out.println("r = "+result.toString(2));

				System.out.println("x = "+biX.toString(16));
				System.out.println("y = "+biY.toString(16));
				System.out.println("r = "+c.binaryToHexString(result.toString(2),des));

				BigInteger biC=new BigInteger("FFFFFFFF",16);
				if(result.compareTo(biC)==1)
				ef.setCarryFlag("1");
				else
				ef.setCarryFlag("0");

				ef.setParityFlag(c.checkParity(result.toString(2)));

				ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));

				String sign=""+result.toString(2).charAt(0);
				ef.setSignFlag(sign);

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
 		else if ( src.isHex() ) {
            System.out.println("ADOX immediate to register");

            String x = src.getValue();
            String y = registers.get(des);

                Calculator c = new Calculator(registers,memory);
                EFlags ef = registers.getEFlags();

                // Addition in Binary Format
                BigInteger biX=new BigInteger(x,16);
                BigInteger biY=new BigInteger(y,16);
                BigInteger result=biX.add(biY);

                if(ef.getCarryFlag() == "1")
                    result.add(new BigInteger("1"));

                registers.set(des,c.binaryToHexString(result.toString(2),des));

                // Debugging
                System.out.println("x = "+biX.toString(2));
                System.out.println("y = "+biY.toString(2));
                System.out.println("r = "+result.toString(2));

                System.out.println("x = "+biX.toString(16));
                System.out.println("y = "+biY.toString(16));
                System.out.println("r = "+c.binaryToHexString(result.toString(2),des));

                BigInteger biC=new BigInteger("FFFFFFFF",16);
                if(result.compareTo(biC)==1)
                ef.setCarryFlag("1");
                else
                ef.setCarryFlag("0");

                ef.setParityFlag(c.checkParity(result.toString(2)));

				ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));

                String sign=""+result.toString(2).charAt(0);
                ef.setSignFlag(sign);

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
 		else if ( src.isMemory() && registers.getBitSize(des) == memory.getBitSize(src)){
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

			if(ef.getCarryFlag() == "1")
				result.add(new BigInteger("1"));

			registers.set(des,c.binaryToHexString(result.toString(2),des));

			// Debugging
			System.out.println("x = "+biX.toString(2));
			System.out.println("y = "+biY.toString(2));
			System.out.println("r = "+result.toString(2));

			System.out.println("x = "+biX.toString(16));
			System.out.println("y = "+biY.toString(16));
			System.out.println("r = "+c.binaryToHexString(result.toString(2),des));

			BigInteger biC=new BigInteger("FFFFFFFF",16);
			if(result.compareTo(biC)==1)
			ef.setCarryFlag("1");
			else
			ef.setCarryFlag("0");

			ef.setParityFlag(c.checkParity(result.toString(2)));

			ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));

			String sign=""+result.toString(2).charAt(0);
			ef.setSignFlag(sign);

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
 	}
 	else if ( des.isMemory() ){
		int desSize = memory.getBitSize(des);
 		if ( src.isRegister() ){
			String x = registers.get(src);
			String y = memory.read(des, desSize);

			if(desSize == registers.getBitSize(src)){
			Calculator c = new Calculator(registers,memory);
			EFlags ef = registers.getEFlags();

			// Addition in Binary Format
			BigInteger biX=new BigInteger(x,16);
			BigInteger biY=new BigInteger(y,16);
			BigInteger result=biX.add(biY);

			if(ef.getOverflowFlag() == "1") {
			System.out.println("Add Overflow Flag");
			result.add(new BigInteger("1"));
			}

			memory.write(des,c.binaryToHexString(result.toString(2),des), desSize);

			// Debugging
			System.out.println("x = "+biX.toString(2));
			System.out.println("y = "+biY.toString(2));
			System.out.println("r = "+result.toString(2));

			System.out.println("x = "+biX.toString(16));
			System.out.println("y = "+biY.toString(16));
			System.out.println("r = "+c.binaryToHexString(result.toString(2),des));

			BigInteger biC=new BigInteger("FFFFFFFF",16);
			if(result.compareTo(biC)==1)
			ef.setCarryFlag("1");
			else
			ef.setCarryFlag("0");

			ef.setParityFlag(c.checkParity(result.toString(2)));

			ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));

			String sign=""+result.toString(2).charAt(0);
			ef.setSignFlag(sign);

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
 		else if ( src.isHex() ){
			String x = src.getValue();
			String y = memory.read(des, desSize);

			Calculator c = new Calculator(registers,memory);
			EFlags ef = registers.getEFlags();

			// Addition in Binary Format
			BigInteger biX=new BigInteger(x,16);
			BigInteger biY=new BigInteger(y,16);
			BigInteger result=biX.add(biY);

			if(ef.getOverflowFlag() == "1") {
			System.out.println("Add Overflow Flag");
			result.add(new BigInteger("1"));
			}

			memory.write(des,c.binaryToHexString(result.toString(2),des), desSize);

			// Debugging
			System.out.println("x = "+biX.toString(2));
			System.out.println("y = "+biY.toString(2));
			System.out.println("r = "+result.toString(2));

			System.out.println("x = "+biX.toString(16));
			System.out.println("y = "+biY.toString(16));
			System.out.println("r = "+c.binaryToHexString(result.toString(2),des));

			BigInteger biC=new BigInteger("FFFFFFFF",16);
			if(result.compareTo(biC)==1)
			ef.setCarryFlag("1");
			else
			ef.setCarryFlag("0");

			ef.setParityFlag(c.checkParity(result.toString(2)));

			ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));

			String sign=""+result.toString(2).charAt(0);
			ef.setSignFlag(sign);

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
 }