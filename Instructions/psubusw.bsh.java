execute(des, src, registers, memory) {
    int desSize = 0;
    int srcSize = 0;
    int sizeOfHex = 4;
    Calculator c = new Calculator(registers, memory);
    if(des.isRegister()) {
        desSize = registers.getBitSize(des);
    }
    else{
        desSize = memory.getBitSize(des);
    }
    if(src.isRegister()) {
        srcSize = registers.getBitSize(src);
    }
    else{
        srcSize = memory.getBitSize(src);
        if( srcSize == 0) {
            srcSize = registers.getBitSize(des);
        }
    }
    String saturateStrAbove = "F";
    String saturateStrBelow = "0";
    String sourceReg = "";
    String desStr = "";
    String srcStr = "";
    for(int x = 1; x < sizeOfHex; x++) {
        saturateStrAbove += "F";
        saturateStrBelow += "0";
    }

    String sourceReg = "";
    String desStr = "";
    String srcStr = "";
    int sizeOfAdd = 0;
    ///end of defining sizes///
    if(des.isRegister()) {
        if(desSize == srcSize && (srcSize == 64  || srcSize == 128 ) && src.isRegister() ) {
            desStr = registers.get(des);
            srcStr = registers.get(src);
            sourceReg = executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex, saturateStrAbove, saturateStrBelow);
        }
        if((srcSize == 64  || srcSize == 128 ) && src.isMemory() ) {
            desStr = registers.get(des);

            srcStr = memory.read(src, desSize);
            sourceReg = executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex, saturateStrAbove, saturateStrBelow);
        }
        registers.set(des, sourceReg);
    }
    if(des.isMemory()) {
        if(desSize == srcSize && ( srcSize == 64  || srcSize == 128 ) && src.isRegister() ) {
            desStr = memory.read(des, desSize);
            srcStr = registers.get(src);
            sourceReg = executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex, saturateStrAbove, saturateStrBelow);
        }
    }
}

String executeAdd(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex, saturateStrAbove, saturateStrBelow){
    String resultingAdd = "";
    for(int x = 0; x < srcSize / 4; x = x + sizeOfHex) {
        StringBuilder strToBuildDes = new StringBuilder();
        StringBuilder strToBuildSrc = new StringBuilder();
        int intSrc = 0;
        int intDes = 0;

        for(int y = 0; y < sizeOfHex; y++) {
            strToBuildDes.append(desStr.charAt(x + y));
            strToBuildSrc.append(srcStr.charAt(x + y));
        }

        BigInteger destination = new BigInteger(strToBuildDes.toString(),16);
        BigInteger source = new BigInteger(strToBuildSrc.toString(),16);


        intDes = Integer.parseInt( c.hexZeroExtend(destination.toString(16), sizeOfHex), 16);

        intSrc = Integer.parseInt( c.hexZeroExtend(source.toString(16), sizeOfHex), 16);



        int result = intDes - intSrc;


        if(result < 0) {
            resultingAdd += saturateStrBelow;
        }
        else{
            resultingAdd += c.hexZeroExtend(new BigInteger(String.valueOf(result), 10).toString(16), sizeOfHex);
        }

    }
    return resultingAdd;
}
