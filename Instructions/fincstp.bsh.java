execute(registers, memory) {
    registers.x87().status().setBinaryTop( (registers.x87().status().getTop() + 1) % 8 );
    registers.x87().status().set("C1", '0');  
    registers.x87().status().set("C0", '0');  
    registers.x87().status().set("C2", '0');  
    registers.x87().status().set("C3", '0');    
}
