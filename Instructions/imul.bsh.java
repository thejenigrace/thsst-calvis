execute(src,registers,memory){
        int srcSize;
        String x = "";
        EFlags ef=registers.getEFlags();
        if(src.isRegister()) {
            System.out.println("(1) IMUL src = register");
            srcSize = registers.getBitSize(src);
            x = registers.get(src);
        }else if(src.isMemory()) {
            System.out.println("(1) IMUL src = memory");
            srcSize = memory.getBitSize(src);
            x = memory.read(src, srcSize);
        }

        System.out.println("srcSize = " + srcSize);
        System.out.println("x = " + x);

        if(srcSize==8){
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

            String finalize=c.cutToCertainHexSize(Long.toHexString(r), srcSize/2);
            //                System.out.println("finalize[0] = " + finalize[0]);
            //                System.out.println("finalize[1] = " + finalize[1]);

            registers.set("AX",finalize);

        }else if(srcSize==16){
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

            String fix=c.cutToCertainHexSize(Long.toHexString(r), srcSize/2);
            String[]finalize=c.cutToCertainSize(fix, srcSize/4);
            registers.set("DX",finalize[0]);
            registers.set("AX",finalize[1]);

            System.out.println("finalize[0] = "+finalize[0]);
            System.out.println("finalize[1] = "+finalize[1]);

        }else if(srcSize==32){
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

            String fix=c.cutToCertainHexSize(Long.toHexString(r), srcSize/2);
            String[]finalize=c.cutToCertainSize(fix, srcSize/4);
            registers.set("EDX",finalize[0]);
            registers.set("EAX",finalize[1]);

            System.out.println("finalize[0] = "+finalize[0]);
            System.out.println("finalize[1] = "+finalize[1]);
        }}

        execute(des,src,registers,memory){
            System.out.println("IMUL 2 parameters");

            int desBitSize = registers.getBitSize(des);
            int desHexSize = registers.getHexSize(des);
            String y = registers.get(des);
            EFlags ef=registers.getEFlags();

            if(desBitSize == 16) {
                System.out.println("(2) IMUL des = register 16");

                if( src.isRegister() && registers.getBitSize(src) == 16 ) {
                    System.out.println("(2) IMUL src = register 16");
                    BigInteger biY=new BigInteger(y,16);
                    BigInteger biX=new BigInteger(registers.get(src),16);

                    Calculator c=new Calculator(registers,memory);
                    long a=c.convertToSignedInteger(biY, desBitSize);
                    long b=c.convertToSignedInteger(biX, desBitSize);
                    long r=a*b;

                    System.out.println("a = "+a);
                    System.out.println("b = "+b);
                    System.out.println("r = "+r);

                    BigInteger result=new BigInteger(r.toString());

                    System.out.println("des (word) = "+biY.toString(2));
                    System.out.println("src (word) = "+biX.toString(2));
                    System.out.println("result = "+Long.toHexString(r));

                    String fix=c.cutToCertainHexSize(Long.toHexString(r), desBitSize/2);
                    String[]finalize=c.cutToCertainSize(fix, desHexSize);
                    registers.set(des,finalize[0]);

                    System.out.println("finalize[0] = "+finalize[0]);
                    System.out.println("finalize[1] = "+finalize[1]);
                } else if( src.isMemory() ) {
                    System.out.println("(2) IMUL src = memory 16");
                    BigInteger biY=new BigInteger(y,16);
                    BigInteger biX=new BigInteger(memory.read(src,desBitSize),16);

                    Calculator c=new Calculator(registers,memory);
                    long a=c.convertToSignedInteger(biY, desBitSize);
                    long b=c.convertToSignedInteger(biX, desBitSize);
                    long r=a*b;

                    System.out.println("a = "+a);
                    System.out.println("b = "+b);
                    System.out.println("r = "+r);

                    BigInteger result=new BigInteger(r.toString());

                    System.out.println("des (word) = "+biY.toString(2));
                    System.out.println("src (word) = "+biX.toString(2));
                    System.out.println("result = "+Long.toHexString(r));

                    String fix=c.cutToCertainHexSize(Long.toHexString(r), desBitSize/2);
                    String[]finalize=c.cutToCertainSize(fix, desHexSize);
                    registers.set(des,finalize[0]);

                    System.out.println("finalize[0] = "+finalize[0]);
                    System.out.println("finalize[1] = "+finalize[1]);
                } else if( src.isHex()) {
                    System.out.println("(2) IMUL src = immediate 8 or 16");
                    String x = src.getValue();

                    if(x.length() == desHexSize/2 || x.length() == desHexSize) {
                        BigInteger biY=new BigInteger(y,16);
                        BigInteger biX=new BigInteger(x,16);

                        Calculator c=new Calculator(registers,memory);
                        long a=c.convertToSignedInteger(biY, desBitSize);
                        long b=c.convertToSignedInteger(biX, desBitSize);
                        long r=a*b;

                        System.out.println("a = "+a);
                        System.out.println("b = "+b);
                        System.out.println("r = "+r);

                        BigInteger result=new BigInteger(r.toString());

                        System.out.println("des (word) = "+biY.toString(2));
                        System.out.println("src (word) = "+biX.toString(2));
                        System.out.println("result = "+Long.toHexString(r));

                        String fix=c.cutToCertainHexSize(Long.toHexString(r), desBitSize/2);
                        String[]finalize=c.cutToCertainSize(fix, desHexSize);
                        registers.set(des,finalize[0]);

                        System.out.println("finalize[0] = "+finalize[0]);
                        System.out.println("finalize[1] = "+finalize[1]);
                    }
                }
            } else if(desBitSize == 32) {
                System.out.println("(2) IMUL des = register 32");

                if( src.isRegister() && registers.getBitSize(src) == 32 ) {
                    System.out.println("(2) IMUL src = register 32");
                    BigInteger biY=new BigInteger(y,16);
                    BigInteger biX=new BigInteger(registers.get(src),16);

                    Calculator c=new Calculator(registers,memory);
                    long a=c.convertToSignedInteger(biY, desBitSize);
                    long b=c.convertToSignedInteger(biX, desBitSize);
                    long r=a*b;

                    System.out.println("a = "+a);
                    System.out.println("b = "+b);
                    System.out.println("r = "+r);

                    BigInteger result=new BigInteger(r.toString());

                    System.out.println("des (word) = "+biY.toString(2));
                    System.out.println("src (word) = "+biX.toString(2));
                    System.out.println("result = "+Long.toHexString(r));

                    String fix=c.cutToCertainHexSize(Long.toHexString(r), desBitSize/2);
                    String[]finalize=c.cutToCertainSize(fix, desHexSize);
                    registers.set(des,finalize[0]);

                    System.out.println("finalize[0] = "+finalize[0]);
                    System.out.println("finalize[1] = "+finalize[1]);
                } else if( src.isMemory() ) {
                    System.out.println("(2) IMUL src = memory 32");
                    BigInteger biY=new BigInteger(y,16);
                    BigInteger biX=new BigInteger(memory.read(src,desBitSize),16);

                    Calculator c=new Calculator(registers,memory);
                    long a=c.convertToSignedInteger(biY, desBitSize);
                    long b=c.convertToSignedInteger(biX, desBitSize);
                    long r=a*b;

                    System.out.println("a = "+a);
                    System.out.println("b = "+b);
                    System.out.println("r = "+r);

                    BigInteger result=new BigInteger(r.toString());

                    System.out.println("des (word) = "+biY.toString(2));
                    System.out.println("src (word) = "+biX.toString(2));
                    System.out.println("result = "+Long.toHexString(r));

                    String fix=c.cutToCertainHexSize(Long.toHexString(r), desBitSize/2);
                    String[]finalize=c.cutToCertainSize(fix, desHexSize);
                    registers.set(des,finalize[0]);

                    System.out.println("finalize[0] = "+finalize[0]);
                    System.out.println("finalize[1] = "+finalize[1]);
                } else if( src.isHex()) {
                    System.out.println("(2) IMUL src = immediate 8/16/32");
                    String x = src.getValue();

                    if(x.length() == desHexSize/4 || x.length() == desHexSize/2 || x.length() == desHexSize) {
                        BigInteger biY=new BigInteger(y,16);
                        BigInteger biX=new BigInteger(x,16);

                        Calculator c=new Calculator(registers,memory);
                        long a=c.convertToSignedInteger(biY, desBitSize);
                        long b=c.convertToSignedInteger(biX, desBitSize);
                        long r=a*b;

                        System.out.println("a = "+a);
                        System.out.println("b = "+b);
                        System.out.println("r = "+r);

                        BigInteger result=new BigInteger(r.toString());

                        System.out.println("des (word) = "+biY.toString(2));
                        System.out.println("src (word) = "+biX.toString(2));
                        System.out.println("result = "+Long.toHexString(r));

                        String fix=c.cutToCertainHexSize(Long.toHexString(r), desBitSize/2);
                        String[]finalize=c.cutToCertainSize(fix, desHexSize);
                        registers.set(des,finalize[0]);

                        System.out.println("finalize[0] = "+finalize[0]);
                        System.out.println("finalize[1] = "+finalize[1]);
                    }
                }
            }
        }

        execute(des,src1,src2,registers,memory){
            System.out.println("IMUL 3 parameters");

            int desBitSize = registers.getBitSize(des);
            int desHexSize = registers.getHexSize(des);
            String y = src2.getValue();
            EFlags ef=registers.getEFlags();

//            if(desBitSize == 16 || desBitSize == 32) {
//                System.out.println("(3) IMUL des = register 16");

                if( src1.isRegister() && src2.isHex() ){
                    if ( desBitSize == 16 && y.length() == desHexSize/2 || y.length() == desHexSize ){
                        System.out.println("(3) IMUL src1 = register, src2 = i8/16");
                        BigInteger biY=new BigInteger(y,16);
                        BigInteger biX=new BigInteger(registers.get(src1),16);

                        Calculator c=new Calculator(registers,memory);
                        long a=c.convertToSignedInteger(biY,desBitSize);
                        long b=c.convertToSignedInteger(biX,desBitSize);
                        long r=a*b;

                        System.out.println("a = "+a);
                        System.out.println("b = "+b);
                        System.out.println("r = "+r);

                        BigInteger result=new BigInteger(r.toString());

                        System.out.println("src2 (word) = "+biY.toString(2));
                        System.out.println("src1 (word) = "+biX.toString(2));
                        System.out.println("result = "+Long.toHexString(r));

                        String fix=c.cutToCertainHexSize(Long.toHexString(r),desBitSize/2);
                        String[]finalize=c.cutToCertainSize(fix,desHexSize);
                        registers.set(des,finalize[0]);

                        System.out.println("finalize[0] = "+finalize[0]);
                        System.out.println("finalize[1] = "+finalize[1]);
                    } else if ( desBitSize == 32 ){
                        System.out.println("(3) IMUL src1 = register, src2 = i");
                        BigInteger biY=new BigInteger(y,16);
                        BigInteger biX=new BigInteger(registers.get(src1),16);

                        Calculator c=new Calculator(registers,memory);
                        long a=c.convertToSignedInteger(biY,desBitSize);
                        long b=c.convertToSignedInteger(biX,desBitSize);
                        long r=a*b;

                        System.out.println("a = "+a);
                        System.out.println("b = "+b);
                        System.out.println("r = "+r);

                        BigInteger result=new BigInteger(r.toString());

                        System.out.println("src2 (word) = "+biY.toString(2));
                        System.out.println("src1 (word) = "+biX.toString(2));
                        System.out.println("result = "+Long.toHexString(r));

                        String fix=c.cutToCertainHexSize(Long.toHexString(r),desBitSize/2);
                        String[]finalize=c.cutToCertainSize(fix,desHexSize);
                        registers.set(des,finalize[0]);

                        System.out.println("finalize[0] = "+finalize[0]);
                        System.out.println("finalize[1] = "+finalize[1]);
                    }
                } else if( src1.isMemory() && src2.isHex() ){
                    if ( desBitSize == 16 && y.length() == desHexSize/2 || y.length() == desHexSize ){
                        System.out.println("(3) IMUL src1 = m16, src2 = i8/i16");
                        BigInteger biY=new BigInteger(y,16);
                        BigInteger biX=new BigInteger(memory.read(src1,desBitSize),16);

                        Calculator c=new Calculator(registers,memory);
                        long a=c.convertToSignedInteger(biY,desBitSize);
                        long b=c.convertToSignedInteger(biX,desBitSize);
                        long r=a*b;

                        System.out.println("a = "+a);
                        System.out.println("b = "+b);
                        System.out.println("r = "+r);

                        BigInteger result=new BigInteger(r.toString());

                        System.out.println("src2 (word) = "+biY.toString(2));
                        System.out.println("src1 (word) = "+biX.toString(2));
                        System.out.println("result = "+Long.toHexString(r));

                        String fix=c.cutToCertainHexSize(Long.toHexString(r),desBitSize/2);
                        String[]finalize=c.cutToCertainSize(fix,desHexSize);
                        registers.set(des,finalize[0]);

                        System.out.println("finalize[0] = "+finalize[0]);
                        System.out.println("finalize[1] = "+finalize[1]);
                    } else  if( desBitSize == 32 ){
                        System.out.println("(3) IMUL src1 = m16, src2 = i");
                        BigInteger biY=new BigInteger(y,16);
                        BigInteger biX=new BigInteger(memory.read(src1,desBitSize),16);

                        Calculator c=new Calculator(registers,memory);
                        long a=c.convertToSignedInteger(biY,desBitSize);
                        long b=c.convertToSignedInteger(biX,desBitSize);
                        long r=a*b;

                        System.out.println("a = "+a);
                        System.out.println("b = "+b);
                        System.out.println("r = "+r);

                        BigInteger result=new BigInteger(r.toString());

                        System.out.println("src2 (word) = "+biY.toString(2));
                        System.out.println("src1 (word) = "+biX.toString(2));
                        System.out.println("result = "+Long.toHexString(r));

                        String fix=c.cutToCertainHexSize(Long.toHexString(r),desBitSize/2);
                        String[]finalize=c.cutToCertainSize(fix,desHexSize);
                        registers.set(des,finalize[0]);

                        System.out.println("finalize[0] = "+finalize[0]);
                        System.out.println("finalize[1] = "+finalize[1]);
                    }
                }

//            }
        }



