execute(src,registers,memory){
        if(src.isRegister()){
            System.out.println("(1) IMUL src = register");

            String x=registers.get(src);

            EFlags ef=registers.getEFlags();

            if(registers.getBitSize(src)==8){
                BigInteger biY=new BigInteger(registers.get("AL"),16);
                BigInteger biX=new BigInteger(x,16);

                Calculator c=new Calculator(registers,memory);
                long a=c.convertToSignedInteger(biY,8);
                long b=c.convertToSignedInteger(biX,8);
                long r=a*b;

                System.out.println("a = "+a);
                System.out.println("b = "+b);
                System.out.println("rr = "+r);

                System.out.println("AL = "+biY.toString(2));
                System.out.println("src (byte) = "+biX.toString(2));
                System.out.println("result = "+Long.toHexString(r));

                String finalize=c.cutToCertainHexSize(Long.toHexString(r),4);
        //
        //                System.out.println("finalize[0] = " + finalize[0]);
        //                System.out.println("finalize[1] = " + finalize[1]);

                registers.set("AX",finalize);
            }else if(registers.getBitSize(src)==16){
                BigInteger biY=new BigInteger(registers.get("AX"),16);
                BigInteger biX=new BigInteger(x,16);

                Calculator c=new Calculator(registers,memory);
                long a=c.convertToSignedInteger(biY,16);
                long b=c.convertToSignedInteger(biX,16);
                long r=a*b;

                System.out.println("a = "+a);
                System.out.println("b = "+b);
                System.out.println("rr = "+r);

                System.out.println("AX = "+biY.toString(2));
                System.out.println("src (byte) = "+biX.toString(2));
                System.out.println("result = "+Long.toHexString(r));

                String fix=c.cutToCertainHexSize(Long.toHexString(r),8);
                String[]finalize=c.cutToCertainSize(fix,src);
                registers.set("DX",finalize[0]);
                registers.set("AX",finalize[1]);

                System.out.println("finalize[0] = "+finalize[0]);
                System.out.println("finalize[1] = "+finalize[1]);
            }else if(registers.getBitSize(src)==32){
                BigInteger biY=new BigInteger(registers.get("EAX"),16);
                BigInteger biX=new BigInteger(x,16);

                Calculator c=new Calculator(registers,memory);
                long a=c.convertToSignedInteger(biY,32);
                long b=c.convertToSignedInteger(biX,32);
                long r=a*b;

                System.out.println("a = "+a);
                System.out.println("b = "+b);
                System.out.println("rr = "+r);

                BigInteger result=new BigInteger(r.toString());

                System.out.println("EAX = "+biY.toString(2));
                System.out.println("src (byte) = "+biX.toString(2));
                System.out.println("result = "+Long.toHexString(r));

                String fix=c.cutToCertainHexSize(Long.toHexString(r),16);
                String[]finalize=c.cutToCertainSize(fix,src);
                registers.set("EDX",finalize[0]);
                registers.set("EAX",finalize[1]);

                System.out.println("finalize[0] = "+finalize[0]);
                System.out.println("finalize[1] = "+finalize[1]);
            }
        }else if(src.isMemory()){
            System.out.println("(1) IMUL src = memory");
            int src_reg_size = registers.getBitSize(src);
            String x = registers.get(src);

            System.out.println("x = " +  x);
        }}

        execute(des,src,registers,memory){
        System.out.println("imul 2 parameters");
        }

        execute(des,src1,src2,registers,memory){
        System.out.println("imul 3 paraments");
        }
