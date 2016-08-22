execute(des, src, registers, memory) {
	 if ( src.isMemory() ) {
		if ( des.isRegister() && registers.getBitSize(des) == 64) {
			String x = registers.get(des);
			int desRegSize = registers.getBitSize(des);
			memory.write(src, x, desRegSize);
		}
		else{
			throw new Exception();
		}
	}
}
