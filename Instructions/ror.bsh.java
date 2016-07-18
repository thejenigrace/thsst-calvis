execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();
    String carryFlagValue = flags.getCarryFlag();
    if (des.isRegister()) {
        if (src.isRegister() && src.getValue().equals("CL")) {
            System.out.println("SHL register and CL");

            //get size of des
            int desSize = registers.getBitSize(des);
            //			int startIndex = registers.;
            String originalDes = calculator.hexToBinaryString(registers.get(des), des);
            String originalSign = originalDes.charAt(0) + "";

            boolean checkSize = false;
            for (int a: registers.getAvailableSizes()) {
                if (a == desSize) {
                    checkSize = true;
                }
            }

            int count = new BigInteger(registers.get(src), 16).intValue() % 32;
            int limit = count;
            if (checkSize && (limit >= 0 && limit <= 31)) {
                String destination = calculator.hexToBinaryString(registers.get(des), des);
                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes;

                //proceed rotate
                boolean bitSet = false;
                for (int x = 0; x < limit; x++) {
                    bitSet = biResult.testBit(0);
                    biResult = biResult.shiftRight(1);
                    if (bitSet) {
                        biResult = biResult.setBit(desSize - 1);
                        carryFlagValue = "1";
                    } else {
                        biResult = biResult.clearBit(desSize - 1);
                        carryFlagValue = "0";
                    }
                }
                flags.setCarryFlag(carryFlagValue);
                String result = calculator.binaryToHexString(biResult.toString(2), des);
                if (result.length() > 8) {
                    int cut = result.length() - 8;
                    String t = result.substring(cut);
                    registers.set(des, t);
                } else {
                    registers.set(des, result);
                }

                //FLAGS
                if (limit == 0) {
                    //flags not affected
                } else {
                    if (biResult.equals(BigInteger.ZERO)) {
                        flags.setZeroFlag("1");
                    } else {
                        flags.setZeroFlag("0");
                    }

                    String r = calculator.hexToBinaryString(registers.get(des), des);
                    String sign = "" + r.charAt(0);

                    if (limit == 1 && originalSign.equals(sign)) {
                        flags.setOverflowFlag("0");
                    } else if (limit == 1 && !originalSign.equals(sign)) {
                        flags.setOverflowFlag("1");
                    } else {
                        flags.setOverflowFlag("0");
                    }

                    //				flags.setCarryFlag(originalDes.charAt(limit - 1).toString());
                    //flags.setAuxiliaryFlag(undefined)
                }
            }
        } else if (src.isHex() && src.getValue().length() <= 2) {
            System.out.println("ROL register and i8");

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

            int count = new BigInteger((src.getValue()), 16).intValue() % 32;
            int limit = count;
            if (checkSize && (limit >= 0 && limit <= 31)) {
                String destination = calculator.hexToBinaryString(registers.get(des), des);
                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes;

                //proceed rotate
                String carryFlagValue = flags.getCarryFlag();
                boolean bitSet = false;
                for (int x = 0; x < count.intValue(); x++) {

                    bitSet = biResult.testBit(0);
					biResult = biResult.shiftRight(1);



		if (bitSet) {
                        biResult = biResult.setBit(desSize - 1);
                        carryFlagValue = "1";
                    } else {
                        biResult = biResult.clearBit(desSize - 1);
                        carryFlagValue = "0";
                    }
					System.out.println(" result " + calculator.binaryZeroExtend(biResult.toString(2), des));
                }
                flags.setCarryFlag(carryFlagValue);

                String result = calculator.binaryToHexString(biResult.toString(2), des);
                if (result.length() > 8) {
                    int cut = result.length() - 8;
                    String t = result.substring(cut);
                    registers.set(des, t);
                } else {
                    registers.set(des, result);
                }

                //FLAGS
                if (limit == 0) {
                    //flags not affected
                } else {
                    if (biResult.equals(BigInteger.ZERO)) {
                        flags.setZeroFlag("1");
                    } else {
                        flags.setZeroFlag("0");
                    }

                    String r = calculator.hexToBinaryString(registers.get(des), des);
                    String sign = "" + r.charAt(0);


                    if (limit == 1 && originalSign.equals(sign)) {
                        flags.setOverflowFlag("0");
                    } else if (limit == 1 && !originalSign.equals(sign)) {
                        flags.setOverflowFlag("1");
                    } else {
                        flags.setOverflowFlag("0");
                    }

                    //			flags.setCarryFlag(originalDes.charAt(limit - 1).toString());

                    //flags.setAuxiliaryFlag(undefined)
                }
            }
        }
    } else if (des.isMemory() && memory.getBitSize(des) > 0) {
        if (src.isRegister() && src.getValue().equals("CL")) {
            System.out.println("ROL memory and CL");
            //get size of des
            int desSize = memory.getBitSize(des);
            //			int startIndex = registers.;
            String originalDes = calculator.hexToBinaryString(memory.read(des, desSize), des);
            String originalSign = originalDes.charAt(0) + "";

            boolean checkSize = false;
            for (int a: registers.getAvailableSizes()) {
                if (a == desSize) {
                    checkSize = true;
                }
            }

            int count = new BigInteger(registers.get(src), 16).intValue() % 32;
            int limit = count;
            if (checkSize && (limit >= 0 && limit <= 31)) {
                String destination = calculator.hexToBinaryString(memory.read(des, desSize), des);
                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes;

                //proceed rotate
                boolean bitSet = false;
                for (int x = 0; x < limit; x++) {
                    bitSet = biResult.testBit(0);
                    biResult = biResult.shiftRight(1);
                    if (bitSet) {
                        biResult = biResult.setBit(desSize - 1);
                        carryFlagValue = "1";
                    } else {
                        biResult = biResult.clearBit(desSize - 1);
                        carryFlagValue = "0";
                    }
                }
                flags.setCarryFlag(carryFlagValue);
                String result = calculator.binaryToHexString(biResult.toString(2), des);
				if (memory.getHexSize(des) < result.length()) {
					int cut = result.length() - memory.getHexSize(des);
					String t = result.substring(cut);
//
					memory.write(des, t, desSize);
				} else {
					memory.write(des, result, desSize);
				}

                //FLAGS
                if (limit == 0) {
                    //flags not affected
                } else {
                    if (biResult.equals(BigInteger.ZERO)) {
                        flags.setZeroFlag("1");
                    } else {
                        flags.setZeroFlag("0");
                    }

                    String r = calculator.hexToBinaryString(memory.read(des, desSize), des);
                    String sign = "" + r.charAt(0);

                    if (limit == 1 && originalSign.equals(sign)) {
                        flags.setOverflowFlag("0");
                    } else if (limit == 1 && !originalSign.equals(sign)) {
                        flags.setOverflowFlag("1");
                    } else {
                        flags.setOverflowFlag("0");
                    }

                    //				flags.setCarryFlag(originalDes.charAt(limit - 1).toString());
                    //flags.setAuxiliaryFlag(undefined)
                }
            }
        } else if (src.isHex() && src.getValue().length() <= 2) {
            System.out.println("ROL memory and i8");
            //get size of des
            int desSize = memory.getBitSize(des);
            //			int startIndex = registers.;
            String originalDes = calculator.hexToBinaryString(memory.read(des, desSize), des);
            String originalSign = originalDes.charAt(0) + "";

            boolean checkSize = false;
            for (int a: registers.getAvailableSizes()) {
                if (a == desSize) {
                    checkSize = true;
                }
            }

            int count = new BigInteger(src.getValue(), 16).intValue() % 32;
            int limit = count;
            if (checkSize && (limit >= 0 && limit <= 31)) {
                String destination = calculator.hexToBinaryString(memory.read(des, desSize), des);
                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes;

                //proceed rotate
                boolean bitSet = false;
                for (int x = 0; x < limit; x++) {
                    bitSet = biResult.testBit(0);
                    biResult = biResult.shiftRight(1);
                    if (bitSet) {
                        biResult = biResult.setBit(desSize - 1);
                        carryFlagValue = "1";
                    } else {
                        biResult = biResult.clearBit(desSize - 1);
                        carryFlagValue = "0";
                    }
                }
                flags.setCarryFlag(carryFlagValue);
                String result = calculator.binaryToHexString(biResult.toString(2), des);
                /*if (result.length() > 8) {
                    int cut = result.length() - 8;
                    String t = result.substring(cut);
                    memory.write(des, t, desSize);
                } else {
                    memory.write(des, result, desSize);
                }*/
				if (memory.getHexSize(des) < result.length()) {
					int cut = result.length() - memory.getHexSize(des);
					String t = result.substring(cut);
					System.out.println(t + " result");
					memory.write(des, t, desSize);
				} else {
					memory.write(des, result, desSize);
				}

                //FLAGS
                if (limit == 0) {
                    //flags not affected
                } else {
                    if (biResult.equals(BigInteger.ZERO)) {
                        flags.setZeroFlag("1");
                    } else {
                        flags.setZeroFlag("0");
                    }

                    String r = calculator.hexToBinaryString(memory.read(des, desSize), des);
                    String sign = "" + r.charAt(0);

                    if (limit == 1 && originalSign.equals(sign)) {
                        flags.setOverflowFlag("0");
                    } else if (limit == 1 && !originalSign.equals(sign)) {
                        flags.setOverflowFlag("1");
                    } else {
                        flags.setOverflowFlag("0");
                    }

                    //				flags.setCarryFlag(originalDes.charAt(limit - 1).toString());
                    //flags.setAuxiliaryFlag(undefined)
                }
            }
        }
    }
}