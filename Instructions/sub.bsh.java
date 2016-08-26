execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    if ( des.isRegister() ) {
        if ( src.isRegister() ) {
            //get size of des, src
            int desSize = registers.getBitSize(des);
            int srcSize = registers.getBitSize(src);

            if( (desSize == srcSize) && checkSizeOfRegister(registers, desSize) ) {
                //get hex value of des, src then convert to binary
                String source = calculator.hexToBinaryString(registers.get(src), src);
                String destination = calculator.hexToBinaryString(registers.get(des), des);

                performSubRegister(source, destination, registers, calculator, flags, des, desSize);
            }
        }
        else if ( src.isMemory() ) {
            //get size of des, src
            int desSize = registers.getBitSize(des);

            //get hex value of des, src then convert to binary
            String source = calculator.hexToBinaryString(memory.read(src, desSize), des);
            String destination = calculator.hexToBinaryString(registers.get(des), des);

            performSubRegister(source, destination, registers, calculator, flags, des, desSize);
        }
        else if ( src.isHex() ) {
            //get size of des, src
            int desSize = registers.getBitSize(des);
            int srcSize = src.getValue().length();

            if( (desSize >= srcSize) && checkSizeOfRegister(registers, desSize)) {
                //get hex value of des, src then convert to binary
                String source = calculator.hexToBinaryString(src.getValue(), des);
                String destination = calculator.hexToBinaryString(registers.get(des), des);

                performSubRegister(source, destination, registers, calculator, flags, des, desSize);
            }
        }
    }
    else if ( des.isMemory() ) {
        if ( src.isRegister() ) {
            //get size of des, src
            int srcSize = registers.getBitSize(src);

            //get hex value of des, src then convert to binary
            String source = calculator.hexToBinaryString(registers.get(src), src);
            String destination = calculator.hexToBinaryString(memory.read(des, srcSize), src);

            String result = "";
            int r = 0;
            int borrow = 0;
            int carry = 0;
            int overflow = 0;

            for(int i = srcSize - 1; i >= 0; i--) {
                r = Integer.parseInt(String.valueOf(destination.charAt(i)))
                    - Integer.parseInt(String.valueOf(source.charAt(i)))
                    - borrow;

                if( r < 0 ) {
                    borrow = 1;
                    r += 2;
                    result = result.concat(r.toString());

                    if( i == 0 ) {
                        carry = 1;
                    }
                    if( i == 0 || i == 1 ) {
                        overflow++;
                    }
                }
                else {
                    borrow = 0;
                    result = result.concat(r.toString());
                }
            }

            String d = new StringBuffer(result).reverse().toString();
            memory.write(des, calculator.binaryToHexString(d, src), srcSize);

            //Flags
            String res = calculator.hexToBinaryString(memory.read(des, srcSize), src);
            BigInteger biR = new BigInteger(res, 2);
            setFlags(flags, calculator, source, destination, carry, overflow, res, biR);
        }
        else if ( src.isHex() ) {
            //get size of des, src
            int desSize = memory.getBitSize(des);

            //get hex value of des, src then convert to binary
            String source = calculator.hexToBinaryString(src.getValue(), des);
            String destination = calculator.hexToBinaryString(memory.read(des, des), des);

            String result = "";
            int r = 0;
            int borrow = 0;
            int carry = 0;
            int overflow = 0;

            for(int i = desSize - 1; i >= 0; i--) {
                r = Integer.parseInt(String.valueOf(destination.charAt(i)))
                    - Integer.parseInt(String.valueOf(source.charAt(i)))
                    - borrow;

                if( r < 0 ) {
                    borrow = 1;
                    r += 2;
                    result = result.concat(r.toString());

                    if( i == 0 ) {
                        carry = 1;
                    }
                    if( i == 0 || i == 1 ) {
                        overflow++;
                    }
                }
                else {
                    borrow = 0;
                    result = result.concat(r.toString());
                }
            }

            String d = new StringBuffer(result).reverse().toString();
            memory.write(des, calculator.binaryToHexString(d, des), des);

            //Flags
            String res = calculator.hexToBinaryString(memory.read(des, des), des);
            BigInteger biR = new BigInteger(res, 2);
            setFlags(flags, calculator, source, destination, carry, overflow, res, biR);
        }
    }
}

performSubRegister(source, destination, registers, calculator, flags, des, desSize) {
    String result = "";
    int r = 0;
    int borrow = 0;
    int carry = 0;
    int overflow = 0;

    for(int i = desSize - 1; i >= 0; i--) {
        r = Integer.parseInt(String.valueOf(destination.charAt(i)))
            - Integer.parseInt(String.valueOf(source.charAt(i)))
            - borrow;

        if( r < 0 ) {
            borrow = 1;
            r += 2;
            result = result.concat(r.toString());

            if( i == 0 ) {
                carry = 1;
            }
            if( i == 0 || i == 1 ) {
                overflow++;
            }
        }
        else {
            borrow = 0;
            result = result.concat(r.toString());
        }
    }

    String d = new StringBuffer(result).reverse().toString();
    registers.set(des, calculator.binaryToHexString(d, des));

    //Flags
    String res = calculator.hexToBinaryString(registers.get(des), des);
    BigInteger biR = new BigInteger(res, 2);
    setFlags(flags, calculator, source, destination, carry, overflow, res, biR);
}

setFlags(flags, calculator, source, destination, carry, overflow, res, biR) {
    flags.setCarryFlag(carry.toString());

    if( overflow == 1 ) {
        flags.setOverflowFlag("1");
    }
    else {
        flags.setOverflowFlag("0");
    }

    if( biR.equals(BigInteger.ZERO) ) {
        flags.setZeroFlag("1");
    }
    else {
        flags.setZeroFlag("0");
    }

    String sign = "" + res.charAt(0);
    flags.setSignFlag(sign);

    String parity = calculator.checkParity(res);
    flags.setParityFlag(parity);

    String auxiliary = calculator.checkAuxiliarySub(source, destination);
    flags.setAuxiliaryFlag(auxiliary);
}

boolean checkSizeOfRegister(registers, desSize) {
    boolean checkSize = false;
    for(int a : registers.getAvailableSizes()) {
        if( a == desSize ) {
            checkSize = true;
        }
    }

    return checkSize;
}
