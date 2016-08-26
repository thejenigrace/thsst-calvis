execute(des, src, rel, registers, memory) throws Exception {
    Calculator cal = new Calculator(registers, memory);
    String desName = des.getValue();
    String srcName = src.getValue();
    String destination = registers.get(des);
    String source = registers.get(src);
    BigInteger desBi = new BigInteger(destination, 16);
    BigInteger srcBi = new BigInteger(source, 16);

    String toAddress = "";
    String pattern = "R.*|r.*|\\$.*";
    boolean isAccepted = (srcName.matches(pattern)) || (desName.matches(pattern));

    if(!isAccepted) {
        throw new IncorrectParameterException("BNEZ");
    }

    if(desBi.compareTo(srcBi) != 0) {
        // default is rel 16
        if( rel.isLabel()) {
            toAddress = memory.getFromLabelMap(rel.getValue());
        }

        toAddress = Memory.reformatAddress(toAddress);
        String fromAddress = registers.getInstructionPointer();
        BigInteger from = new BigInteger(fromAddress, 16);
        BigInteger to = new BigInteger(toAddress, 16);
        BigInteger result = from.subtract(to);

        // default is rel 16
        if ( cal.isWithinBounds(result, Memory.DEFAULT_RELATIVE_SIZE) ) {
            registers.setInstructionPointer(toAddress);
        }
        else {
            throw new JumpOutOfBoundsException(from, to);
        }
    }
    else {
        /**
         * Else, incremet instruction pointer to proceed to next instruction;
         */
        String currentLine = registers.getInstructionPointer();
        BigInteger value = new BigInteger(currentLine, 16);
        value = value.add(new BigInteger("1"));
        registers.setInstructionPointer(value.toString(16));
    }
}
