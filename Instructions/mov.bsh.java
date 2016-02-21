execute(des, src, registers, memory) {
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
			int desRegSize = registers.getBitSize(des);
			String x = memory.read(src, desRegSize);
			registers.set(des, x);
		}
	}
	else if ( des.isMemory() ) {
		if ( src.isRegister() ) {
			String x = registers.get(src);
			int srcRegSize = registers.getBitSize(src);
			memory.write(des, x, srcRegSize);
		}
		else if ( src.isHex() ) {
			String x = src.getValue();
			memory.write(des, x, des);
		}
	}
}
