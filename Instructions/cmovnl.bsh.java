execute(des, src, registers, memory){
	Calculator cal = new Calculator(registers, memory);
	boolean initialCondition = cal.evaluateCondition("NL");
	System.out.println("Should I move: " + initialCondition);

	/**
	 * If condition is true, carry out the move
	 */
	if ( initialCondition ){
		if ( des.isRegister() ) {
			if ( src.isRegister() ) {
				System.out.println("Moving register to register");
				String x = registers.get(src);
				registers.set(des, x);
			}
			else if ( src.isMemory() ) {
				System.out.println("Moving memory to register");
				int des_reg_size = registers.getBitSize(des);
				String x = memory.read(src, des_reg_size);
				registers.set(des, x);
			}
		}
	}
}
