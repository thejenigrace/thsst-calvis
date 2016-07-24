execute(registers, memory) {
    int top  = (registers.x87().status().getTop() - 1);
	if(top < 0){
		top = 7;
	}
	registers.x87().status().setBinaryTop(top);
    registers.x87().status().set("C1", '0');  
    registers.x87().status().set("C0", '0');  
    registers.x87().status().set("C2", '0');  
    registers.x87().status().set("C3", '0');      
}
