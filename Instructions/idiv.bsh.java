execute(src, registers, memory) {
//        if ( src.isRegister() ){
//            System.out.println("IDIV source register");

//            String x = registers.get(src);
            int srcBitSize;
            String x = "";
            EFlags ef=registers.getEFlags();
            Calculator c = new Calculator(registers, memory);
            if ( src.isRegister() ){
                System.out.println("DIV src = register");
                srcBitSize = registers.getBitSize(src);
                x = registers.get(src);
            }else if( src.isMemory() ){
                System.out.println("DIV src = memory");
                srcBitSize = memory.getBitSize(src);
                x = memory.read(src, srcBitSize);
            }


            if ( srcBitSize == 8 ){
                BigInteger biX = new BigInteger(x, 16);
                BigInteger biY = new BigInteger(registers.get("AX"), 16);

                System.out.println("AX = " + biY.toString(16));
                System.out.println("src (byte) = " + biX.toString(16));

                long a = c.convertToSignedInteger(biX, 8);
                long b = c.convertToSignedInteger(biY, 16);
                biX = new BigInteger(a.toString());
                biY = new BigInteger(b.toString());
                BigInteger[] result = biY.divideAndRemainder(biX);

                System.out.println("result0 = " + result[0].toString());
                System.out.println("result1 = " + result[1].toString());

                long r0 = Long.parseLong(result[0].toString());
                long r1 = Long.parseLong(result[1].toString());

                String fix1 = c.cutToCertainHexSize(Long.toHexString(r0), 2);
                String fix2 = c.cutToCertainHexSize(Long.toHexString(r1), 2);

                System.out.println("fix1 = " + fix1);
                System.out.println("fix2 = " + fix2);

                registers.set("AL", fix1);
                registers.set("AH", fix2);
            } else if ( srcBitSize == 16 ){
                BigInteger biX = new BigInteger(x, 16);
                String y = registers.get("DX") + registers.get("AX");
                BigInteger biY = new BigInteger(y, 16);

                System.out.println("DX, AX = " + y);
                System.out.println("src (word) = " + biX.toString(16));

                long a = c.convertToSignedInteger(biX, 16);
                long b = c.convertToSignedInteger(biY, 32);
                biX = new BigInteger(a.toString());
                biY = new BigInteger(b.toString());
                BigInteger[] result = biY.divideAndRemainder(biX);

                System.out.println("result0 = " + result[0].toString());
                System.out.println("result1 = " + result[1].toString());

                long r0 = Long.parseLong(result[0].toString());
                long r1 = Long.parseLong(result[1].toString());

                String fix1 = c.cutToCertainHexSize(Long.toHexString(r0), 4);
                String fix2 = c.cutToCertainHexSize(Long.toHexString(r1), 4);

                System.out.println("fix1 = " + fix1);
                System.out.println("fix2 = " + fix2);

                registers.set("AX", fix1);
                registers.set("DX", fix2);
            } else if ( srcBitSize == 32 ){
                BigInteger biX = new BigInteger(x, 16);
                String y = registers.get("EDX") + registers.get("EAX");
                BigInteger biY = new BigInteger(y, 16);

                System.out.println("EDX, EAX = " + y);
                System.out.println("src (dword) = " + biX.toString(16));

                long a = c.convertToSignedInteger(biX, 32);
                long b = c.convertToSignedInteger(biY, 64);
                biX = new BigInteger(a.toString());
                biY = new BigInteger(b.toString());
                BigInteger[] result = biY.divideAndRemainder(biX);

                System.out.println("result0 = " + result[0].toString());
                System.out.println("result1 = " + result[1].toString());

                long r0 = Long.parseLong(result[0].toString());
                long r1 = Long.parseLong(result[1].toString());

                String fix1 = c.cutToCertainHexSize(Long.toHexString(r0), 8);
                String fix2 = c.cutToCertainHexSize(Long.toHexString(r1), 8);

                System.out.println("fix1 = " + fix1);
                System.out.println("fix2 = " + fix2);

                registers.set("EAX", fix1);
                registers.set("EDX", fix2);
            }
//        }
 }