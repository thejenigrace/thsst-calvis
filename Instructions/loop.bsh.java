execute(rel, registers, memory) throws Exception {
    Calculator cal = new Calculator(registers, memory);

    String c = "";
    switch(Memory.MAX_ADDRESS_SIZE) {
    case 32:
        c = registers.get("ECX");
        Token token = new Token(Token.REG, "ECX");
        break;
    default:
        c = registers.get("CX");
        Token token = new Token(Token.REG, "CX");
        break;
    }

    BigInteger count = new BigInteger(c, 16);
    count = count.subtract(BigInteger.ONE);

    if ( count.intValue() == -1 ) {
        registers.set(token, cal.binaryToHexString(cal.binarySignExtend("1", token), token));
    }
    else {
        registers.set(token, cal.hexZeroExtend(count.toString(16), token));
    }

    if( count.intValue() != 0 ) {
        String toAddress = "";
        if ( rel.isLabel() ) {
            toAddress = memory.getFromLabelMap(rel.getValue());
        }

        toAddress = Memory.reformatAddress(toAddress);
        String fromAddress = registers.getInstructionPointer();
        BigInteger from = new BigInteger(fromAddress, 16);
        BigInteger to = new BigInteger(toAddress, 16);
        BigInteger result = from.subtract(to);

        if ( cal.isWithinBounds(result, 8) ) {
            registers.setInstructionPointer(toAddress);
        }
        else {
            throw new JumpOutOfBoundsException(from, to);
        }
    }
    else {
        String currentLine = registers.getInstructionPointer();
        BigInteger value = new BigInteger(currentLine, 16);
        value = value.add(new BigInteger("1"));
        registers.setInstructionPointer(value.toString(16));
    }
}
