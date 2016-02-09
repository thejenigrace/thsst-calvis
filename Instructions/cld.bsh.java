execute(registers, memory) {
  System.out.println("CLD");

  EFlags flags = registers.getEFlags();

  flags.setDirectionFlag("0");
}
