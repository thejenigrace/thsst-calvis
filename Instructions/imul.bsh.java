execute(src,registers,memory) {
        int srcBitSize;
        String multiplier;
        Calculator calculator = new Calculator(registers, memory);
        if( src.isRegister() ){
            System.out.println("IMUL (1): src == register");
            srcBitSize = registers.getBitSize(src);
            multiplier = registers.get(src);
        }else if ( src.isMemory() ){
            System.out.println("IMUL (1): src == memory");
            srcBitSize = memory.getBitSize(src);
            multiplier = memory.read(src,srcBitSize);
        }

        System.out.println("srcBitSize = " + srcBitSize);

        if ( srcBitSize==8 ){
            String multiplicand = registers.get("AL");
            multiplyForOneOperand(srcBitSize,registers,calculator,multiplicand,multiplier,null,"AX");
        }else if ( srcBitSize==16 ){
            String multiplicand = registers.get("AX");
            multiplyForOneOperand(srcBitSize,registers,calculator,multiplicand,multiplier,"DX","AX");
        }else if( srcBitSize==32 ){
            String multiplicand = registers.get("EAX");
            multiplyForOneOperand(srcBitSize,registers,calculator,multiplicand,multiplier,"EDX","EAX");
        }
}

multiplyForOneOperand(srcBitSize,registers,calculator,multiplicand,multiplier,registerUpperHalf,registerLowerHalf) {
    // debugging
    System.out.println("MULTIPLICAND = " + multiplicand
    + "\nMULTIPLIER = " + multiplier
    + "\nregisterLowerHalf = " + registerLowerHalf
    + "\nregisterUpperHalf = " + registerUpperHalf);

    BigInteger biMultiplicand = new BigInteger(multiplicand, 16);
    BigInteger biMultiplier = new BigInteger(multiplier, 16);

    int registerLowerHalfBitSize = registers.getBitSize(registerLowerHalf);
    long longMultiplicand = calculator.convertToSignedInteger(biMultiplicand, srcBitSize);
    long longMultiplier = calculator.convertToSignedInteger(biMultiplier,  srcBitSize);
    long longResult = longMultiplicand * longMultiplier;

    System.out.println("longMultiplicand = "+longMultiplicand);
    System.out.println("longMultiplier = "+longMultiplier);
    System.out.println("longResult = "+longResult);

    int registerLowerHalfHexSize = registers.getHexSize(registerLowerHalf);
    String[] results=calculator.cutToCertainSize(Long.toHexString(longResult), registerLowerHalfHexSize);

    registers.set(registerLowerHalf, results[1]);

    EFlags ef = registers.getEFlags();
    BigInteger checkUpperHalf;
    if ( registerUpperHalf != null ){
        registers.set(registerUpperHalf,results[0]);
        checkUpperHalf = new BigInteger(results[0], 16);

        System.out.println(registerUpperHalf + " = " + results[0].toUpperCase());
    } else {
        checkUpperHalf = new BigInteger(registers.get("AH"), 16);
    }

    System.out.println(registerLowerHalf + " = " + results[1].toUpperCase());

    if( checkUpperHalf.equals(BigInteger.ZERO) ){
        System.out.println("CF = 0; OF = 0");
        ef.setCarryFlag("0");
        ef.setOverflowFlag("0");
    } else{
        System.out.println("CF = 1; OF = 1");
        ef.setCarryFlag("1");
        ef.setOverflowFlag("1");
    }
}

execute(des,src,registers,memory) {
        int BYTE = 8;
        int WORD = 16;
        int DWORD = 32;

        System.out.println("IMUL (2) Parameters");

        int desBitSize = registers.getBitSize(des);
        Calculator calculator = new Calculator(registers,memory);

        if ( desBitSize == WORD ){
            System.out.println("IMUL (2): des == register 16");
            if ( src.isRegister() && registers.getBitSize(src) == WORD ) {
                System.out.println("IMUL (2): src == register 16");
                String multiplier = registers.get(src);
                multiplyForTwoOperand(des,registers,calculator,multiplier);
            } else if ( src.isMemory() ){
                System.out.println("IMUL (2): src == memory 16");
                String multiplier = memory.read(src,desBitSize);
                multiplyForTwoOperand(des,registers,calculator,multiplier);
            } else if ( src.isHex() ){
                System.out.println("IMUL (2): src == immediate 8/16");
                String multiplier = src.getValue();

                if ( multiplier.length() <= WORD/4 )
                    multiplyForTwoOperand(des,registers,calculator,multiplier);
            }
        } else if ( desBitSize == DWORD ){
            System.out.println("IMUL (2): des == register 32");
            if ( src.isRegister() && registers.getBitSize(src) == DWORD ) {
                System.out.println("IMUL (2): src == register 32");
                String multiplier = registers.get(src);
                multiplyForTwoOperand(des,registers,calculator,multiplier);
            } else if ( src.isMemory() ){
                System.out.println("IMUL (2): src == memory 32");
                String multiplier = memory.read(src,desBitSize);
                multiplyForTwoOperand(des,registers,calculator,multiplier);
            } else if ( src.isHex() ){
                System.out.println("IMUL (2): src == immediate 8/16/32");
                String multiplier = src.getValue();

                if ( multiplier.length() <= DWORD/4 )
                    multiplyForTwoOperand(des,registers,calculator,multiplier);
            }
        }
}

