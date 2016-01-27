execute(des, src, registers, memory){
	if ( src.isMemory() && des.isRegister() ){
		EnvironmentConfiguration.model.Calculator cal = new EnvironmentConfiguration.model.Calculator(registers, memory);
		String address = cal.computeEffectiveAddress(src);

		int des_reg_size = registers.getSize(des);
		// r32, m32 ; r16, m16
		if ( (des_reg_size == 32 || des_reg_size == 16) && ((des_reg_size / 4) == address.length()) ){
			registers.set(des, address);
		}
		// r16, m32
		else if ( des_reg_size == 16 && address.length() == 8 ){
			address = address.substring(4,8); // get lower half
			registers.set(des, address);
		}
		// r32, m16
		else if ( des_reg_size == 32 && address.length() == 4 ){
			address = "0000" + address; // zero extend
			registers.set(des, address);
		}
		else {
			// throw invalid parameters for lea
			System.out.println("Invalid parameters for LEA");
		}
	}
}