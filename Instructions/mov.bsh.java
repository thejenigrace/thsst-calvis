execute(des, src, registers, memory){
	if ( des.isRegister() ) {
		if ( src.isRegister() ) {
			String x = registers.get(src);
 		    registers.set(des, x);
	    }
	    else if ( src.isHex() ) {
	        String x = src.getValue();
	        registers.set(des, x);
		}
	    else if ( src.isMemory() ) {
	        int des_reg_size = registers.getBitSize(des);
	        String x = memory.read(src, des_reg_size);
	        registers.set(des, x);
	    }
 	}
 	else if ( des.isMemory() ) {
		if ( src.isRegister() ) {
 		    String x = registers.get(src);
 		    int src_reg_size = registers.getBitSize(src);
 		    memory.write(des, x, src_reg_size);
 		}
	    else if ( src.isHex() ) {
	        String x = src.getValue();
			memory.write(des, x, des);
		}
	}
}
