execute(des, src, registers, memory) {
    String numberOfF = "";
    String resultingValue = "";
    int sizeOfF = 0;
    if (des.isRegister())
        sizeOfF = registers.getBitSize(des);
    else if (des.isMemory()) {
        if (src.isRegister())
            sizeOfF = registers.getBitSize(src);
        else
            sizeOfF = memory.getBitSize(des);
    }
    for (int x = 0; x < sizeOfF / 4; x++) {
        numberOfF += "F";
    }

    if (des.isRegister()) {
        int desSize = registers.getBitSize(des);


        if (src.isRegister() && desSize == registers.getBitSize(src)) {
            System.out.println("ADD register to register");
            String x = registers.get(src);
            String y = registers.get(des);

            if (registers.getBitSize(des) == registers.getBitSize(src)) {
                // Addition in Binary Format
                BigInteger biX = new BigInteger(x, 16);
                BigInteger biY = new BigInteger(y, 16);
                BigInteger result = biY.add(biX);

                Calculator c = new Calculator(registers, memory);
                if (result.toString(16).length() > registers.getBitSize(src) / 4) {
                    resultingValue = result.toString(2).substring(1);
                } else {
                    resultingValue = result.toString(2);
                }
                registers.set(des, c.binaryToHexString(c.binaryZeroExtend(resultingValue, des), des));

                EFlags ef = registers.getEFlags();
                BigInteger biC = new BigInteger(numberOfF, 16);
                if (result.compareTo(biC) == 1)
                    ef.setCarryFlag("1");
                else
                    ef.setCarryFlag("0");

                ef.setParityFlag(c.checkParity(result.toString(2)));

                ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));
                if (result.testBit(desSize - 1))
                    ef.setSignFlag("1");
                else
                    ef.setSignFlag("0");

				
                if (c.binaryZeroExtend(resultingValue, des).equals(c.binaryZeroExtend("0", des)))
                    ef.setZeroFlag("1");
                else
                    ef.setZeroFlag("0");

                ef.setOverflowFlag(c.checkOverflowAdd(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0), c.binaryZeroExtend(resultingValue, des).charAt(0)));
            }
        } else if (src.isHex()) {
            String x = src.getValue();
            String y = registers.get(des);

            // Addition in Binary Format
            BigInteger biX = new BigInteger(x, 16);
            BigInteger biY = new BigInteger(y, 16);
            BigInteger result = biY.add(biX);
            System.out.println("result" + result);
            Calculator c = new Calculator(registers, memory);
            if (result.toString(16).length() > registers.getBitSize(des) / 4) {
                resultingValue = result.toString(2).substring(1);
            } else {
                resultingValue = result.toString(2);
            }
            registers.set(des, c.binaryToHexString(c.binaryZeroExtend(resultingValue, des), des));
            EFlags ef = registers.getEFlags();
            BigInteger biC = new BigInteger(numberOfF, 16);

            if (result.compareTo(biC) == 1)
                ef.setCarryFlag("1");
            else
                ef.setCarryFlag("0");

            ef.setParityFlag(c.checkParity(result.toString(2)));

            ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));

            if (result.testBit(desSize - 1))
                ef.setSignFlag("1");
            else
                ef.setSignFlag("0");

            if (c.binaryZeroExtend(resultingValue, des).equals(c.binaryZeroExtend("0", des)))
                ef.setZeroFlag("1");
            else
                ef.setZeroFlag("0");

            ef.setOverflowFlag(c.checkOverflowAdd(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0), c.binaryZeroExtend(resultingValue, des).charAt(0)));
        } else if (src.isMemory()) {

            String x = memory.read(src, desSize);
            String y = registers.get(des);

            // Addition in Binary Format
            BigInteger biX = new BigInteger(x, 16);
            BigInteger biY = new BigInteger(y, 16);
            BigInteger result = biY.add(biX);

            Calculator c = new Calculator(registers, memory);
            registers.set(des, c.binaryToHexString(c.binaryZeroExtend(resultingValue, des), des));

            EFlags ef = registers.getEFlags();
            BigInteger biC = new BigInteger(numberOfF, 16);
            if (result.compareTo(biC) == 1)
                ef.setCarryFlag("1");
            else
                ef.setCarryFlag("0");

            ef.setParityFlag(c.checkParity(result.toString(2)));

            ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));


            if (result.testBit(desSize - 1))
                ef.setSignFlag("1");
            else
                ef.setSignFlag("0");


            if (c.binaryZeroExtend(resultingValue, des).equals(c.binaryZeroExtend("0", des)))
                ef.setZeroFlag("1");
            else
                ef.setZeroFlag("0");

            ef.setOverflowFlag(c.checkOverflowAdd(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0), c.binaryZeroExtend(resultingValue, des).charAt(0)));
        }
    } else if (des.isMemory()) {

        int desSize = memory.getBitSize(des);
        if (src.isRegister()) {
            System.out.println("ADD register to register");
            String x = registers.get(src);
            String y = memory.read(des, registers.getBitSize(src));
            BigInteger biX = new BigInteger(x, 16);
            BigInteger biY = new BigInteger(y, 16);
            BigInteger result = biY.add(biX);
            Calculator c = new Calculator(registers, memory);

            if (result.toString(16).length() > registers.getBitSize(src) / 4) {
                resultingValue = result.toString(2).substring(1);
            } else {
                resultingValue = result.toString(2);
            }
            memory.write(des, c.binaryToHexString(c.binaryZeroExtend(resultingValue, des), des), desSize);
            EFlags ef = registers.getEFlags();
            BigInteger biC = new BigInteger(numberOfF, 16);

            if (result.compareTo(biC) == 1)
                ef.setCarryFlag("1");
            else
                ef.setCarryFlag("0");

            ef.setParityFlag(c.checkParity(result.toString(2)));
            ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));
            if (result.testBit(registers.getBitSize(src) - 1))
                ef.setSignFlag("1");
            else
                ef.setSignFlag("0");

            System.out.println("ADD register to register");
            if (c.binaryZeroExtend(resultingValue, des).equals(c.binaryZeroExtend("0", des)))
                ef.setZeroFlag("1");
            else
                ef.setZeroFlag("0");

            ef.setOverflowFlag(c.checkOverflowAdd(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0), c.binaryZeroExtend(resultingValue, des).charAt(0)));
        } else if (src.isHex()) {
            System.out.println("ADD register to register");
            String x = src.getValue();
            String y = memory.read(des, desSize);

            // Addition in Binary Format
            BigInteger biX = new BigInteger(x, 16);
            BigInteger biY = new BigInteger(y, 16);
            BigInteger result = biY.add(biX);

            Calculator c = new Calculator(registers, memory);

            if (result.toString(16).length() > memory.getBitSize(des) / 4) {
                resultingValue = result.toString(2).substring(1);
            } else {
                resultingValue = result.toString(2);
            }
            memory.write(des, c.binaryToHexString(c.binaryZeroExtend(resultingValue, des), des), desSize);

            EFlags ef = registers.getEFlags();
            BigInteger biC = new BigInteger(numberOfF, 16);
            if (result.compareTo(biC) == 1)
                ef.setCarryFlag("1");
            else
                ef.setCarryFlag("0");

            ef.setParityFlag(c.checkParity(result.toString(2)));

            ef.setAuxiliaryFlag(c.checkAuxiliary(biX.toString(16), biY.toString(16)));

            if (result.testBit(desSize - 1))
                ef.setSignFlag("1");
            else
                ef.setSignFlag("0");


            if (c.binaryZeroExtend(resultingValue, des).equals(c.binaryZeroExtend("0", des)))
                ef.setZeroFlag("1");
            else
                ef.setZeroFlag("0");

            ef.setOverflowFlag(c.checkOverflowAdd(c.binaryZeroExtend(biY.toString(2), src).charAt(0), c.binaryZeroExtend(biX.toString(2), src).charAt(0), c.binaryZeroExtend(resultingValue, des).charAt(0)));
        }
    }
}