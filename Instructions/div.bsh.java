execute(src, registers, memory) {
        if ( src.isRegister() ){
            System.out.println("DIV source register");

            String x = registers.get(src);

            if ( registers.getBitSize(src) == 8 ){
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
            } else if ( registers.getBitSize(src) == 16 ){
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
            } else if ( registers.getBitSize(src) == 32 ){
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
        }
 }