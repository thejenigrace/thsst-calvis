execute(des, registers, memory) {
    if ( des.isMemory() ) {
        String control = registers.x87().control().getValue();
        String status = registers.x87().status().getValue();
        String tag = registers.x87().tag().getValue();

        String env = tag + status + control;

        for( int i = 0; i < 8; i++ ) {
            String st = registers.get("ST" + i);
            Converter con = new Converter(st);
            st = con.toDoublePrecisionHex();
            while ( st.length() < 16 ) {
                st = "0" + st;
            }
            env = st + env;
        }

        memory.write(des, env, 560);

        int top = registers.x87().status().getTop();
        while ( top < 8 ) {
            top++;
            registers.x87().rotateBarrel(1); // rotate right to simulate pop
        }

        registers.x87().status().initializeValue();
        registers.x87().control().initializeValue();
        registers.x87().tag().initializeValue();
        registers.x87().refreshST();
    }
}
