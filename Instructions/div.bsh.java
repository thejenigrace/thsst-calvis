execute(src, registers, memory) {
            int srcBitSize;
            String divisor;
            EFlags ef = registers.getEFlags();
            Calculator calculator = new Calculator(registers, memory);

            if ( src.isRegister() ){
                System.out.println("DIV: src == register");
                srcBitSize = registers.getBitSize(src);
                divisor = registers.get(src);
            }else if( src.isMemory() ){
                System.out.println("DIV: src == memory");
                srcBitSize = memory.getBitSize(src);
                divisor = memory.read(src, srcBitSize);
            }

            if ( srcBitSize == 8 ){
                String dividend = registers.get("AX");
                divide(registers, calculator, dividend, divisor, "AL", "AH");
            } else if ( srcBitSize == 16 ){
                String dividend = registers.get("DX") + registers.get("AX");
                divide(registers, calculator, dividend, divisor, "AX", "DX");
            } else if ( srcBitSize == 32 ){
                String dividend = registers.get("EDX") + registers.get("EAX");
                divide(registers, calculator, dividend, divisor, "EAX", "EDX");
            }
        }

        divide(registers, calculator, dividend, divisor, registerForQuotient, registerForRemainder) {
            // debugging
            System.out.println("DIVIDEND = " + dividend
                + "\nDIVISOR = " + divisor
                + "\nregisterForQuotient = " + registerForQuotient
                + "\nregisterForRemainder = " + registerForRemainder);

            BigInteger biDividend = new BigInteger(dividend, 16);
            BigInteger biDivisor = new BigInteger(divisor, 16);
            BigInteger[] biResult = biDividend.divideAndRemainder(biDivisor);

            System.out.println("DECIMAL DIVIDEND = " + biDividend.toString()
            + "\nDECIMAL DIVISOR = " + biDivisor.toString());
            System.out.println("quotient = " + biResult[0].toString(16));
            System.out.println("remainder = " + biResult[1].toString(16));

            long longQuotient = Long.parseLong(biResult[0].toString());
            long longRemainder = Long.parseLong(biResult[1].toString());

            System.out.println("long quotient = " + Long.toHexString(longQuotient));
            System.out.println("long remainder = " + Long.toHexString(longRemainder));

            int resultBitSize = registers.getBitSize(registerForQuotient);
            if(check(longQuotient, resultBitSize)) {
                int resultHexSize = registers.getHexSize(registerForQuotient);
                String quotient = calculator.cutToCertainHexSize("getLower", biResult[0].toString(16), resultHexSize);
                String remainder = calculator.cutToCertainHexSize("getLower", biResult[1].toString(16), resultHexSize);

                System.out.println("FINAL QUOTIENT = " + quotient.toUpperCase());
                System.out.println("FINAL REMAINDER = " + remainder.toUpperCase());

                registers.set(registerForQuotient, quotient);
                registers.set(registerForRemainder, remainder);
            } else {
                throw new ArithmeticException("Divide Error");
            }
        }

        boolean check(long quotient, int size){
            boolean answer=false;
            Long max8=Long.parseLong("255");
            Long max16=Long.parseLong("65535");
            Long max32=Long.parseLong("4294967295"); //2E+32 - 1
            if( size==8 && max8>=quotient){
                System.out.println("8!");
                answer=true;
            }else if( size==16 && max16>=quotient){
                System.out.println("16!");
                answer=true;
            }else if( size==32 && max32>=quotient){
                System.out.println("32!");
                answer=true;
            }

            return answer;
        }
