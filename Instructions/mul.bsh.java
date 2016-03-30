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
        //     String[] results = calculator.cutToCertainSize(biResult.toString(16), registerLowerHalfHexSize);
        //     registers.set(registerLowerHalf, results[1]);

        // Get the substring of the result that will fit to the size of the Lower Half Register
            String result = biResult.toString(16);
            System.out.println("result = " + result);
            result = calculator.hexZeroExtend(result,registerLowerHalfHexSize*2);
            System.out.println("extend result = " + result);
            String finalResult = result.substring(result.length()-registerLowerHalfHexSize);
            System.out.println("finalResult = " + finalResult);

            registers.set(registerLowerHalf, finalResult);

        //     EFlags ef = registers.getEFlags();
        //     BigInteger checkUpperHalf;
        //     if ( registerUpperHalf != null ){
        //         registers.set(registerUpperHalf,results[0]);
        //         checkUpperHalf = new BigInteger(results[0], 16);
            //
        //         System.out.println(registerUpperHalf + " = " + results[0].toUpperCase());
        //     } else {
        //         checkUpperHalf = new BigInteger(registers.get("AH"), 16);
        //     }
            //
        //     System.out.println(registerLowerHalf + " = " + results[1].toUpperCase());

                EFlags ef = registers.getEFlags();
                BigInteger checkUpperHalf;
                if ( registerUpperHalf != null ){
                        System.out.println(result.length());
                        int start = result.length() - registerLowerHalfHexSize * 2;
                        int end = result.length() - registerLowerHalfHexSize;
                        String upperResult = result.substring(start, end);
                        registers.set(registerUpperHalf,upperResult);
                        checkUpperHalf = new BigInteger(upperResult, 16);

                        System.out.println(registerUpperHalf + " = " + upperResult.toUpperCase());

                        // System.out.println(registerUpperHalf + " = " + results[0].toUpperCase());
                } else {
                        checkUpperHalf = new BigInteger(registers.get("AH"), 16);
                }

                // System.out.println(registerLowerHalf + " = " + results[1].toUpperCase())
                System.out.println(registerLowerHalf + " = " + finalResult.toUpperCase());

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