multiplyForTwoOperand(des,registers,calculator,multiplier) {
        String multiplicand = registers.get(des);

        // debugging
        System.out.println("MULTIPLICAND = " + multiplicand
        + "\nMULTIPLIER = " + multiplier
        + "\nregisterLowerHalf = " + registerLowerHalf
        + "\nregisterUpperHalf = " + registerUpperHalf);

        BigInteger biMultiplicand=new BigInteger(multiplicand,16);
        BigInteger biMultiplier=new BigInteger(multiplier,16);

        int desBitSize = registers.getBitSize(des);
        int desHexSize = registers.getHexSize(des);
        long longMultiplicand=calculator.convertToSignedInteger(biMultiplicand, desBitSize);
        long longMultiplier=calculator.convertToSignedInteger(biMultiplier, desBitSize);
        long longResult=longMultiplicand*longMultiplier;

        // debugging
        System.out.println("longMultiplicand = "+longMultiplicand);
        System.out.println("longMultiplier = "+longMultiplier);
        System.out.println("longResult = "+longResult);

        BigInteger checkTruncatedResult;
        String[] results=calculator.cutToCertainSize(Long.toHexString(longResult),desHexSize);
        if ( results[0].equals("0000") || results[0].equals("00000000") ){
            registers.set(des,results[1]);
            checkTruncatedResult = new BigInteger(results[1], 16);
        }else {
            registers.set(des,results[0]);
            checkTruncatedResult = new BigInteger(results[0], 16);
        }

        EFlags ef = registers.getEFlags();
        if( checkTruncatedResult.equals(new BigInteger(longResult.toString())) ){
            System.out.println("CF = 1; OF = 1"); // debugging
            ef.setCarryFlag("1");
            ef.setOverflowFlag("1");
        } else{
            System.out.println("CF = 0; OF = 0"); // debugging
            ef.setCarryFlag("0");
            ef.setOverflowFlag("0");
        }
}


execute(des,src1,src2,registers,memory) {
    int BYTE = 8;
    int WORD = 16;
    int DWORD = 32;
    System.out.println("IMUL (3) Parameters");

    int desBitSize = registers.getBitSize(des);
    int desHexSize = registers.getHexSize(des);
    String multiplier = src2.getValue();
    Calculator calculator =  new Calculator(registers,memory);

    if ( src1.isRegister() && src2.isHex() ){
        if ( (desBitSize == WORD && multiplier.length() <= WORD) || desBitSize == DWORD ){
            String multiplicand = registers.get(src1);
            multiplyForThreeOperand(des,registers,calculator,multiplicand,multiplier);
        }
    }else if ( src1.isMemory() && src2.isHex() ){
        if ( (desBitSize == WORD && multiplier.length() <= WORD) || desBitSize == DWORD ){
            String multiplicand = memory.read(src1,desBitSize);
            multiplyForThreeOperand(des,registers,calculator,multiplicand,multiplier);
        }
    }
}

multiplyForThreeOperand(des,registers,calculator,multiplicand,multiplier) {
        // debugging
        System.out.println("MULTIPLICAND = " + multiplicand
        + "\nMULTIPLIER = " + multiplier
        + "\nregisterLowerHalf = " + registerLowerHalf
        + "\nregisterUpperHalf = " + registerUpperHalf);

        BigInteger biMultiplicand=new BigInteger(multiplicand,16);
        BigInteger biMultiplier=new BigInteger(multiplier,16);

        int desBitSize = registers.getBitSize(des);
        int desHexSize = registers.getHexSize(des);
        long longMultiplicand=calculator.convertToSignedInteger(biMultiplicand, desBitSize);
        long longMultiplier=calculator.convertToSignedInteger(biMultiplier, desBitSize);
        long longResult=longMultiplicand*longMultiplier;

        // debugging
        System.out.println("longMultiplicand = "+longMultiplicand);
        System.out.println("longMultiplier = "+longMultiplier);
        System.out.println("longResult = "+longResult);

        BigInteger checkTruncatedResult;
        String[] results=calculator.cutToCertainSize(Long.toHexString(longResult),desHexSize);
        if ( results[0].equals("0000") || results[0].equals("00000000") ){
            registers.set(des,results[1]);
            checkTruncatedResult = new BigInteger(results[1], 16);
        }else {
            registers.set(des,results[0]);
            checkTruncatedResult = new BigInteger(results[0], 16);
        }

        EFlags ef = registers.getEFlags();
        if( checkTruncatedResult.equals(new BigInteger(longResult.toString())) ){
            System.out.println("CF = 1; OF = 1"); // debugging
            ef.setCarryFlag("1");
            ef.setOverflowFlag("1");
        } else{
            System.out.println("CF = 0; OF = 0"); // debugging
            ef.setCarryFlag("0");
            ef.setOverflowFlag("0");
        }
}
