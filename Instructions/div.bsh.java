execute(src, registers, memory) {
        int srcBitSize;
        String x = "";
        EFlags ef=registers.getEFlags();
        if ( src.isRegister() ){
            System.out.println("DIV src = register");
            srcBitSize = registers.getBitSize(src);
            x = registers.get(src);
        }else if( src.isMemory() ){
            System.out.println("DIV src = memory");
            srcBitSize = memory.getBitSize(src);
            x = memory.read(src, srcBitSize);
        }
//        if ( src.isRegister() )

//            String x = registers.get(src);

            if ( srcBitSize == 8 ){
                BigInteger biX = new BigInteger(x, 16);
                BigInteger biY = new BigInteger(registers.get("AX"), 16);
                BigInteger[] result = biY.divideAndRemainder(biX);
                registers.set("AL", result[0].toString(16));
                registers.set("AH", result[1].toString(16));

                // debugging
                System.out.println("AX = " + biY.toString(16));
                System.out.println("src (byte) = " + biX.toString(16));
                System.out.println("quotient = " + result[0].toString(16));
                System.out.println("remainder = " + result[1].toString(16));
            } else if ( srcBitSize == 16 ){
                BigInteger biX = new BigInteger(x, 16);
                String y = registers.get("DX") + registers.get("AX");
                BigInteger biY = new BigInteger(y, 16);
                BigInteger[] result = biY.divideAndRemainder(biX);
                registers.set("AX", result[0].toString(16));
                registers.set("DX", result[1].toString(16));

                // debugging
                System.out.println("DX, AX = " + y);
                System.out.println("src (word) = " + biX.toString(16));
                System.out.println("quotient = " + result[0].toString(16));
                System.out.println("remainder = " + result[1].toString(16));
            } else if ( srcBitSize == 32 ){
                BigInteger biX = new BigInteger(x, 16);
                String y = registers.get("EDX") + registers.get("EAX");
                BigInteger biY = new BigInteger(y, 16);
                BigInteger[] result = biY.divideAndRemainder(biX);
                registers.set("EAX", result[0].toString(16));
                registers.set("EDX", result[1].toString(16));

                // debugging
                System.out.println("EDX, EAX = " + y);
                System.out.println("src (dword) = " + biX.toString(16));
                System.out.println("quotient = " + result[0].toString(16));
                System.out.println("remainder = " + result[1].toString(16));
            }
//        }
 }