//to fix: if count > operand size, undefined
//check for 64 bit mode
execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();
    int bitMode = 32;
    if (des.isRegister()) {
        if (src.isRegister() && src.getValue().equals("CL")) {
            System.out.println("SHL register and CL");

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

            int count = new BigInteger(registers.get(src), 16).intValue() % bitMode;
            int limit = count;
            if (checkSize && (limit >= 0 && limit <= 31)) {
                String destination = calculator.hexToBinaryString(registers.get(des), des);
                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes;

				//proceed rotate
				String carryFlagValue = flags.getCarryFlag();
				boolean bitSet = false;
				for (int x = 1; x < limit + 1; x++) {
				bitSet = biResult.testBit(0);
				biResult = biResult.shiftRight(1);
				if (bitSet) {
				carryFlagValue = "1";
				} else {
				carryFlagValue = "0";
				}
				boolean isFlagSet = flags.getCarryFlag().equals("1");
				if(isFlagSet){
				biResult = biResult.setBit(desSize - 1);
				}
				else{
				biResult = biResult.clearBit(desSize - 1);
				}
				flags.setCarryFlag(carryFlagValue);
				System.out.print(flags.getCarryFlag() + " ");
				System.out.println(calculator.binaryZeroExtend(biResult.toString(2), des) + " shifted");
				}

                String result = calculator.binaryToHexString(biResult.toString(2), des);
			if (registers.getHexSize(des) < result.length()) {
				int cut = result.length() - registers.getHexSize(des);
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
                        // flags.setOverflowFlag(undefined);
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

            int count = new BigInteger((src.getValue()), 16).intValue() % bitMode;
            int limit = count;
            if (checkSize && (limit >= 0 && limit <= 31)) {
                String destination = calculator.hexToBinaryString(registers.get(des), des);
                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes;

				//proceed rotate
				String carryFlagValue = flags.getCarryFlag();
				boolean bitSet = false;
				for (int x = 1; x < limit + 1; x++) {
				bitSet = biResult.testBit(0);
				biResult = biResult.shiftRight(1);
				if (bitSet) {
				carryFlagValue = "1";
				} else {
				carryFlagValue = "0";
				}
				boolean isFlagSet = flags.getCarryFlag().equals("1");
				if(isFlagSet){
				biResult = biResult.setBit(desSize - 1);
				}
				else{
				biResult = biResult.clearBit(desSize - 1);
				}
				flags.setCarryFlag(carryFlagValue);
				System.out.println(calculator.binaryZeroExtend(biResult.toString(2), des) + " shifted");
				}

                String result = calculator.binaryToHexString(biResult.toString(2), des);
		if (registers.getHexSize(des) < result.length()) {
		int cut = result.length() - registers.getHexSize(des);
		String t = result.substring(cut);
		registers.set(des, t);
		} else {
		registers.set(des, result);
		}

                //FLAGS
                EFlags flags = registers.getEFlags();
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
                        // flags.setOverflowFlag(undefined);
                    }

                    //			flags.setCarryFlag(originalDes.charAt(limit - 1).toString());

                    //flags.setAuxiliaryFlag(undefined)
                }
            }
        }
    } else if (des.isMemory() && memory.getBitSize(des) > 0) {
        if (src.isRegister() && src.getValue().equals("CL") && memory.getBitSize(des) > 0) {
            System.out.println("ROL memory and CL");

            //get size of des
            int desSize = memory.getBitSize(des);
            String originalDes = calculator.hexToBinaryString(memory.read(des, desSize), des);
            String originalSign = originalDes.charAt(0) + "";

            boolean checkSize = false;
            for (int a: registers.getAvailableSizes()) {
                if (a == desSize) {
                    checkSize = true;
                }
            }

            int count = new BigInteger(registers.get(src), 16).intValue() % bitMode;
            int limit = count;
            if (checkSize && (limit >= 0 && limit <= 31)) {
                String destination = calculator.hexToBinaryString(memory.read(des, desSize), des);
                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes;
				System.out.println(biResult.toString(16) + " fuck");
				//proceed rotate
				String carryFlagValue = flags.getCarryFlag();
				boolean bitSet = false;
				for (int x = 1; x < limit + 1; x++) {
				bitSet = biResult.testBit(0);
				biResult = biResult.shiftRight(1);
				if (bitSet) {
				carryFlagValue = "1";
				} else {
				carryFlagValue = "0";
				}
				boolean isFlagSet = flags.getCarryFlag().equals("1");
				if(isFlagSet){
				biResult = biResult.setBit(desSize - 1);
				}
				else{
				biResult = biResult.clearBit(desSize - 1);
				}
				flags.setCarryFlag(carryFlagValue);
				System.out.println(calculator.binaryZeroExtend(biResult.toString(2), des) + " shifted" + " flags value:" + flags.getCarryFlag());
				}

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
                        // flags.setOverflowFlag(undefined);
                    }

                    //				flags.setCarryFlag(originalDes.charAt(limit - 1).toString());
                    //flags.setAuxiliaryFlag(undefined)
                }
            }
        } else if (src.isHex() && src.getValue().length() <= 2) {
            System.out.println("ROL register and i8");

            //get size of des
            int desSize = memory.getBitSize(des);
            String originalDes = calculator.hexToBinaryString(memory.read(des, desSize), des);
            String originalSign = originalDes.charAt(0) + "";

            boolean checkSize = false;
            for (int a: registers.getAvailableSizes()) {
                if (a == desSize) {
                    checkSize = true;
                }
            }

            int count = new BigInteger((src.getValue()), 16).intValue() % bitMode;
            int limit = count;
            if (checkSize && (limit >= 0 && limit <= 31)) {
                String destination = calculator.hexToBinaryString(memory.read(des, desSize), des);
                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes;

				//proceed rotate
				String carryFlagValue = flags.getCarryFlag();
				boolean bitSet = false;
				for (int x = 1; x < limit + 1; x++) {
				bitSet = biResult.testBit(0);
				biResult = biResult.shiftRight(1);
				if (bitSet) {
				carryFlagValue = "1";
				} else {
				carryFlagValue = "0";
				}
				boolean isFlagSet = flags.getCarryFlag().equals("1");
				if(isFlagSet){
				biResult = biResult.setBit(desSize - 1);
				}
				else{
				biResult = biResult.clearBit(desSize - 1);
				}
				flags.setCarryFlag(carryFlagValue);
				System.out.println(calculator.binaryZeroExtend(biResult.toString(2), des) + " shifted");
				}
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
                EFlags flags = registers.getEFlags();
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

                }
            }
        }

    }
}