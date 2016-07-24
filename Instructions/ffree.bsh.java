execute(src, registers, memory) {
    String st = src.getValue();
    int tag = 7 - Integer.parseInt(st.charAt(2) + "");
    if ( src.isRegister() ) {
        registers.x87().tag().setTag(tag + "", "11");
        registers.x87().status().set("C0", '0');
        registers.x87().status().set("C1", '0');
        registers.x87().status().set("C2", '0');
        registers.x87().status().set("C3", '0');
    }
}
