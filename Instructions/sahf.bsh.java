 execute(registers, memory){
   System.out.println("SAHF");

   Calculator calculator = new Calculator(registers, memory);
   EFlags flags = registers.getEFlags();
   Token token = new Token(Token.REG, "AH");
   String sAH = calculator.hexToBinaryString(registers.get("AH"), token);

	 String sf = sAH.charAt(0) + "";
   String zf = sAH.charAt(1) + "";
   String af = sAH.charAt(3) + "";
   String pf = sAH.charAt(5) + "";
   String cf = sAH.charAt(7) + "";

   flags.setSignFlag(sf);
   flags.setZeroFlag(zf);
   flags.setAuxiliaryFlag(af);
   flags.setParityFlag(pf);
   flags.setCarryFlag(cf);
 }
