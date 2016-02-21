execute(cc, des, src, registers, memory) {
	Calculator cal = new Calculator(registers, memory);
	boolean initialCondition = cal.evaluateCondition(cc.getValue());

	/**
	 * If condition is true, carry out the move
	 */
	if ( initialCondition ) {
		if ( des.isRegister() ) {
			if ( src.isRegister() ) {
				String x = registers.get(src);
				registers.set(des, x);
			}
			else if ( src.isMemory() ) {
				int desRegSize = registers.getBitSize(des);
				String x = memory.read(src, desRegSize);
				registers.set(des, x);
			}
		}
	}
}
