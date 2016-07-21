execute(registers, memory) {
//    String st0 = registers.get("ST0");
//    String st1 = registers.get("ST1");
//
//	double qVal = Double.parseDouble(st0) / Double.parseDouble(st1);
//	double remainder = Double.parseDouble(st0) - (qVal.intValue * Double.parseDouble(st1));
//	double d = Double.parseDouble(st0) / Double.parseDouble(st1);
//    // divide st0 / st1, IEEE remainder goes to st0;
//    registers.set("ST0", st0);
		double st0 = Double.parseDouble(registers.get("ST0"));
		double st1 = Double.parseDouble(registers.get("ST1"));
		registers.set("ST0", (st0 % st1) + "");
}
