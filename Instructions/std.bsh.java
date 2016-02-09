execute(registers, memory) {
  System.out.println("STD");

  EFlags flags = registers.getEFlags();

  flags.setDirectionFlag("1");
}
