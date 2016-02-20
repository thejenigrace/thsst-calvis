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

            String quotient = calculator.cutToCertainHexSize("original", biResult[0].toString(16), resultHexSize);
            String remainder = calculator.cutToCertainHexSize("original", biResult[1].toString(16), resultHexSize);

            registers.set(registerForQuotient, quotient);
            registers.set(registerForRemainder, remainder);

            // debugging
            System.out.println("DECIMAL DIVIDEND = " + biDividend.toString()
                + "\nDECIMAL DIVISOR = " + biDivisor.toString());
            System.out.println("quotient = " + biResult[0].toString(16));
            System.out.println("remainder = " + biResult[1].toString(16));
            System.out.println("FINAL QUOTIENT = " + quotient.toUpperCase());
            System.out.println("FINAL REMAINDER = " + remainder.toUpperCase());
        }