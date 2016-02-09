 execute(registers, memory){
   System.out.println("LAHF");

   Calculator calculator = new Calculator(registers, memory);
   EFlags flags = registers.getEFlags();
   Token token = new Token(Token.REG, "AH");
   String sAH = calculator.hexToBinaryString(registers.get("AH"), token);

	 String sf = flags.getSignFlag();
   String zf = flags.getZeroFlag();
   String af = flags.getAuxiliaryFlag();
   String pf = flags.getParityFlag();
   String cf = flags.getCarryFlag();

   StringBuffer buffer = new StringBuffer(sAH);
   buffer.setCharAt(0, sf.charAt(0));
   buffer.setCharAt(1, zf.charAt(0));
   buffer.setCharAt(3, af.charAt(0));
   buffer.setCharAt(5, pf.charAt(0));
   buffer.setCharAt(7, cf.charAt(0));

   String result = calculator.binaryToHexString(buffer.toString(), token);

   registers.set(token, result);
 }
