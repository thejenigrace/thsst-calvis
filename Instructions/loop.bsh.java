execute(rel, registers, memory) {
	String count = "";
	switch(Memory.MAX_ADDRESS_SIZE){
		case 32:
			count = registers.get("ECX");
			break;
	}

}
