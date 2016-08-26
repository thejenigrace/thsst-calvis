execute(des, src, registers, memory) {
    Double[] floatValuesDes = new Double[2];
    Double[] floatValuesSrc = new Double[2];
    Double[] floatValuesResult = new Double[2];
    Calculator c = new Calculator(registers, memory);
    boolean isRegisterSrc = false;
    int desSize = 0;
    int srcSize = 0;

    if(des.isRegister()) {
        desSize = registers.getBitSize(des);
    }

    if(src.isRegister()) {
        srcSize = registers.getBitSize(src);
        isRegisterSrc = true;
    }

    else if(src.isMemory()) {
        srcSize = memory.getBitSize(src);
    }

    if(des.isRegister()) {
        String destination = registers.get(des);
        String resultingHexAdd = "";
        String source = "";
        floatValuesDes[0] = c.hexToDoublePrecisionFloatingPoint(destination.substring(0,16));
        floatValuesDes[1] = c.hexToDoublePrecisionFloatingPoint(destination.substring(16,32));
        if(isRegisterSrc) {
            source = registers.get(src);
        }

        else{
            source = memory.read(src, desSize);

        }

        floatValuesSrc[0] = c.hexToDoublePrecisionFloatingPoint(source.substring(0,16));
        floatValuesSrc[1] = c.hexToDoublePrecisionFloatingPoint(source.substring(16,32));
        for(int x = 0; x < floatValuesDes.length; x++) {
            if(floatValuesDes[x] < floatValuesSrc[x]) {
                floatValuesResult[x] = floatValuesDes[x];
            }
            else{
                floatValuesResult[x] = floatValuesSrc[x];
            }
        }

        resultingHexAdd += c.convertDoublePrecisionToHexString(floatValuesResult[1]);
        registers.set(des, destination.substring(0,16) + resultingHexAdd);
    }
}
