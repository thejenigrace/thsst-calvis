 execute(des, registers, memory) {
 	if ( des.isRegister() ){
 		// 32 (8 hex) bit registers only
		 System.out.println(registers.getBitSize(des));
 		if ( registers.getBitSize(des) == 32) {
	 		String x = registers.get(des);
	 		char[] temp = x.toCharArray();
			char[] toBeReplaced = x.toCharArray();
		    int y = 7;
			for(int x = 0; x < 8; x++){
		        toBeReplaced[x] = temp[y];
		        y--;
		    }
 		}
 	}
 }
//31 ---- 0
// eax: 12345678
//temp: 12345678
//
//0 ----- 31
//
//		01234567
//from:	12345678
//
//		01234567
//to:		78563412

