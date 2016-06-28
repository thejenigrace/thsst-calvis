execute(src, registers, memory) throws Exception {
            int srcBitSize;
            String divisor;
            EFlags ef = registers.getEFlags();
            Calculator calculator = new Calculator(registers, memory);
            if ( src.isRegister() ){
                System.out.println("DIV src = register");
                srcBitSize = registers.getBitSize(src);
                divisor = registers.get(src);
            }else if ( src.isMemory() ){
                System.out.println("DIV src = memory");
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

            int resultBitSize = registers.getBitSize(registerForQuotient);

            long dvd = calculator.convertToSignedInteger(biDividend, resultBitSize*2);
            long dvs = calculator.convertToSignedInteger(biDivisor, resultBitSize);
            biDividend = new BigInteger(dvd.toString());
            biDivisor = new BigInteger(dvs.toString());
            BigInteger[] biResult = biDividend.divideAndRemainder(biDivisor);

            System.out.println("quotient = " + biResult[0].toString());
            System.out.println("remainder = " + biResult[1].toString());

            long longQuotient = Long.parseLong(biResult[0].toString());
            long longRemainder = Long.parseLong(biResult[1].toString());

            System.out.println("long quotient = " + Long.toHexString(longQuotient));
            System.out.println("long remainder = " + Long.toHexString(longRemainder));

            if(check(longQuotient, resultBitSize)) {
                int resultHexSize = registers.getHexSize(registerForQuotient);
                String quotient = calculator.cutToCertainHexSize("getLower", Long.toHexString(longQuotient), resultHexSize);
                String remainder = calculator.cutToCertainHexSize("getLower", Long.toHexString(longRemainder), resultHexSize);

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
            Long min8=Long.parseLong("-128");
            Long max8=Long.parseLong("127");
            Long min16=Long.parseLong("-32768");
            Long max16=Long.parseLong("32767");
            Long min32=Long.parseLong("-2147483648");
            Long max32=Long.parseLong("2147483647");
            if( size==8 && max8>=quotient && min8<=quotient ){
                System.out.println("8!");
                answer=true;
            }else if( size==16 &&max16>=quotient && min16<=quotient ){
                System.out.println("16!");
                answer=true;
            }else if( size==32 && max32>=quotient && min32<=quotient ){
                System.out.println("32!");
                answer=true;
            }

            return answer;
        }
