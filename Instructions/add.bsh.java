execute(des, src, registers, memory) {
  int desBitSize;
  String desValue;
  String srcValue;

  // Checks if DESTINATION is either REGISTER or MEMORY
	if ( des.isRegister() ) {
		System.out.println("des: register");
		desBitSize = registers.getBitSize(des);
		desValue = registers.get(des);
	} else if( des.isMemory() && src.isRegister() ){
		System.out.println("des: memory");
		desBitSize = registers.getBitSize(src);
		desValue = memory.read(des, desBitSize);
	} else {
		System.out.println("des: memory");
		desBitSize = memory.getBitSize(des);
		desValue = memory.read(des, desBitSize);
	}

  // Checks if SOURCE is either REGISTER, MEMORY or IMMEDIATE
  if ( src.isRegister() ) {
		System.out.println("src: register");
		srcValue = registers.get(src);
	} else if ( src.isMemory() ) {
		System.out.println("src: memory");
		srcValue = memory.read(src, desBitSize);
	} else if( src.isHex() ){
		System.out.println("src: immediate");
		srcValue = src.getValue();
	}

  Calculator calculator = new Calculator(registers, memory);
  EFlags eFlags = registers.getEFlags();

  // ADDITION
  BigInteger biDesValue = new BigInteger(desValue, 16);
  BigInteger biSrcValue = new BigInteger(srcValue, 16);
  BigInteger biResult = biDesValue.add(biSrcValue);

  System.out.println(biDesValue.toString(16) + ": " + biDesValue.toString());
	System.out.println(biSrcValue.toString(16) + ": " + biSrcValue.toString());
	System.out.println(biResult.toString(16) + ": " + biResult.toString());

	String finalResult;
	if ( biResult.toString(16).length() > desBitSize/4 )
	  finalResult = biResult.toString(16).substring(1);
	else
	  finalResult = biResult.toString(16);

	System.out.println("finalResult: " + finalResult);

	// Check if DESTINATION is either REGISTER or MEMORY
	if ( des.isRegister() ) {
		System.out.println("des: register; result out");
		registers.set( des, finalResult );
	} else if ( des.isMemory() ) {
		System.out.println("des: memory; result out");
		memory.write( des, finalResult, desBitSize );
	}

	String desHexValue = biDesValue.toString(16);
	String srcHexValue = biSrcValue.toString(16);
	String resultHexValue = biResult.toString(16);

	String desBinaryValue = biDesValue.toString(2);
	String srcBinaryValue = biSrcValue.toString(2);
	String resultBinaryValue = biResult.toString(2);

	// Create the MAX HEX VALUE depends on the HEX SIZE of DESTINATION
	String maxHexValue = "";
	for(int i = 0; i < desBitSize/4; i++)
		maxHexValue = maxHexValue + "F";

	System.out.println("maxHexValue: " + maxHexValue);

	// Checks Carry Flag
  BigInteger biCF = new BigInteger(maxHexValue, 16);
  if ( biResult.compareTo(biCF) == 1 )
    eFlags.setCarryFlag("1");
  else
    eFlags.setCarryFlag("0");

	// Checks Parity Flag
	String resultBinaryValueZeroExtend = calculator.binaryZeroExtend(resultBinaryValue, des);
	String parityFlag = calculator.checkParity( resultBinaryValueZeroExtend );
	eFlags.setParityFlag( parityFlag );

	// Checks Auxialiary Flag
	 String auxialiaryFlag = calculator.checkAuxiliary( srcHexValue, desHexValue );
	 eFlags.setAuxiliaryFlag( auxialiaryFlag );

	// Checks Sign Flag
	if( biResult.testBit(desBitSize - 1) )
	 	eFlags.setSignFlag("1");
	else
		eFlags.setSignFlag("0");

	// Checks Zero Flag
	if( finalResult.equals("0") )
		eFlags.setZeroFlag("1");
	else
		eFlags.setZeroFlag("0");

	// Checks Overflow Flag
  char desMSB = calculator.binaryZeroExtend(desBinaryValue, des).charAt(0);
  char srcMSB = calculator.binaryZeroExtend(srcBinaryValue, src).charAt(0);
  char resultMSB = calculator.binaryZeroExtend(resultBinaryValue, des).charAt(0);

  String overflowFlag = calculator.checkOverflowAddWithFlag( srcMSB, desMSB, resultMSB, eFlags.getOverflowFlag() );
  eFlags.setOverflowFlag( overflowFlag );
}
