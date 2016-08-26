execute(registers, memory) {
    String st0 = registers.get("ST0");
    Double result = Double.parseDouble(st0);
    Converter converter = new Converter(st0);
    st0 = converter.toDoublePrecisionHex();
    // examine st0
    String first = st0.charAt(0) + "";
    converter = new Converter(first);
    first = converter.toBinaryString();
    registers.x87().status().set("C1", first.charAt(0));

    String top = registers.x87().status().getTop() + "";
    if ( registers.x87().tag().getTag(top).equals("11") ) { // empty
        registers.x87().status().set("C3", '1');
        registers.x87().status().set("C2", '0');
        registers.x87().status().set("C0", '1');
    } else if ( result.isNaN() ) {
        registers.x87().status().set("C3", '0');
        registers.x87().status().set("C2", '0');
        registers.x87().status().set("C0", '1');
    } else if ( result.isInfinite() ) {
        registers.x87().status().set("C3", '0');
        registers.x87().status().set("C2", '1');
        registers.x87().status().set("C0", '1');
    } else if ( result.compareTo(new Double(0.0d)) == 0 ) {
        registers.x87().status().set("C3", '1');
        registers.x87().status().set("C2", '0');
        registers.x87().status().set("C0", '0');
    } else {
        registers.x87().status().set("C3", '0');
        registers.x87().status().set("C2", '1');
        registers.x87().status().set("C0", '0');
    }
}
