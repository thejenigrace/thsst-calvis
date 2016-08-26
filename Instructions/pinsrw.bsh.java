execute(des,src,ctr,registers,memory) {
    int desBitSize;
    int desHexSize;
    int srcHexSize;
    int srcBitSize;
    String srcValue;
    String desValue;
    String effectiveAddress = src.getValue();
    effectiveAddress = memory.removeSizeDirectives(effectiveAddress);
    Calculator c = new Calculator(registers, memory);
    if( des.isRegister() && (registers.getBitSize(des) == 64 || registers.getBitSize(des) == 128 )) {
        desBitSize = registers.getBitSize(des);
        desHexSize = registers.getHexSize(des);
        desValue = registers.get(des);
    }

    if( (src.isRegister() && registers.getBitSize(src) == 32 )) {
        srcValue = registers.get(src).substring(4, 8);
        srcBitSize = registers.getBitSize(src);
        srcHexSize = registers.getHexSize(src);
    }
    else if(src.isMemory() && memory.getBitSize(src) == 16 ) {
        srcBitSize = memory.getBitSize(src);
        srcHexSize = memory.getHexSize(src);
        srcValue = memory.read(src, srcBitSize);
    }

    else if(src.isMemory() && memory.getBitSize(src) == 0 ) {
        srcBitSize = desBitSize;
        srcHexSize =desHexSize;
        srcValue = memory.read(src, srcBitSize);
    }
    if(registers.getBitSize(des) == 64 || registers.getBitSize(des) == 128 ) {
        int count = 0;
        if(desBitSize == 64 && ctr.isHex()) {
            count = new BigInteger(ctr.getValue(), 16).intValue() % 4;
        }
        else if(desBitSize == 128 && ctr.isHex()) {
            count = new BigInteger(ctr.getValue(), 16).intValue() % 8;
        }

        StringBuilder myResultString = new StringBuilder(desValue);

        for(int y = 0; y < 4; y++) {
            myResultString.setCharAt(count * 4 + y, srcValue.charAt(y));
        }
        registers.set(des, myResultString.toString());

    }
}
