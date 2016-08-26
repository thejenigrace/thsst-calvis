execute(des, src, ctr, registers, memory) {
    Calculator c = new Calculator(registers, memory);
    String destination;
    String source;
    int desSize = 0;
    int srcSize = 0;
    int countImmediate = 8;
    boolean isHex = false;
    String countStr;
    String[] countArr = new String[4];

    if(ctr.isHex()) {
        countStr = new BigInteger(ctr.getValue(), 16).toString(2);
        String toBeZeroes = "";

        for(int x =0; x < 8 - countStr.length(); x++) {
            toBeZeroes += "0";
        }
        countStr = toBeZeroes + countStr;

        isHex = true;
    }

    if(des.isRegister()) {
        destination = registers.get(des);
        desSize = registers.getBitSize(des);
    }
    if(src.isRegister()) {
        if(registers.getBitSize(src) == 128) {
            srcSize = registers.getBitSize(src);
            source = registers.get(src);
        }
    }

    else if(src.isMemory()) {
        srcSize = memory.getBitSize(src);
        source = memory.read(src, 128);
    }

    int a = 0;
    if(des.isRegister() && desSize == 128) {
        int countDistance = 16;
        String resultingStr = "";
        if(isHex) {
            for(int x = 0; x < 4; x++) {
                countArr[x] = countStr.substring(x * 2, x * 2 + 2);

            }

            for(int x = 0; x < 4; x++) {
                int sourceIndex = Integer.parseInt(countArr[x], 2);
                resultingStr += source.substring((srcSize / 4) - 4 - (sourceIndex * 4), (srcSize / 4) - (sourceIndex * 4));

            }

            registers.set(des, source.substring(0, 16) + resultingStr);
        }
    }
}
