execute(des, registers, memory) {
  if ( des.isMemory() ) {
      String control = registers.x87().control().getValue();
      String status = registers.x87().status().getValue();
      String tag = registers.x87().tag().getValue();

      // environment has tag, status, and control
      String env = tag + status + control;

      // environment has MXSCR register
      String mxscr = registers.getMxscr().getValue();
      env = mxscr + env;

      // environment has ST registers
      for( int i = 0; i < 8; i++ ) {
        String st = registers.get("ST" + i);
        Converter con = new Converter(st);
        st = con.toDoublePrecisionHex();
        while ( st.length() < 16 ) {
          st = "0" + st;
        }
        env = st + env;
      }

      // environment has MMX registers
      for( int i = 0; i < 8; i++ ) {
        String mm = registers.get("MM" + i);
        env = mm + env;
      }

      // environment has XMM registers
      for( int i = 0; i < 8; i++ ) {
        String xmm = registers.get("XMM" + i);
        env = xmm + env;
      }

      System.out.println(env);

      memory.write(des, env, 2128);

  }
}
