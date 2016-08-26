execute(des, src, count, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags eflags = registers.getEFlags();

    String carryFlagValue = eflags.getCarryFlag();
    if (des.isRegister() && src.isRegister()) {
        //get size of des, src
        int desSize = registers.getBitSize(des);
        int srcSize = registers.getBitSize(src);
        if (((desSize == 32 && srcSize == 32) || (desSize == 16 && srcSize == 16)) && count.getValue().equals("CL")) {

            //get size of des
            String originalDes = calculator.hexToBinaryString(registers.get(des), des);
            String originalSign = originalDes.charAt(0) + "";


            boolean checkSize = false;

            for (int a: registers.getAvailableSizes()) {
                if (a == desSize) {
                    checkSize = true;
                }
            }

            int cnt = new BigInteger(registers.get(count), 16).intValue() % 31;

            int limit = cnt;

            if (checkSize && (limit >= 0 && limit <= 32 - 1)) {
                String destination = calculator.hexToBinaryString(registers.get(des), des);
                String source = calculator.hexToBinaryString(registers.get(src), src);
                //

                BigInteger biSrc = new BigInteger(source, 2);
                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes;
                boolean bitSet = false;
                boolean srcBitSet = false;

                String carryFlag = eflags.getCarryFlag();


                for (int x = 0; x < limit; x++) {
                    bitSet = biDes.testBit(desSize - 1);
                    biResult = biResult.shiftRight(1);

                    if (bitSet) {
                        carryFlagValue = "1";
                    } else {
                        carryFlagValue = "0";
                    }
                    //check sign bit of source then transfer to lsb of destination
                    srcBitSet = biSrc.testBit(0);
                    switch (srcBitSet) {
                        case true:
                            biResult = biResult.setBit(desSize - 1 - x);
                            break;
                        case false:
                            biResult = biResult.clearBit(desSize - 1 - x);
                            break;
                    }
                }

                eflags.setCarryFlag(carryFlagValue);
                String result = calculator.binaryToHexString(biResult.toString(2), des);

                if (result.length() > 8) {
                    int cut = result.length() - 8;
                    String t = result.substring(cut);
                    registers.set(des, t);
                } else {
                    registers.set(des, result);
                }

                if (limit == 0) {
                    //eflags not affected
                } else {
                    if (calculator.binaryZeroExtend(biResult.toString(2), des).equals(calculator.binaryZeroExtend(BigInteger.ZERO.toString(2), des))) {
                        eflags.setZeroFlag("1");
                    } else {
                        eflags.setZeroFlag("0");
                    }
                    String r = calculator.hexToBinaryString(registers.get(des), des);
                    String sign = "" + r.charAt(0);
                    eflags.setSignFlag(sign);

                    String parity = calculator.checkParity(r);
                    eflags.setParityFlag(parity);
                    if (limit == 1 && originalSign.equals(sign)) {
                        eflags.setOverflowFlag("0");
                    } else if (limit == 1 && !originalSign.equals(sign)) {
                        eflags.setOverflowFlag("1");
                    }
                    eflags.setCarryFlag(originalDes.charAt(limit - 1).toString());
                }
            }
        } else if (((desSize == 32 && srcSize == 32) || (desSize == 16 && srcSize == 16)) && count.getValue().length() <= 2) {
            //get size of des
            int desSize = registers.getBitSize(des);
            String originalDes = calculator.hexToBinaryString(registers.get(des), des);
            String originalSign = originalDes.charAt(0) + "";

            boolean checkSize = false;
            for (int a: registers.getAvailableSizes()) {
                if (a == desSize) {
                    checkSize = true;
                }
            }

            int cnt = new BigInteger(count.getValue(), 16).intValue() % 32;
            int limit = cnt.intValue();

            if (checkSize && (limit >= 0 && limit <= 32 - 1)) {
                String destination = calculator.hexToBinaryString(registers.get(des), des);
                String source = calculator.hexToBinaryString(registers.get(src), src);

                BigInteger biSrc = new BigInteger(source, 2);
                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes;

                boolean bitSet = false;
                boolean srcBitSet = false;
                String carryFlag = eflags.getCarryFlag();

                for (int x = 0; x < limit; x++) {
                    bitSet = biDes.testBit(desSize - 1);
                    biResult = biResult.shiftRight(1);

                    if (bitSet) {
                        carryFlagValue = "1";
                    } else {
                        carryFlagValue = "0";
                    }
                    //check sign bit of source then transfer to lsb of destination
                    srcBitSet = biSrc.testBit(0);

                    switch (srcBitSet) {
                        case true:
                            biResult = biResult.setBit(desSize - 1 - x);
                            break;
                        case false:
                            biResult = biResult.clearBit(desSize - 1 - x);
                            break;
                    }
                }

                eflags.setCarryFlag(carryFlagValue);

                String result = calculator.binaryToHexString(biResult.toString(2), des);
                if (result.length() > 8) {
                    int cut = result.length() - 8;
                    String t = result.substring(cut);
                    registers.set(des, t);
                } else {
                    registers.set(des, result);
                }

                if (limit == 0) {
                    //eflags not affected
                } else {
                    if (calculator.binaryZeroExtend(biResult.toString(2), des).equals(calculator.binaryZeroExtend(BigInteger.ZERO.toString(2), des))) {
                        eflags.setZeroFlag("1");
                    } else {
                        eflags.setZeroFlag("0");
                    }

                    String r = calculator.hexToBinaryString(registers.get(des), des);
                    String sign = "" + r.charAt(0);
                    eflags.setSignFlag(sign);

                    String parity = calculator.checkParity(r);
                    eflags.setParityFlag(parity);

                    if (limit == 1 && originalSign.equals(sign)) {
                        eflags.setOverflowFlag("0");
                    } else if (limit == 1 && !originalSign.equals(sign)) {
                        eflags.setOverflowFlag("1");
                    }

                    eflags.setCarryFlag(originalDes.charAt(limit - 1).toString());
                }
            }
        }
    } else if (des.isMemory()) {

        //get size of des, src
        int desSize = memory.getBitSize(des);
        int srcSize = registers.getBitSize(src);

        if (((desSize == 32 && srcSize == 32) || (desSize == 16 && srcSize == 16)) && count.getValue().equals("CL")) {

            //get size of des
            String originalDes = calculator.hexToBinaryString(memory.read(des, desSize), des);
            String originalSign = originalDes.charAt(0) + "";


            boolean checkSize = false;
            for (int a: registers.getAvailableSizes()) {
                if (a == desSize) {
                    checkSize = true;
                }
            }

            int cnt = new BigInteger(registers.get(count), 16).intValue() % 32;

            int limit = cnt;

            if (checkSize && (limit >= 0 && limit <= 32 - 1)) {

                String destination = calculator.hexToBinaryString(memory.read(des, desSize), des);
                String source = calculator.hexToBinaryString(registers.get(src), src);


                BigInteger biSrc = new BigInteger(source, 2);
                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes;
                boolean bitSet = false;
                boolean srcBitSet = false;
                String carryFlag = eflags.getCarryFlag();


                for (int x = 0; x < limit; x++) {
                    bitSet = biDes.testBit(desSize - 1);
                    biResult = biResult.shiftRight(1);

                    if (bitSet) {
                        carryFlagValue = "1";
                    } else {
                        carryFlagValue = "0";
                    }
                    //check sign bit of source then transfer to lsb of destination
                    srcBitSet = biSrc.testBit(0);
                    switch (srcBitSet) {
                        case true:
                            biResult = biResult.setBit(desSize - 1 - x);
                            break;
                        case false:
                            biResult = biResult.clearBit(desSize - 1 - x);
                            break;
                    }
                }



                eflags.setCarryFlag(carryFlagValue);
                String result = calculator.binaryToHexString(biResult.toString(2), des);

                memory.write(des, result, desSize);

                if (limit == 0) {
                    //eflags not affected
                } else {
                    if (calculator.binaryZeroExtend(biResult.toString(2), des).equals(calculator.binaryZeroExtend(BigInteger.ZERO.toString(2), des))) {
                        eflags.setZeroFlag("1");
                    } else {
                        eflags.setZeroFlag("0");
                    }
                    String r = calculator.hexToBinaryString(memory.read(des, desSize), des);
                    String sign = "" + r.charAt(0);
                    eflags.setSignFlag(sign);

                    String parity = calculator.checkParity(r);
                    eflags.setParityFlag(parity);
                    if (limit == 1 && originalSign.equals(sign)) {
                        eflags.setOverflowFlag("0");
                    } else if (limit == 1 && !originalSign.equals(sign)) {
                        eflags.setOverflowFlag("1");
                    } else {
                        eflags.setOverflowFlag("0");
                    }
                }
            }
        } else if (((desSize == 32 && srcSize == 32) || (desSize == 16 && srcSize == 16)) && count.getValue().length() <= 2) {

            //get size of des
            String originalDes = calculator.hexToBinaryString(memory.read(des, desSize), des);
            String originalSign = originalDes.charAt(0) + "";


            boolean checkSize = false;
            for (int a: registers.getAvailableSizes()) {
                if (a == desSize) {
                    checkSize = true;
                }
            }

            int cnt = new BigInteger(count.getValue(), 16).intValue() % 32;

            int limit = cnt;

            if (checkSize && (limit >= 0 && limit <= 32 - 1)) {

                String destination = calculator.hexToBinaryString(memory.read(des, desSize), des);
                String source = calculator.hexToBinaryString(registers.get(src), src);


                BigInteger biSrc = new BigInteger(source, 2);
                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes;
                boolean bitSet = false;
                boolean srcBitSet = false;
                String carryFlag = eflags.getCarryFlag();


                for (int x = 0; x < limit; x++) {
                    bitSet = biDes.testBit(desSize - 1);
                    biResult = biResult.shiftRight(1);

                    if (bitSet) {
                        carryFlagValue = "1";
                    } else {
                        carryFlagValue = "0";
                    }
                    //check sign bit of source then transfer to lsb of destination
                    srcBitSet = biSrc.testBit(0);
                    switch (srcBitSet) {
                        case true:
                            biResult = biResult.setBit(desSize - 1 - x);
                            break;
                        case false:
                            biResult = biResult.clearBit(desSize - 1 - x);
                            break;
                    }
                }



                eflags.setCarryFlag(carryFlagValue);
                String result = calculator.binaryToHexString(biResult.toString(2), des);

                memory.write(des, result, desSize);

                if (limit == 0) {
                    //eflags not affected
                } else {
                    if (calculator.binaryZeroExtend(biResult.toString(2), des).equals(calculator.binaryZeroExtend(BigInteger.ZERO.toString(2), des))) {
                        eflags.setZeroFlag("1");
                    } else {
                        eflags.setZeroFlag("0");
                    }
                    String r = calculator.hexToBinaryString(memory.read(des, desSize), des);
                    String sign = "" + r.charAt(0);
                    eflags.setSignFlag(sign);

                    String parity = calculator.checkParity(r);
                    eflags.setParityFlag(parity);
                    if (limit == 1 && originalSign.equals(sign)) {
                        eflags.setOverflowFlag("0");
                    } else if (limit == 1 && !originalSign.equals(sign)) {
                        eflags.setOverflowFlag("1");
                    } else {
                        eflags.setOverflowFlag("0");
                    }
                }
            }
        }
    }
}
