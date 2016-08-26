execute(src,registers,memory) {
    int srcBitSize;
    String multiplier;
    Calculator calculator = new Calculator(registers, memory);
    if( src.isRegister() ) {
        srcBitSize = registers.getBitSize(src);
        multiplier = registers.get(src);
    }else if ( src.isMemory() ) {
        srcBitSize = memory.getBitSize(src);
        multiplier = memory.read(src,srcBitSize);
    }

    if ( srcBitSize==8 ) {
        String multiplicand = registers.get("AL");
        multiplyForOneOperand(srcBitSize,registers,calculator,multiplicand,multiplier,null,"AX");
    }else if ( srcBitSize==16 ) {
        String multiplicand = registers.get("AX");
        multiplyForOneOperand(srcBitSize,registers,calculator,multiplicand,multiplier,"DX","AX");
    }else if( srcBitSize==32 ) {
        String multiplicand = registers.get("EAX");
        multiplyForOneOperand(srcBitSize,registers,calculator,multiplicand,multiplier,"EDX","EAX");
    }
}

multiplyForOneOperand(srcBitSize,registers,calculator,multiplicand,multiplier,registerUpperHalf,registerLowerHalf) {
    BigInteger biMultiplicand = new BigInteger(multiplicand, 16);
    BigInteger biMultiplier = new BigInteger(multiplier, 16);

    int registerLowerHalfBitSize = registers.getBitSize(registerLowerHalf);
    long longMultiplicand = calculator.convertToSignedInteger(biMultiplicand, srcBitSize);
    long longMultiplier = calculator.convertToSignedInteger(biMultiplier,  srcBitSize);
    long longResult = longMultiplicand * longMultiplier;

    int registerLowerHalfHexSize = registers.getHexSize(registerLowerHalf);
    String result = Long.toHexString(longResult);
    result = calculator.hexZeroExtend(result,registerLowerHalfHexSize*2);
    String finalResult = result.substring(result.length()-registerLowerHalfHexSize);
    registers.set(registerLowerHalf, finalResult);

    EFlags ef = registers.getEFlags();
    BigInteger checkUpperHalf;
    if ( registerUpperHalf != null ) {
        int start = result.length() - registerLowerHalfHexSize * 2;
        int end = result.length() - registerLowerHalfHexSize;
        String upperResult = result.substring(start, end);
        registers.set(registerUpperHalf,upperResult);
        checkUpperHalf = new BigInteger(upperResult, 16);
    } else {
        checkUpperHalf = new BigInteger(registers.get("AH"), 16);
    }

    String checker = "";
    if( registerUpperHalf != null ) {
        checker = finalResult.substring(0,1);
    } else {
        checker = registers.get("AL");
        checker = checker.substring(0,1);
    }

    if( checkUpperHalf.equals(new BigInteger(checker, 16)) ) {
        ef.setCarryFlag("0");
        ef.setOverflowFlag("0");
    } else{
        ef.setCarryFlag("1");
        ef.setOverflowFlag("1");
    }
}

execute(des,src,registers,memory) {
    int BYTE = 8;
    int WORD = 16;
    int DWORD = 32;
    int desBitSize = registers.getBitSize(des);
    Calculator calculator = new Calculator(registers,memory);

    if ( desBitSize == WORD ) {

        if ( src.isRegister() && registers.getBitSize(src) == WORD ) {

            String multiplier = registers.get(src);
            multiplyForTwoOperand(des,registers,calculator,multiplier);
        } else if ( src.isMemory() ) {

            String multiplier = memory.read(src,desBitSize);
            multiplyForTwoOperand(des,registers,calculator,multiplier);
        } else if ( src.isHex() ) {

            String multiplier = src.getValue();

            if ( multiplier.length() <= WORD/4 )
                multiplyForTwoOperand(des,registers,calculator,multiplier);
        }
    } else if ( desBitSize == DWORD ) {

        if ( src.isRegister() && registers.getBitSize(src) == DWORD ) {

            String multiplier = registers.get(src);
            multiplyForTwoOperand(des,registers,calculator,multiplier);
        } else if ( src.isMemory() ) {

            String multiplier = memory.read(src,desBitSize);
            multiplyForTwoOperand(des,registers,calculator,multiplier);
        } else if ( src.isHex() ) {

            String multiplier = src.getValue();

            if ( multiplier.length() <= DWORD/4 )
                multiplyForTwoOperand(des,registers,calculator,multiplier);
        }
    }
}

