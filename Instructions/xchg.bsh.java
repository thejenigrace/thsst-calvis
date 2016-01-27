 execute(des, src, registers, memory) {
 	if ( des.isRegister() ){
 		if ( src.isRegister() ) { 
 			System.out.println("Exchange register to register");
 			String x = registers.get(src);
 			String y = registers.get(des);
 			registers.set(des, x);
 			registers.set(src, y);
 		}
 		else if ( src.isMemory() ){
 			System.out.println("Exchange memory to register");
 			EnvironmentConfiguration.model.Calculator cal = new EnvironmentConfiguration.model.Calculator(registers, memory);
 			String address = cal.computeMemoryAddress(src);
			int des_reg_size = registers.getSize(des);
 			String x = memory.read(address, des_reg_size);

 			// get value of registers
 			String y = registers.get(des);
 			// write value to memory
 			memory.write(address, y, des_reg_size);
 			// set register to value read from memory;
 			registers.set(des, x);

 		}
 	}
 	else if ( des.isMemory() ){
 		EnvironmentConfiguration.model.Calculator cal = new EnvironmentConfiguration.model.Calculator(registers, memory);
 		String address = cal.computeMemoryAddress(des);

 		if ( src.isRegister() ){
 			System.out.println("Exchange register to memory");
 			String x = registers.get(src);
 			int src_reg_size = registers.getSize(src);
 			

 			// read from memory
 			String y = memory.read(address, src_reg_size);
 			// set register to value read from memory
 			registers.set(src, y);
 			// write register value to memory
			memory.write(address, x, src_reg_size);
 		}
 	}
 }