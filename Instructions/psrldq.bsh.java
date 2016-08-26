execute(des, src, registers, memory) {
    Calculator c = new Calculator(registers, memory);
    boolean isRegisterSrc = false;
    boolean isRegisterDes = false;
    int desSize = 0;
    int counter = 0;
    String destination = "";
    String source = "";
    String resultingHex = "";


    if(des.isRegister()) {
        desSize = registers.getBitSize(des);
        isRegisterDes = true;
        destination = registers.get(des);

    }


    if(src.isHex()) {
        counter = new BigInteger(src.getValue(), 16).intValue();
    }

    if(desSize == 128) {
        resultingHex = destination;
        for(int x = 0; x < (counter); x++) {
            StringBuilder sb = new StringBuilder(resultingHex);
            String temp = resultingHex.substring(0, 30);

            temp = "00" + temp;
            resultingHex = temp;
        }

        registers.set(des, resultingHex);
    }
}
