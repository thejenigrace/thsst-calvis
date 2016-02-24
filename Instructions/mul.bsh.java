execute(src, registers, memory) {
        int srcBitSize;
        String multiplier;
        Calculator calculator = new Calculator(registers, memory);
        if ( src.isRegister() ){
            System.out.println("MUL: src == register");
            srcBitSize = registers.getBitSize(src);
            multiplier = registers.get(src);
        }else if( src.isMemory() ){
            System.out.println("MUL: src == memory");
            srcBitSize = memory.getBitSize(src);
            multiplier = memory.read(src, srcBitSize);
        }

        if ( srcBitSize == 8 ){
            String multiplicand = registers.get("AL");
            multiply(registers, calculator, multiplicand, multiplier, null, "AX");
        } else if ( srcBitSize == 16 ){
            String multiplicand = registers.get("AX");
            multiply(registers, calculator, multiplicand, multiplier, "DX", "AX");
        } else if ( srcBitSize == 32 ){
            String multiplicand = registers.get("EAX");
            multiply(registers, calculator, multiplicand, multiplier, "EDX", "EAX");
        }}

        multiply(registers, calculator, multiplicand, multiplier, registerUpperHalf, registerLowerHalf) {
            // debugging
            System.out.println("MULTIPLICAND = " + multiplicand
                + "\nMULTIPLIER = " + multiplier
                + "\nregisterLowerHalf = " + registerLowerHalf
                + "\nregisterUpperHalf = " + registerUpperHalf);

            BigInteger biMultiplicand = new BigInteger(multiplicand, 16);
            BigInteger biMultiplier = new BigInteger(multiplier, 16);
            BigInteger biResult = biMultiplicand.multiply(biMultiplier);

            int registerLowerHalfHexSize = registers.getHexSize(registerLowerHalf);
            System.out.println("registerLowerHalfHexSize = " + registerLowerHalfHexSize);
            String[] results = calculator.cutToCertainSize(biResult.toString(16), registerLowerHalfHexSize);
            registers.set(registerLowerHalf, results[1]);

            EFlags ef = registers.getEFlags();
            BigInteger checkUpperHalf;
            if ( registerUpperHalf != null ){
                registers.set(registerUpperHalf,results[0]);
                checkUpperHalf = new BigInteger(results[0], 16);

                System.out.println(registerUpperHalf + " = " + results[0].toUpperCase());
            } else {
                String ah = registers.get("AH");
                checkUpperHalf = new BigInteger(ah, 16);
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