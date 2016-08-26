execute(des, registers, memory) {
    Calculator cal = new Calculator(registers, memory);
    if ( des.isRegister() ) {

        if ( registers.getBitSize(des) == 32) {
            String x = registers.get(des);
            char[] temp = x.toCharArray();
            char[] toBeReplaced = x.toCharArray();
            int y = 7;
            for(int x = 0; x < 8; x++) {
                toBeReplaced[x] = temp[y];
                y--;
            }

            int y = 0;
            for(int x = 0; x < 4; x++) {
                char temp = toBeReplaced[y];
                toBeReplaced[y] = toBeReplaced[y + 1];
                toBeReplaced[y + 1] = temp;
                y += 2;
            }   /// 32 (8 hex) bit registers only

            registers.set(des, cal.hexZeroExtend(new String(toBeReplaced), registers.getHexSize(des)));
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
