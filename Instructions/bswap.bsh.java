 execute(des, registers, memory) {
 	if ( des.isRegister() ){
 		// 32 (8 hex) bit registers only
		 System.out.println(registers.getBitSize(des));
 		if ( registers.getBitSize(des) == 32) {
	 		String x = registers.get(des);
	 		char[] temp = x.toCharArray();
			char[] toBeReplaced = x.toCharArray();
		    int y = 7;
		 	int z = 0;
		 	char tempa;
		 	char tempb;
//			for(int x = 0; x < 4; x++){
		 		tempa = toBeReplaced[0];
		 		tempb = toBeReplaced[1];
		        toBeReplaced[0] = temp[6];
				toBeReplaced[1] = temp[7];
		 		toBeReplaced[6] = tempa;
				toBeReplaced[7] = tempb;
		 		tempa = toBeReplaced[2];
				tempb = toBeReplaced[3];
				toBeReplaced[2] = temp[4];
				toBeReplaced[3] = temp[5];
				toBeReplaced[4] = tempa;
				toBeReplaced[5] = tempb;

//				System.out.println(tempa + " " + tempb + " " + toBeReplaced[x] + " " + toBeReplaced[x + 1] + " " + temp[y] + " " + temp[y-1]);
		 y--;
		 z++;

//		    }
		 registers.set(des, new String(toBeReplaced));
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

