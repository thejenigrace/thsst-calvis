 execute(registers, memory){
   System.out.println("CLC");

   EFlags flags = registers.getEFlags();

	 flags.setCarryFlag("0");
 }