multiplyForTwoOperand(des,registers,calculator,multiplier) {
    String multiplicand = registers.get(des);

    BigInteger biMultiplicand=new BigInteger(multiplicand,16);
    BigInteger biMultiplier=new BigInteger(multiplier,16);

    int desBitSize = registers.getBitSize(des);
    int desHexSize = registers.getHexSize(des);
    long longMultiplicand=calculator.convertToSignedInteger(biMultiplicand, desBitSize);
    long longMultiplier=calculator.convertToSignedInteger(biMultiplier, desBitSize);
    long longResult=longMultiplicand*longMultiplier;

    String result = Long.toHexString(longResult);
    result = calculator.hexZeroExtend(result,desHexSize);
    String finalResult = result.substring(result.length()-desHexSize);
    registers.set(des,finalResult);

    BigInteger checkTruncatedResult;
    checkTruncatedResult = new BigInteger(finalResult,16);

    EFlags ef = registers.getEFlags();
    // CF = OF = 1; intermediate product is different from the truncated result
    if( checkTruncatedResult.equals(new BigInteger(longResult.toString())) ) {
        ef.setCarryFlag("0");
        ef.setOverflowFlag("0");
    } else{
        ef.setCarryFlag("1");
        ef.setOverflowFlag("1");
    }
}


execute(des,src1,src2,registers,memory) {
    int BYTE = 8;
    int WORD = 16;
    int DWORD = 32;

    int desBitSize = registers.getBitSize(des);
    int desHexSize = registers.getHexSize(des);
    String multiplier = src2.getValue();
    Calculator calculator =  new Calculator(registers,memory);

    if ( src1.isRegister() && src2.isHex() ) {
        if ( (desBitSize == WORD && multiplier.length() <= WORD) || desBitSize == DWORD ) {
            String multiplicand = registers.get(src1);
            multiplyForThreeOperand(des,registers,calculator,multiplicand,multiplier);
        }
    }else if ( src1.isMemory() && src2.isHex() ) {
        if ( (desBitSize == WORD && multiplier.length() <= WORD) || desBitSize == DWORD ) {
            String multiplicand = memory.read(src1,desBitSize);
            multiplyForThreeOperand(des,registers,calculator,multiplicand,multiplier);
        }
    }
}

multiplyForThreeOperand(des,registers,calculator,multiplicand,multiplier) {

    BigInteger biMultiplicand=new BigInteger(multiplicand,16);
    BigInteger biMultiplier=new BigInteger(multiplier,16);

    int desBitSize = registers.getBitSize(des);
    int desHexSize = registers.getHexSize(des);
    long longMultiplicand=calculator.convertToSignedInteger(biMultiplicand, desBitSize);
    long longMultiplier=calculator.convertToSignedInteger(biMultiplier, desBitSize);
    long longResult=longMultiplicand*longMultiplier;

    String result = Long.toHexString(longResult);
    result = calculator.hexZeroExtend(result,desHexSize);
    String finalResult = result.substring(result.length()-desHexSize);
    registers.set(des,finalResult);

    BigInteger checkTruncatedResult;
    checkTruncatedResult = new BigInteger(finalResult,16);

    EFlags ef = registers.getEFlags();
    // CF = OF = 1; intermediate product is different from the truncated result
    if( checkTruncatedResult.equals(new BigInteger(longResult.toString())) ) {
        ef.setCarryFlag("0");
        ef.setOverflowFlag("0");
    } else{
        ef.setCarryFlag("1");
        ef.setOverflowFlag("1");
    }
}
