execute(registers, memory) {
    String st0 = registers.get("ST0");
    String st1 = "0.0";

    Double double1 = Double.parseDouble(st0);
    Double double2 = Double.parseDouble(st1);

    if ( double1.compareTo(double2) > 0 ) {
        registers.x87().status().set("C3", '0');
        registers.x87().status().set("C2", '0');
        registers.x87().status().set("C0", '0');
    } else if ( double1.compareTo(double2) < 0 ) {
        registers.x87().status().set("C3", '0');
        registers.x87().status().set("C2", '0');
        registers.x87().status().set("C0", '1');
    } else if ( double1.compareTo(double2) == 0 ) {
        registers.x87().status().set("C3", '1');
        registers.x87().status().set("C2", '0');
        registers.x87().status().set("C0", '0');
    } else if ( double1.isNaN() || double2.isNaN() ) {
        registers.x87().status().set("C3", '1');
        registers.x87().status().set("C2", '1');
        registers.x87().status().set("C0", '1');
        registers.x87().status().setInvalidOperationFlag();
    }
    registers.x87().status().set("C1", '0');

}
