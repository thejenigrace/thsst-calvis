execute(des, src, registers, memory){
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    if( des.isRegister() && registers.getBitSize(des) == 16 ) {
        if( src.isRegister() && registers.getBitSize(src) == 16 ) {
            String destination = getDestinationRegister(registers, calculator, des);
            String s = calculator.hexToBinaryString(registers.get(src), src);
            BigInteger source = new BigInteger(s, 2);
            BigInteger mod = new BigInteger("00010000", 2);
            BigInteger[] r = source.divideAndRemainder(mod);

            //Flags
            setFlags(flags, destination.charAt(r[1].intValue()));

            storeResultToDesRegister(registers, calculator, des, destination, r[1].intValue());
        }
        else if( src.isHex() && src.getValue().length() <= 2 ) {
            BigInteger l = new BigInteger(src.getValue(), 16);
            int limit = l.intValue();

            if( limit >= 0 && limit <= 15 ) {
                String destination = getDestinationRegister(registers, calculator, des);
                String s = calculator.hexToBinaryString(src.getValue(), src);
                BigInteger source = new BigInteger(s, 2);

                //Flags
                setFlags(flags, destination.charAt(source.intValue()));

                storeResultToDesRegister(registers, calculator, des, destination, source.intValue());
            }
        }
    }
    else if( des.isRegister() && registers.getBitSize(des) == 32 ) {
        if( src.isRegister() && registers.getBitSize(src) == 32 ) {
            String destination = getDestinationRegister(registers, calculator, des);
            String s = calculator.hexToBinaryString(registers.get(src), src);
            BigInteger source = new BigInteger(s, 2);
            BigInteger mod = new BigInteger("00100000", 2);
            BigInteger[] r = source.divideAndRemainder(mod);

            //Flags
            setFlags(flags, destination.charAt(r[1].intValue()));

            storeResultToDesRegister(registers, calculator, des, destination, r[1].intValue());
        }
        else if( src.isHex() && src.getValue().length() <= 2 ) {
            BigInteger l = new BigInteger(src.getValue(), 16);
            int limit = l.intValue();

            if( limit >= 0 && limit <= 31 ) {
                String destination = getDestinationRegister(registers, calculator, des);
                String s = calculator.hexToBinaryString(src.getValue(), src);
                BigInteger source = new BigInteger(s, 2);

                //Flags
                setFlags(flags, destination.charAt(source.intValue()));

                storeResultToDesRegister(registers, calculator, des, destination, source.intValue());
            }
        }
    }
    else if( des.isMemory() && memory.getBitSize(des) == 16 ) {
        if( src.isRegister() && registers.getBitSize(src) == 16 ) {
            String destination = getDestinationMemory(memory, calculator, des);
            String s = calculator.hexToBinaryString(registers.get(src), src);
            BigInteger source = new BigInteger(s, 2);

            if( source.intValue() >= 0 && source.intValue() <= 15 ) {
                //Flags
                setFlags(flags, destination.charAt(source.intValue()));

                storeResultToDesMemory(memory, calculator, des, destination, source);
            }
        }
        else if( src.isHex() && src.getValue().length() <= 2 ) {
            BigInteger l = new BigInteger(src.getValue(), 16);
            int limit = l.intValue();

            if( limit >= 0 && limit <= 16 ) {
                String destination = getDestinationMemory(memory, calculator, des);
                String s = calculator.hexToBinaryString(src.getValue(), src);
                BigInteger source = new BigInteger(s, 2);

                //Flags
                setFlags(flags, destination.charAt(source.intValue()));

                storeResultToDesMemory(memory, calculator, des, destination, source);
            }
        }
    }
    else if( des.isMemory() && memory.getBitSize(des) == 32 ) {
        if( src.isRegister() && registers.getBitSize(src) == 32 ) {
            String destination = getDestinationMemory(memory, calculator, des);
            String s = calculator.hexToBinaryString(registers.get(src), src);
            BigInteger source = new BigInteger(s, 2);

            if( source.intValue() >= 0 && source.intValue() <= 31 ) {
                //Flags
                setFlags(flags, destination.charAt(source.intValue()));

                storeResultToDesMemory(memory, calculator, des, destination, source);
            }
        }
        else if( src.isHex() && src.getValue().length() <= 2 ) {
            BigInteger l = new BigInteger(src.getValue(), 16);
            int limit = l.intValue();

            if( limit >= 0 && limit <= 31 ) {
                String destination = getDestinationMemory(memory, calculator, des);
                String s = calculator.hexToBinaryString(src.getValue(), src);
                BigInteger source = new BigInteger(s, 2);

                //Flags
                setFlags(flags, destination.charAt(source.intValue()));

                storeResultToDesMemory(memory, calculator, des, destination, source);
            }
        }
    }
}

String getDestinationRegister(registers, calculator, des) {
    String d = calculator.hexToBinaryString(registers.get(des), des);
    String dest = new StringBuffer(d).reverse().toString();

    return dest;
}

String getDestinationMemory(memory, calculator, des) {
    String d = calculator.hexToBinaryString(memory.read(des, des), des);
    String dest = new StringBuffer(d).reverse().toString();

    return dest;
}

storeResultToDesRegister(registers, calculator, des, destination, i) {
    StringBuffer buffer = new StringBuffer(destination);
    buffer.setCharAt(i, '1');
    destination = buffer.reverse().toString();
    registers.set(des, calculator.binaryToHexString(destination, des));
}

storeResultToDesMemory(registers, calculator, des, destination, source) {
    StringBuffer buffer = new StringBuffer(destination);
    buffer.setCharAt(source.intValue(), '1');
    destination = buffer.reverse().toString();
    memory.write(des, calculator.binaryToHexString(destination, des), des);
}

setFlags(flags, cf) {
    flags.setCarryFlag(cf + "");

    //OF, SF, PF, AF are undefined
    flags.setOverflowFlag("0");
    flags.setSignFlag("0");
    flags.setParityFlag("0");
    flags.setAuxiliaryFlag("0");
    //Zero flag no change
}
