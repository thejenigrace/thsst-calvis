execute(des, src, registers, memory) {
    String st0 = "";
    String st1 = "";

    Double d1;
    Double d2;

    if ( des.isRegister() && registers.getBitSize(des) == 80 ) {
        st0 = registers.get(des);
        d1 = Double.parseDouble(st0);
    }

    if ( src.isRegister() && registers.getBitSize(src) == 80 ) {
        st1 = registers.get(src);
        d2 = Double.parseDouble(st1);
    }

    if ( d1.compareTo(d2) > 0 ) {
        registers.getEFlags().setZeroFlag("0");
        registers.getEFlags().setParityFlag("0");
        registers.getEFlags().setCarryFlag("0");
    } else if ( d1.compareTo(d2) < 0 ) {
        registers.getEFlags().setZeroFlag("0");
        registers.getEFlags().setParityFlag("0");
        registers.getEFlags().setCarryFlag("1");
    } else if ( d1.compareTo(d2) == 0 ) {
        registers.getEFlags().setZeroFlag("1");
        registers.getEFlags().setParityFlag("0");
        registers.getEFlags().setCarryFlag("0");
    } else if ( d1.isNaN() || d2.isNaN() ) {
        registers.getEFlags().setZeroFlag("1");
        registers.getEFlags().setParityFlag("1");
        registers.getEFlags().setCarryFlag("1");
        registers.x87().status().setInvalidOperationFlag();
    }

    registers.x87().status().set("C1", '0');

    registers.getEFlags().setOverflowFlag("0");
    registers.getEFlags().setSignFlag("0");
    registers.getEFlags().setAuxiliaryFlag("0");

}
