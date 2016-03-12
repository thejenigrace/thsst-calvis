execute(des,src,ctr,registers,memory) {
    int desBitSize;
    int desHexSize;
    int srcHexSize;
    int srcBitSize;
    String srcValue;
    String desValue;
    String effectiveAddress = des.getValue();
    effectiveAddress = memory.removeSizeDirectives(effectiveAddress);
    Calculator c = new Calculator(registers, memory);
    if( des.isRegister() && registers.getBitSize(des) == 16) {
        desBitSize = registers.getBitSize(des);
        desHexSize = registers.getHexSize(des);
        desValue = registers.get(des);
    }
    else if( des.isMemory() && (memory.getBitSize(des) == 0 || memory.getBitSize(des) == 16) ) {
        desBitSize = 16;
        desHexSize = 4;
        desValue = memory.read(des, desBitSize);

    }
    if( src.isRegister() ){
        srcValue = registers.get(src);
        srcBitSize = registers.getBitSize(src);
        srcHexSize = registers.getHexSize(src);
    }
    if(desBitSize == 16){
        int count = 0 ;

        if(src.isRegister()){
            if(srcBitSize == 64 && ctr.isHex()){
                count = new BigInteger(ctr.getValue(), 16).intValue() % 4;
            }
            else if(srcBitSize == 128 && ctr.isHex()){
                count = new BigInteger(ctr.getValue(), 16).intValue() % 8;
            }
        }
        if(c.checkIfInGPRegisterLow(effectiveAddress)){
            if( des.isRegister()){
                registers.set(des, srcValue.substring(count * 4, count * 4 + 4));
            }
        }
        else if(des.isMemory() && desBitSize == 16){
            memory.write(des, srcValue.substring(count * 4, count * 4 + 4), desBitSize);
        }
    }
}
