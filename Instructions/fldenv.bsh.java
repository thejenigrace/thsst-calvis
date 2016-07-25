execute(src, registers, memory) {
    if ( src.isMemory() ) {
      String env = memory.read(src, 48);

      String tag = env.substring(0, 4);
      String status = env.substring(4, 8);
      String control = env.substring(8, 12);

      registers.x87().control().setValue(control);
      registers.x87().status().setValue(status);
      registers.x87().tag().setValue(tag);
    }
}
