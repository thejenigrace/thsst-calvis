execute(des, src, registers, memory) {
    if ( src.isMemory() && des.isRegister() ) {
        String effectiveAddress = src.getValue();
        effectiveAddress = memory.removeSizeDirectives(effectiveAddress);

        int desRegSize = registers.getBitSize(des);

        if ( (desRegSize == 32 || desRegSize == 16)
             && ((desRegSize / 4) == effectiveAddress.length()) ) {
            registers.set(des, effectiveAddress);
        }
        else if ( desRegSize == 16 && effectiveAddress.length() == 8 ) {
            effectiveAddress = effectiveAddress.substring(4,8); // get lower half
            registers.set(des, effectiveAddress);
        }
        else if ( desRegSize == 32 && effectiveAddress.length() == 4 ) {
            Calculator cal = new Calculator(registers, memory);
            effectiveAddress = cal.hexZeroExtend(effectiveAddress, des);
            registers.set(des, effectiveAddress);
        }
    }
}
