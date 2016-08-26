execute(registers, memory) {
    double prevValue = Double.parseDouble(registers.get("ST0"));
    double st0 = Math.round(prevValue);
    // round st0 to nearest integral value
    if(st0 > prevValue) {
        registers.x87().status().set("C1",'1');
    }
    else{
        registers.x87().status().set("C1",'0');
    }
    registers.set("ST0", st0 + "");
    registers.x87().status().set("C2",'0');
    registers.x87().status().set("C3",'0');
    registers.x87().status().set("C0",'0');
}
