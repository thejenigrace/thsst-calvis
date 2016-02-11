 execute(registers, memory){
   System.out.println("STI");

   EFlags flags = registers.getEFlags();

	 flags.setInterruptFlag("1");
 }
