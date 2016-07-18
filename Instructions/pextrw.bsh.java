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
    if( des.isRegister() && registers.getBitSize(des) == 32) {
        desBitSize = registers.getBitSize(des);
        desHexSize = registers.getHexSize(des);
        desValue = registers.get(des);
    }
    else if( des.isMemory() && (memory.getBitSize(des) == 0 || memory.getBitSize(des) == 16) ) {
        desBitSize = 32;
        desHexSize = 4;
        desValue = memory.read(des, desBitSize);

    }
    if( src.isRegister() ){
        srcValue = registers.get(src);
        srcBitSize = registers.getBitSize(src);
        srcHexSize = registers.getHexSize(src);
    }
    if(desBitSize == 32){
        int count = 0 ;

        if(src.isRegister()){
            if(srcBitSize == 64 && ctr.isHex()){
                count = new BigInteger(ctr.getValue(), 16).intValue() % 4;
            }
            else if(srcBitSize == 128 && ctr.isHex()){
                count = new BigInteger(ctr.getValue(), 16).intValue() % 8;
            }
        }
		System.out.println("dafuq");
        if(des.isRegister()){
		System.out.println("go gog ogo" + "0000" + srcValue.substring(count * 4, count * 4 + 4));
            if( des.isRegister()){
                registers.set(des, "0000" + srcValue.substring(count * 4, count * 4 + 4));
            }
        }
        else if(des.isMemory()){
			System.out.println(srcValue.substring(count * 4, count * 4 + 4) + " woah");
            memory.write(des, srcValue.substring(count * 4, count * 4 + 4), desBitSize / 2);
        }
    }
}
