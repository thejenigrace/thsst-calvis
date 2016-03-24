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
	
    if( (src.isRegister() && registers.getBitSize(src) == 32 )){
        srcValue = registers.get(src);
        srcBitSize = registers.getBitSize(src);
        srcHexSize = registers.getHexSize(src);
    }
	else if(src.isMemory() && memory.getBitSize(src) == 16 ){
		srcBitSize = memory.getBitSize(src);
        srcHexSize = memory.getHexSize(src);
		srcValue = memory.read(src, srcBitSize);
	}
	
    if(registers.getBitSize(des) == 64 || registers.getBitSize(des) == 128 ){
        int count = 0 ;

        if(src.isRegister()){
            if(desBitSize == 64 && ctr.isHex()){
                count = new BigInteger(ctr.getValue(), 16).intValue() % 4;
            }
            else if(desBitSize == 128 && ctr.isHex()){
                count = new BigInteger(ctr.getValue(), 16).intValue() % 8;
            }
        }
        System.out.println(count);
        StringBuilder myResultString = new StringBuilder(desValue);
        System.out.println(myResultString.toString() + " nganga");
        for(int y = count * 4; y < count * 4 + 4; y++){
            myResultString.setCharAt(y, srcValue.charAt(y % 4));
        }

		System.out.println(myResultString.toString() + " icant wait");
        if(c.checkIfInGPRegisterLow(effectiveAddress)){
            if( des.isRegister()){
                registers.set(des, myResultString.toString());
            }
        }
        else if(des.isMemory() && srcBitSize == 16){
            memory.write(des, myResultString.toString(), desBitSize);
        }
    }
}
