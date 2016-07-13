execute(des, registers, memory) {
    String value = registers.x87().peek();
//    if ( des.isRegister() ) {
//        if ( registers.getBitSize(des) == 80 ) {
//            registers.set(des, value);
//        }
//    }
//    else if ( des.isMemory() ) {
//        int size = memory.getBitSize(des);
//        if ( size == 32 ) {
//            // conversion to single precision
//            float floatValue = Float.parseFloat(value);
//            value = Integer.toHexString(Float.floatToIntBits(floatValue));
//        } else if ( size == 64 ) {
//            // conversion to double precision
//            Double doubleValue = Double.parseDouble(value);
//            value = Long.toHexString(Double.doubleToLongBits(doubleValue));
//        }
//        memory.write(des, value, des);
//    }
}
