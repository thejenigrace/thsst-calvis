 execute(des, registers, memory) {
 	if ( des.isRegister() ){
 		// 32 (8 hex) bit registers only
 		if ( registers.getSize(des) == 8) {
	 		String x = registers.get(des);
	 		char[] temp = x.toCharArray();
	 		temp[0] = x.charAt(6); 
	 		temp[1] = x.charAt(7); 
	 		temp[2] = x.charAt(4);
	 		temp[3] = x.charAt(5);
	 		temp[4] = x.charAt(2);
	 		temp[5] = x.charAt(3);
	 		temp[6] = x.charAt(0);
	 		temp[7] = x.charAt(1);
	 		int index = 1;
	 		for( int k = 0; k < temp.length; k++){
	 			if ( k % 2 == 0)
	 				temp[k] = x.charAt(temp.length - (k * 2));
	 		}
	 		0 6 
	 		1 7
	 		2 4
	 		3 5
	 		4 2
	 		5 3
	 		6 0 
	 		7 1
	 		
 		}
 	}
 }
31 ---- 0
 eax: 12345678
temp: 12345678

0 ----- 31

		01234567
from:	12345678

		01234567
to:		78563412
