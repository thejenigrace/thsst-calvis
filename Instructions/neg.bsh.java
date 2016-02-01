execute(des, registers, memory){

        String x = registers.get(des);

            if(des.isRegister()) {
                BigInteger bx = new BigInteger(x, 16);
                BigInteger r = bx.negate();

                System.out.println("bx = " + bx);
                System.out.println("r = " + r);

                registers.set(des, r.toString(16));
            }
        }