execute(src, registers, memory) {
        if ( src.isRegister() ){
            System.out.println("IMUL source register");

            String x = registers.get(src);

            EFlags ef = registers.getEFlags();

            if ( registers.getBitSize(src) == 8 ){
                BigInteger biX = new BigInteger(x, 16);
                BigInteger biY = new BigInteger(registers.get("AL"), 16);
                BigInteger result = biY.multiply(biX);

                System.out.println("AL = " + biY.toString(2));
                System.out.println("src (byte) = " + biX.toString(2));
                System.out.println("result = " + result.toString());

                Calculator c = new Calculator(registers, memory);
                String[] finalize = c.cutToCertainSize(result.toString(16), src);
                registers.set("AX", result.toString(16));

                System.out.println("finalize[0] = " + finalize[0]);
                System.out.println("finalize[1] = " + finalize[1]);

                BigInteger check = new BigInteger(finalize[0], 16);
                if(check.equals(BigInteger.ZERO)){
                    ef.setCarryFlag("0");
                    ef.setOverflowFlag("0");
                } else{
                    ef.setCarryFlag("1");
                    ef.setOverflowFlag("1");
                }
            } else if ( registers.getBitSize(src) == 16 ){
                BigInteger biX = new BigInteger(x, 16);
                BigInteger biY = new BigInteger(registers.get("AX"), 16);
                BigInteger result = biY.multiply(biX);
            } else if ( registers.getBitSize(src) == 32 ){
                BigInteger biX = new BigInteger(x, 16);
                BigInteger biY = new BigInteger(registers.get("EAX"), 16);
                BigInteger result = biY.multiply(biX);
            }
        }
 }