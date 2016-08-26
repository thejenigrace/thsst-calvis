execute(des, src, registers, memory) {
    Calculator c = new Calculator(registers, memory);
    String destination;
    String source;
    int desSize = 0;
    String countStr;
    int totalHexSize = 0;
    boolean isSizeAllowable = false;
    int[] addMode = new int[2];

    for(int x = 0; x < 2; x++) {
        addMode[x] = 0;
    }

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

        if( (memory.getBitSize(src) == 64 || memory.getBitSize(src) == 128 || memory.getBitSize(src) == 0)) {

            srcSize = memory.getBitSize(src);
            if(srcSize == 0) {
                srcSize = desSize;
            }

            source = memory.read(src, desSize);
            totalHexSize = (srcSize / 8);
            isSizeAllowable = true;
        }
    }

    int a = 0;
    if(des.isRegister() && isSizeAllowable && srcSize == desSize) {
        String sourceHex = source;
        String destinationHex = destination;
        String resultAddAll = "";


        for(int x = 0; x < totalHexSize * 2; x = x + 2) {

            String partialDes = destinationHex.substring(x, x + 2);
            String partialSrc = sourceHex.substring(x, x + 2);


            int result = Math.abs(Integer.parseInt(partialDes, 16) - Integer.parseInt(partialSrc, 16));

            switch(srcSize) {
            case 64:
                addMode[0] += result;
                break;
            case 128:
                if(x < 16)
                    addMode[0] += result;
                else
                    addMode[1] += result;
                break;
            }
        }


        switch(srcSize) {
        case 64:
            resultAddAll = "000000000000" + c.hexZeroExtend(new BigInteger(Integer.toString(addMode[0]), 10) + resultAddAll, 4);
            break;
        case 128:
            resultAddAll = "000000000000" + c.hexZeroExtend(new BigInteger(Integer.toString(addMode[1]), 10) + resultAddAll, 4) + "000000000000" + c.hexZeroExtend(new BigInteger(Integer.toString(addMode[0]), 10) + resultAddAll, 4);
            break;
        }

        registers.set(des, resultAddAll);
    }
}
