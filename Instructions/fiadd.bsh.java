execute(src, registers, memory) {
    Calculator c = new Calculator(registers, memory);
    if ( src.isMemory() ) {
		
        int size = memory.getBitSize(src);
        String value = memory.read(src, size);
		double spValue = 0.0;
		spValue = Long.parseLong(value, 16) + 0.0;
        String st0 = registers.get("ST0");
        double stValue = Double.parseDouble(st0);
		double resultingValue = stValue + spValue;
		boolean isException = c.generateFPUExceptions(registers, resultingValue);
		if(!isException){
			System.out.println(resultingValue + " value");
			registers.set("ST0", resultingValue + "");
		}
        
		registers.x87().status().set("C3",'0');
		registers.x87().status().set("C2",'0');
		registers.x87().status().set("C0",'0');
    }
}