execute(des, src, registers, memory) {
    Float[] floatValuesDes = new Float[4];
    Float floatValuesSrc = 0;
    Float[] floatValuesResult = new Float[4];
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

        floatValuesDes = c.HexToSinglePrecisionFloatingPoint(destination);

        if(isRegisterSrc) {
            source = registers.get(src);
        }

        else if(!isRegisterSrc) {
            source = memory.read(src, 128);
        }

        floatValuesSrc = c.HexToSinglePrecisionFloatingPointSingle(source.substring(24,32));

        for(int x = 0; x < 3; x++) {
            floatValuesResult[x] = (floatValuesDes[x]);
        }

        floatValuesResult[3] = (floatValuesDes[3] * floatValuesSrc);
        resultingHexAdd = c.hexZeroExtend(c.SinglePrecisionFloatingPointToHexSingle(floatValuesResult[0]), 8) + c.hexZeroExtend(c.SinglePrecisionFloatingPointToHexSingle(floatValuesResult[1]), 8) +  c.hexZeroExtend(c.SinglePrecisionFloatingPointToHexSingle(floatValuesResult[2]), 8) + c.hexZeroExtend(c.SinglePrecisionFloatingPointToHexSingle(floatValuesResult[3]), 8);
        registers.set(des, resultingHexAdd);
    }
}
