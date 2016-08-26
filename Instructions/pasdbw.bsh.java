execute(des, src, registers, memory) {
    Calculator c = new Calculator(registers, memory);
    String destination;
    String source;
    int desSize = 0;
    String countStr;
    int totalHexSize = 0;
    boolean isSizeAllowable = false;

    if(des.isRegister()) {
        destination = registers.get(des);
        desSize = registers.getBitSize(des);
    }

    if(src.isRegister()) {
        if( (registers.getBitSize(src) == 64 || registers.getBitSize(src) == 128) && desSize == registers.getBitSize(src) ) {
            srcSize = registers.getBitSize(src);
            source = registers.get(src);
            totalHexSize = (srcSize / 8);
            isSizeAllowable = true;
        }
    }

    else if(src.isMemory()) {
        if( (memory.getBitSize(src) == 64 || memory.getBitSize(src) == 128) && desSize == memory.getBitSize(src) ) {
            srcSize = memory.getBitSize(src);
            source = memory.read(src, srcSize);
            isSizeAllowable = true;
        }
    }

    int a = 0;
    if(des.isRegister() && isSizeAllowable) {
        String sourceBits = new BigInteger(source, 16).toString(2);
        String destinationBits = new BigInteger(destination, 16).toString(2);
        String operandResArr = "";
        String resultStr = "";
        for(int x = 0; x < totalHexSize; x++) {
            String resultingAbsSub = new BigInteger(sourceBits.substring(x * 8, x * 8 + 8), 2)
                                     .subtract(new BigInteger(destinationBits.substring(x * 8, x * 8 + 8), 2)).toString(10);
            if(resultingAbsSub.charAt(0) == '-') {
                resultingAbsSub = new BigInteger(resultingAbsSub, 10).multiply("-1", 10).toString(2);
            }
            operandResArr += resultingAbsSub;
        }
        String addAllStr = "";
        for(int x = 0; x < totalHexSize / 2; x++) {
            addAllStr += new BigInteger(operandResArr.substring(x * 8, x * 8 + 8), 2)
                         .add(new BigInteger(operandResArr.substring( (x + 1) * 8, (x + 1) * 8 + 8), 2)).toString(16);
        }

        switch(srcSize) {
        case 64:
            registers.set(des, "000000000000" + addAllStr);
            break;
        case 128:
            registers.set(des, "000000000000" + addAllStr.substring(4, 8) + "000000000000" + addAllStr(0, 4));
            break;
        }
    }
}
