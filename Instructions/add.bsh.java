execute(des, src, registers, memory) {
 	if ( des.isRegister() ){
        if ( src.isRegister() ){
 			System.out.println("ADD register to register");

            String x = registers.get(src);
            String y = registers.get(des);

            Calculator c = new Calculator(registers, memory);
            EFlags ef = registers.getEFlags();
//
            if(registers.getBitSize(des) == registers.getBitSize(src)){
                x = c.hexToBinaryString(x, src);
                y = c.hexToBinaryString(y, des);

                // Addition in Binary Format
                BigInteger biX = new BigInteger(x, 2);
                BigInteger biY = new BigInteger(y, 2);
                BigInteger result = biX.add(biY);
                registers.set(des, result.toString(16));

                // Debugging
                System.out.println("x = " + x);
                System.out.println("y = " + y);
                System.out.println("r = " + result.toString(16));

                ef.setCarryFlag("0");

                ef.setAuxiliaryFlag("0");

                String sign = "" + result.toString(2).charAt(0);
                ef.setSignFlag(sign);

                if(result.equals(BigInteger.ZERO)) {
                    ef.setZeroFlag("1");
                }
                else {
                    ef.setZeroFlag("0");
                }

                ef.setOverflowFlag("0");

            }
 		}
 		else if ( src.isHex() ) {
// 			System.out.println("Adding immediate to register");
 			String x = src.getValue();
		 	String y = registers.get(des);

 			registers.set(des, x);
 		}
 		else if ( src.isMemory() ){
// 			System.out.println("Adding memory to register");
 			int des_reg_size = registers.getSize(des);
 			String x = memory.read(src, des_reg_size);
 			registers.set(des, x);
 		}
 	}
 	else if ( des.isMemory() ){
 		if ( src.isRegister() ){
 			System.out.println("Adding register to memory");
 			String x = registers.get(src);
 			int src_reg_size = registers.getSize(src);
 			memory.write(des, x, src_reg_size);
 		}
 		else if ( src.isHex() ){
 			System.out.println("Adding immediate to memory");
 			String x = src.getValue();

 			/* we need to know how big of a memory chunk
 				we'll need for the memory.write()
 			 	Pass the des token as a parameter
 			 	des contains the keywords byte, word, dword to denote the size
 			 */
 			memory.write(des, x, des);
 		}
 	}
 }