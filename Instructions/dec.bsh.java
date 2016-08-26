execute(des, registers, memory) {
    Calculator c = new Calculator(registers, memory);
    EFlags ef = registers.getEFlags();

    String numberOfF = "";
    int bitSize = 0;
    if( des.isRegister() ) {
        bitSize = registers.getBitSize(des);
    }
    else if( des.isMemory() ) {
        bitSize = memory.getBitSize(des);
    }

    for( int x = 0; x < bitSize; x++) {
        numberOfF += "1";
    }

    if ( des.isRegister() ) {
        String x = registers.get(des);
        BigInteger biX = new BigInteger(x, 16);
        BigInteger result = biX.subtract(new BigInteger("1"));

        if( result.toString(16).equals("-1") ) {
            result = new BigInteger(numberOfF, 2);
            registers.set(des, c.binaryToHexString(result.toString(2), des));
        }
        else {
            registers.set(des, c.binaryToHexString(result.toString(2),des));
        }

        //ef.setCarryFlag("0");

        ef.setParityFlag(c.checkParity(result.toString(2)));

        //to do listef.setAuxiliaryFlag("0");

        String sign=""+result.toString(2).charAt(0);
        ef.setSignFlag(sign);

        if(result.equals(BigInteger.ZERO))
            ef.setZeroFlag("1");
        else
            ef.setZeroFlag("0");

        ef.setOverflowFlag(c.checkOverflowAdd('0', c.binaryZeroExtend(biX.toString(2), des).charAt(0), c.binaryZeroExtend(result.toString(2), des).charAt(0)));
    }
    else if ( des.isMemory() ) {
        int desSize = memory.getBitSize(des);
        String x = memory.read(des, desSize);
        BigInteger biX = new BigInteger(x,16);
        BigInteger result = biX.subtract(new BigInteger("1"));

        if( result.compareTo(BigInteger.ZERO) < 0 ) {
            result = new BigInteger(numberOfF, 2);
            memory.write(des, c.binaryToHexString(result.toString(2),des), desSize);
        }
        else {
            memory.write(des,c.binaryToHexString(result.toString(2),des), desSize);
        }


        ef.setParityFlag(c.checkParity(result.toString(2)));

        ef.setAuxiliaryFlag("0");

        String sign=""+result.toString(2).charAt(0);
        ef.setSignFlag(sign);

        if(result.equals(BigInteger.ZERO))
            ef.setZeroFlag("1");
        else
            ef.setZeroFlag("0");

        ef.setOverflowFlag(c.checkOverflowAdd('0', c.binaryZeroExtend(biX.toString(2), des).charAt(0), c.binaryZeroExtend(result.toString(2), des).charAt(0)));
    }
}
