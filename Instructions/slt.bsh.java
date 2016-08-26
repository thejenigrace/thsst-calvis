execute(res, des, src, registers, memory) throws Exception {
    String resName = res.getValue();
    String desName = des.getValue();
    String srcName = src.getValue();
    String result = registers.get(res);
    String destination = registers.get(des);
    String source = registers.get(src);
    BigInteger resBi = new BigInteger(result, 16);
    BigInteger desBi = new BigInteger(destination, 16);
    BigInteger srcBi = new BigInteger(source, 16);
    String pattern = "R.*|r.*|\\$.*";
    boolean isAccepted = (resName.matches(pattern)) || (srcName.matches(pattern)) || (desName.matches(pattern));

    if(!isAccepted) {
        throw new IncorrectParameterException("SLT");
    }

    int compareResult = desBi.compareTo(srcBi);

    if(!des.getValue().equals("R0") || !des.getValue().equals("$0")) {
        switch(compareResult) {
            case -1:
                registers.set(res, "1");
                break;
            case 0:
            case 1:
                registers.set(res, "0");
                break;
        }
    }

}
