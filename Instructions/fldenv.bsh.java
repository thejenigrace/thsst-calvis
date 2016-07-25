execute(src, registers, memory) {
//    registers.x87().status().set("C0", '0');
//    registers.x87().status().set("C1", '0');
//    registers.x87().status().set("C2", '0');
//    registers.x87().status().set("C3", '0');
//    if ( src.isMemory() ) {
//        int size = memory.getBitSize(src);
//        if ( size == 16 ) {
//            String env = memory.read(src, 48);
//
//            String tag = env.substring(0, 4);
//            String status = env.substring(4, 8);
//            String control = env.substring(8, 12);
//
//            registers.x87().control().setValue(control);
//            registers.x87().status().setValue(status);
//            registers.x87().tag().setValue(tag);
//
//        } else if ( size == 32 ) {
//            // special commands
//            String env = memory.read(src, 48);
//
//            String tag = env.substring(0, 4);
//            String status = env.substring(4, 8);
//            String control = env.substring(8, 12);
//
//            registers.x87().control().setValue(control);
//            registers.x87().status().setValue(status);
//            registers.x87().tag().setValue(tag);
//
//        }
//    }
}
