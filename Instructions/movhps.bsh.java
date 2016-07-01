execute(des,src,registers,memory) {
    int QWORD = 64;
    String srcValue;
    Calculator calculator = new Calculator(registers,memory);

    //TODO Need to set value into lower 64-bit of XMM Register
    //Memory to XMM: Destination-High DWORD cleared to 0
    //XMM to Memory: Destination-High DWORD should NOT changed
    if(src.isMemory() && des.isRegister()) {
		srcValue = memory.read(src, QWORD);

		desValue = registers.get(des);
		desValue = desValue.substring(16, 32);

		srcValue = srcValue.concat(desValue);

		registers.set(des, srcValue);
    } else if(src.isRegister() && des.isMemory()) {
		desValue = memory.read(des, 128);
		srcValue = registers.get(src);
		memory.write(des, srcValue.substring(0, 16).concat(desValue.substring(16,32)), QWORD * 2);
    }
}
