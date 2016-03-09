execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);

    int desSize = 0;
    int srcSize = 0;

    if( des.isRegister() ) {
		desSize = registers.getBitSize(des);
	}
	if( src.isRegister() ) {
		srcSize = registers.getBitSize(src);
	}
	else {
		srcSize = memory.getBitSize(src);
	}

    if( des.isRegister() ) {
        if( src.isRegister() ) {
            if( (desSize == srcSize) && checkSizeOfRegister(registers, desSize) ) {
                String source = registers.get(src);
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            }
            else {
                //throw exception
            }
        }
        else if( src.isMemory() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                String source = memory.read(src, desSize);
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            }
            else {
                //throw exception
            }
        }
    }
    else {
        //throw exception
    }
}

storeResultToRegister(registers, calculator, des, source, destination, desSize) {
    String result = "";

    BigInteger biDes = new BigInteger("0", 16);
    BigInteger dwordpos = new BigInteger("7FFF", 16);
    BigInteger dwordneg = new BigInteger("8000", 16);

    if( desSize == 64 ) {
        for(int i = 0; i <= 24; i+=8) {
            if( i == 24 ) {
                biDes = new BigInteger(destination.substring(i), 16);
            }
            else {
                biDes = new BigInteger(destination.substring(i + 4, i + 8), 16);
            }

            if( biDes.compareTo(dwordpos) == 1 ) {
                result += "7FFF";
            }
            else {
                if( i == 12 ) {
                    result += destination.substring(i + 4);
                }
                else {
                    result += destination.substring(i + 4, i + 8);
                }
            }
        }

        for(int i = 0; i <= 24; i+=8) {
            if( i == 24 ) {
                biDes = new BigInteger(source.substring(i), 16);
            }
            else {
                biDes = new BigInteger(source.substring(i + 4, i + 8), 16);
            }

            if( biDes.compareTo(dwordpos) == 1 ) {
                result += "7FFF";
            }
            else {
                if( i == 12 ) {
                    result += source.substring(i + 4);
                }
                else {
                    result += source.substring(i + 4, i + 8);
                }
            }
        }
    }
    else if( desSize == 128 ) {
        for(int i = 0; i <= 24; i+=8) {
            if( i == 24 ) {
                biDes = new BigInteger(destination.substring(i), 16);
            }
            else {
                biDes = new BigInteger(destination.substring(i + 4, i + 8), 16);
            }

            if( biDes.compareTo(dwordpos) == 1 ) {
                result += "7FFF";
            }
            else {
                if( i == 24 ) {
                    result += destination.substring(i + 4);
                }
                else {
                    result += destination.substring(i + 4, i + 8);
                }
            }
        }

        for(int i = 0; i <= 24; i+=8) {
            if( i == 24 ) {
                biDes = new BigInteger(source.substring(i), 16);
            }
            else {
                biDes = new BigInteger(source.substring(i + 4, i + 8), 16);
            }

            if( biDes.compareTo(dwordpos) == 1 ) {
                result += "7FFF";
            }
            else {
                if( i == 12 ) {
                    result += source.substring(i + 4);
                }
                else {
                    result += source.substring(i + 4, i + 8);
                }
            }
        }
    }

    registers.set(des, result);
}

boolean checkSizeOfRegister(registers, desSize) {
    boolean checkSize = false;

    if( 128 == desSize || 64 == desSize ) {
        checkSize = true;
    }

    return checkSize;
}
