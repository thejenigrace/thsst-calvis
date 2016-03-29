execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);

    int desSize = 0;
    int srcSize = 0;

    if( des.isRegister() ) {
		desSize = registers.getBitSize(des);
	}

	if( src.isRegister() ) {
		srcSize = registers.getBitSize(src);
	} else {
		srcSize = memory.getBitSize(src);
	}

    if( des.isRegister() ) {
        if( src.isRegister() ) {
            if( (desSize == srcSize) && checkSizeOfRegister(registers, desSize) ) {
                String source = registers.get(src);
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            } else {
                //throw exception
            }
        } else if( src.isMemory() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                String source = memory.read(src, desSize);
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            } else {
                //throw exception
            }
        }
    } else {
        //throw exception
    }
}

storeResultToRegister(registers, calculator, des, source, destination, desSize) {
    String result = "";

    Token token = new Token(Token.REG, "AX");

    BigInteger biDes = new BigInteger("0", 16);
    BigInteger wordpos = new BigInteger("FF", 16);
    BigInteger wordneg = new BigInteger("00", 16);

    if( desSize == 64 ) {
        for(int i = 0; i <= 12; i+=4) {
            if( i == 12 ) {
                biDes = new BigInteger(source.substring(i), 16);
            } else {
                biDes = new BigInteger(source.substring(i, i + 4), 16);
            }

            String temp = calculator.hexToBinaryString(biDes.toString(16), token);
            String sign = temp.charAt(0) + "";

            if( sign.equals("0") ) {
                if( biDes.compareTo(wordpos) == 1 ) {
                    result += "FF";
                } else {
                    if( i == 12 ) {
                        result += source.substring(i + 2);
                    } else {
                        result += source.substring(i + 2, i + 4);
                    }
                }
            } else if( sign.equals("1") ) {
                result += "00";
            }
        }

        for(int i = 0; i <= 12; i+=4) {
            if( i == 12 ) {
                biDes = new BigInteger(destination.substring(i), 16);
            } else {
                biDes = new BigInteger(destination.substring(i, i + 4), 16);
            }

            String temp = calculator.hexToBinaryString(biDes.toString(16), token);
            String sign = temp.charAt(0) + "";

            if( sign.equals("0") ) {
                if( biDes.compareTo(wordpos) == 1 ) {
                    result += "FF";
                } else {
                    if( i == 12 ) {
                        result += destination.substring(i + 2);
                    } else {
                        result += destination.substring(i + 2, i + 4);
                    }
                }
            } else if( sign.equals("1") ) {
                result += "00";
            }
        }
    } else if( desSize == 128 ) {
        for(int i = 0; i <= 28; i+=4) {
            if( i == 28 ) {
                biDes = new BigInteger(source.substring(i), 16);
            } else {
                biDes = new BigInteger(source.substring(i, i + 4), 16);
            }

            String temp = calculator.hexToBinaryString(biDes.toString(16), token);
            String sign = temp.charAt(0) + "";

            if( sign.equals("0") ) {
                if( biDes.compareTo(wordpos) == 1 ) {
                    result += "FF";
                } else {
                    if( i == 28 ) {
                        result += source.substring(i + 2);
                    } else {
                        result += source.substring(i + 2, i + 4);
                    }
                }
            } else if( sign.equals("1") ) {
                result += "00";
            }
        }

        for(int i = 0; i <= 28; i+=4) {
            if( i == 28 ) {
                biDes = new BigInteger(destination.substring(i), 16);
            } else {
                biDes = new BigInteger(destination.substring(i, i + 4), 16);
            }

            String temp = calculator.hexToBinaryString(biDes.toString(16), token);
            String sign = temp.charAt(0) + "";

            if( sign.equals("0") ) {
                if( biDes.compareTo(wordpos) == 1 ) {
                    result += "FF";
                } else {
                    if( i == 28 ) {
                        result += destination.substring(i + 2);
                    } else {
                        result += destination.substring(i + 2, i + 4);
                    }
                }
            } else if( sign.equals("1") ) {
                result += "00";
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
