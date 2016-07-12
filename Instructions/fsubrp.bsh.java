
execute(registers, memory) {
	Calculator c = new Calculator(registers, memory);
	String desValue = registers.get(des);
            String srcValue = registers.get(src);
			double dbDes = Double.parseDouble(registers.get("ST1"));
			double dbSrc = Double.parseDouble(registers.get("ST0"));
			
			double resultingValue = dbSrc - dbDes;
			
			if(resultingValue > Math.pow(2,64)){
				registers.mxscr.setOverflowFlag("1");
			}
			else if(resultingValue < Math.pow(2, 64) * -1){
				registers.mxscr.setUnderflowFlag("1");
			}
			else{
				//System.out.println(resultingValue + " value");
				registers.set("ST1", resultingValue + "");
			}
		
		registers.x87().status().set("C3",'0');
		registers.x87().status().set("C2",'0');
		registers.x87().status().set("C0",'0');
		registers.x87().pop();
}