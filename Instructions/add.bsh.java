 execute(des, src, registers, memory) {
 	if ( des.isRegister() ){
 		if ( src.isRegister() ){
// 			System.out.println("Add register to register");
         String x = registers.get(src);
         String y = registers.get(des);

         Calculator c = new Calculator(registers,memory);
//         EFlags flags = registers.getEFlags();
//         flags.set
         if(registers.getBitSize(des)==registers.getBitSize(src)){
             x=c.hexToBinaryString(x,src);
             y=c.hexToBinaryString(y,des);

            int result=Integer.parseInt(x,2)+Integer.parseInt(y,2);
            registers.set(des,c.binaryToHexString(Integer.toBinaryString(result),des));


            System.out.println("x = "+Integer.parseInt(x,2));
            System.out.println("y = "+Integer.parseInt(y,2));
            System.out.println("result = "+Integer.toBinaryString(result));
         }
 		}
 		else if ( src.isHex() ) {
// 			System.out.println("Adding immediate to register");
 			String x = src.getValue();
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