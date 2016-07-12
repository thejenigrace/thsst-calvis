execute(cc, registers, memory) {
	System.out.println(cc.getValue() + " from beanshell");
}

execute(registers, memory) {
	System.out.println("Execute from beanshell");
}
