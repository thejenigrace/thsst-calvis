execute(des, src, registers, memory) {
    Calculator c = new Calculator(registers, memory);
    boolean isRegisterSrc = false;
    int desSize = 0;
    int srcSize = 0;
    String resultingHex = "";
    if(des.isRegister()) {
        desSize = registers.getBitSize(des);
    }

    if(src.isRegister()) {
        srcSize = registers.getBitSize(src);
        isRegisterSrc = true;
    }

    else if(src.isMemory()) {
        srcSize = desSize;
    }

    if(des.isRegister() && (registers.getBitSize(des) == 128  && desSize == srcSize)) {
        String destination = registers.get(des);
        String resultingHexAdd = "";
        String source = "";

        if ( isRegisterSrc ) {
            source = registers.get(src);
        } else {
            source = memory.read(src, desSize);
        }

        String resultingHex = "";
        for(int x = 0; x < 2; x++) {
            String strSource = source.substring(x * 16, x * 16 + 16);
            double longSource = c.hexToDoublePrecisionFloatingPoint(strSource);
            double result = Math.sqrt(longSource);
            resultingHex += c.convertDoublePrecisionToHexString(result);

        }
        registers.set(des, resultingHex);
    }
}
