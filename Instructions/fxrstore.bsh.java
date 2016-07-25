execute(src, registers, memory) {
  if ( src.isMemory() ) {
      String env = memory.read(src, 2128);

      // environment has XMM registers
      int start = 0;
      int end = 32;
      for( int i = 7; i >= 0; i-- ) {
        String xmm = env.substring(start, end);
        start += 32;
        end += 32;
        registers.set("XMM" + i, xmm);
      }

      // environment has MMX registers
      end -= 16;
      for( int i = 7; i >= 0; i-- ) {
        String mm = env.substring(start, end);
        start += 16;
        end += 16;
        registers.set("MM" + i, mm);
      }

      // environment has ST registers
      for( int i = 7; i >= 0; i-- ) {
        String st = env.substring(start, end);
        Converter converter = new Converter(st);
        st = converter.toDoublePrecision() + "";
        start += 16;
        end += 16;
        registers.set("ST" + i, st);
      }

      // environment has MXSCR register
      end -= 8;
      String mxscr = env.substring(start, end);
      registers.getMxscr().setValue(mxscr);
      start += 8;
      end += 8;

      // environment fpu
      end -= 4;
      String tag = env.substring(start, end);
      String status = env.substring(start + 4, end + 4);
      String control = env.substring(start + 8, end + 8);

      registers.x87().control().setValue(control);
      registers.x87().status().setValue(status);
      registers.x87().tag().setValue(tag);

  }
}
