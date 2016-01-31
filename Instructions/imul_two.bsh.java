    execute(src, registers, memory){
        if(src.isRegister()){
            System.out.println("IMUL source register");

            String x=registers.get(src);

            EFlags ef=registers.getEFlags();

            if(registers.getBitSize(src)==8){
                BigInteger biY=new BigInteger(registers.get("AL"),16);
                BigInteger biX=new BigInteger(x,16);

                Calculator c = new Calculator(registers, memory);
                long a = c.convertToSignedInteger(biY, 8);
                long b = c.convertToSignedInteger(biX, 8);
                long r = a * b;

                System.out.println("a = " + a);
                System.out.println("b = " + b);
                System.out.println("rr = " + r);

                BigInteger result = new BigInteger(r.toString());

                System.out.println("AL = "+biY.toString(2));
                System.out.println("src (byte) = "+biX.toString(2));
                System.out.println("result = "+ result.toString(16));

                registers.set("AX", result.toString(16));
            } else if(registers.getBitSize(src)==16){
                BigInteger biY=new BigInteger(registers.get("AX"),16);
                BigInteger biX=new BigInteger(x,16);

                Calculator c = new Calculator(registers, memory);
                long a = c.convertToSignedInteger(biY, 16);
                long b = c.convertToSignedInteger(biX, 16);
                long r = a * b;

                System.out.println("a = " + a);
                System.out.println("b = " + b);
                System.out.println("rr = " + r);

                BigInteger result = new BigInteger(r.toString());

                System.out.println("AL = "+biY.toString(2));
                System.out.println("src (byte) = "+biX.toString(2));
                System.out.println("result = "+ result.toString(16));

                String[] finalize = c.cutToCertainSize(result.toString(16), src);
                registers.set("DX", finalize[0]);
                registers.set("AX", finalize[1]);

                System.out.println("finalize[0] = " + finalize[0]);
                System.out.println("finalize[1] = " + finalize[1]);
            }  else if(registers.getBitSize(src)==32){
                BigInteger biY=new BigInteger(registers.get("EAX"),16);
                BigInteger biX=new BigInteger(x,16);

                Calculator c = new Calculator(registers, memory);
                long a = c.convertToSignedInteger(biY, 32);
                long b = c.convertToSignedInteger(biX, 32);
                long r = a * b;

                System.out.println("a = " + a);
                System.out.println("b = " + b);
                System.out.println("rr = " + r);

                BigInteger result = new BigInteger(r.toString());

                System.out.println("AL = "+biY.toString(2));
                System.out.println("src (byte) = "+biX.toString(2));
                System.out.println("result = "+ result.toString(16));

                String[] finalize = c.cutToCertainSize(result.toString(16), src);
                registers.set("EDX", finalize[0]);
                registers.set("EAX", finalize[1]);

                System.out.println("finalize[0] = " + finalize[0]);
                System.out.println("finalize[1] = " + finalize[1]);
            }
        }}
