execute(des,src,registers,memory) {
  int desBitSize = 128;
  int desHexSize = desBitSize / 4;
	int srcHexSize;
  String srcValue;
	String desValue;

    if( src.isMemory() ) {
		// srcValue = memory base address
		srcValue = memory.removeSizeDirectives(src.getValue());
		// System.out.println("src-memory base address: " + srcValue);

		// SOURCE == MEMORY; checks the memory alignment
		if ( !srcValue.substring(srcValue.length() - 1).equals("0") ){
			throw new MemoryAlignmentException(src.getValue());
		}

		// get the content of the memory
		srcValue = memory.read(src, desBitSize);
	} else if ( src.isRegister() ) {
		// get the content of the register
		srcValue = registers.get(src);
	}

	if( des.isMemory() ) {
		// desValue = memory base address
		desValue = memory.removeSizeDirectives(des.getValue());
		// System.out.println("des-memory base address: " + desValue);

		// DESTINATION == MEMORY; checks the memory alignment
		if ( !desValue.substring(desValue.length() - 1).equals("0") ){
			throw new MemoryAlignmentException(des.getValue());
		}

		// Insert SOURCE to DESTINATION
		memory.write(des, srcValue, desBitSize);
	} else if ( des.isRegister() ) {
		// Insert SOURCE to DESTINATION
		registers.set(des, srcValue);
	}
}
