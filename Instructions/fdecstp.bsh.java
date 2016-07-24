execute(registers, memory) {
  int iTop = registers.x87().status().getTop();
  if ( iTop == 0 ) {
    iTop = 7;
  } else {
    iTop--;
  }
  registers.x87().status().setBinaryTop(iTop);
  registers.x87().rotateBarrel(1); // rotate right
  registers.x87().refreshST();
  
  registers.x87().status().set("C1", '0');
  registers.x87().status().set("C0", '0');
  registers.x87().status().set("C2", '0');
  registers.x87().status().set("C3", '0');
}
