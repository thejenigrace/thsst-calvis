 execute(registers, memory){
   System.out.println("CLI");

   EFlags flags = registers.getEFlags();

	 flags.setInterruptFlag("0");
 }
