execute(des, src, registers, memory){
		if ( des.isRegister() ){
			if ( src.isRegister() ){
				System.out.println("Moving register to register");
				String x = registers.get(src);
 			    registers.set(des, x);
	        }
	        else if ( src.isHex() ) {
	            System.out.println("Moving immediate to register");
	            String x = src.getValue();
	            registers.set(des, x);
	        }
	        else if ( src.isMemory() ){
	            System.out.println("Moving memory to register");
	            int des_reg_size = registers.getBitSize(des);
	            String x = memory.read(src, des_reg_size);
	            registers.set(des, x);
	        }
 	    }
 	    else if ( des.isMemory() ){
 		    if ( src.isRegister() ){
 			    System.out.println("Moving register to memory");
 			    String x = registers.get(src);
 			    int src_reg_size = registers.getBitSize(src);
 			    memory.write(des, x, src_reg_size);
 		    }
	        else if ( src.isHex() ){
	            System.out.println("Moving immediate to memory");
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