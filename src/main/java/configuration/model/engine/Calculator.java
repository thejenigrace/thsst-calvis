package configuration.model.engine;

import java.math.BigInteger;

public class Calculator {

    private RegisterList registers;
    private Memory memory;

    public Calculator(RegisterList registers, Memory memory) {
        this.registers = registers;
        this.memory = memory;
    }

    public boolean evaluateCondition(String condition) {
        String con = condition.toUpperCase();
        EFlags flags = registers.getEFlags();
        String CF = flags.getCarryFlag();
        String ZF = flags.getZeroFlag();
        String OF = flags.getOverflowFlag();
        //String AF = flags.getAuxiliaryFlag();
        String PF = flags.getParityFlag();
        String SF = flags.getSignFlag();

        switch (con) {
            case "A": // fall through
            case "NBE":
                return (CF.equals("0") && ZF.equals("0"));
            case "AE":
            case "NB":
                return CF.equals("0");
            case "B":
            case "NAE":
                return CF.equals("1");
            case "BE":
            case "NA":
                return (CF.equals("1") || ZF.equals("1"));
            case "G":
            case "NLE":
                return ((SF.equals(OF)) && (ZF.equals("0")));
            case "GE":
            case "NL":
                return SF.equals(OF);
            case "L":
            case "NGE":
                return !SF.equals(OF);
            case "LE":
            case "NG":
                return ((!SF.equals(OF)) || (ZF.equals("1")));
            case "E":
            case "Z":
                return ZF.equals("1");
            case "NE":
            case "NZ":
                return ZF.equals("0");
            case "P":
            case "PE":
                return PF.equals("1");
            case "NP":
            case "PO":
                return PF.equals("0");
            case "O":
                return OF.equals("1");
            case "NO":
                return OF.equals("0");
            case "C":
                return CF.equals("1");
            case "NC":
                return CF.equals("0");
            case "S":
                return SF.equals("1");
            case "NS":
                return SF.equals("0");
            case "CXZ":
                return registers.get("CX").equals("0000");
            case "ECXZ":
                return registers.get("ECX").equals("00000000");
            case "U":
                return PF.equals("1");
            case "NU":
                return PF.equals("0");

            default:
                System.out.println("Condition not found");
                return false;
        }
    }

    public boolean checkIfInGPRegisterLow(String register){
        return register.equals("AX") || register.equals("BX") || register.equals("CX") || register.equals("DX");
    }

    public String hexToBinaryString(String value, Token des) {
        BigInteger bi = new BigInteger(value, 16);
        String val = bi.toString(2);

        if (des.isRegister()) {
            int missingZeroes = registers.getBitSize(des) - val.length();

            //zero extend
            for (int k = 0; k < missingZeroes; k++) {
                val = "0" + val;
            }
        } else if (des.isMemory()) {
            int missingZeroes = memory.getBitSize(des) - val.length();

            //zero extend
            for (int k = 0; k < missingZeroes; k++) {
                val = "0" + val;
            }
        }

        return val;
    }

    public String hexToBinaryString(String value, int count) {
        BigInteger bi = new BigInteger(value, 16);
        String val = bi.toString(2);
        int missingZeroes = count - val.length();
        //zero extend
        for (int k = 0; k < missingZeroes; k++) {
            val = "0" + val;
        }

        return val;
    }

    public String binaryToHexString(String value, Token des) {
        BigInteger bi = new BigInteger(value, 2);
        String val = bi.toString(16);

        if (des.isRegister()) {
            int registerSize = registers.getHexSize(des);
            int missingZeroes = registerSize - val.length();

            //zero extend
            for (int k = 0; k < missingZeroes; k++) {
                val = "0" + val;
            }

            //remove carry flag
            if (val.length() > registerSize) {
                StringBuilder sb = new StringBuilder();

                for (int i = 1; i < val.length(); i++) {
                    sb.append(val.charAt(i));
                }

                val = sb.toString();
            }
        } else if (des.isMemory()) {
            int missingZeroes = memory.getHexSize(des) - val.length();

            //zero extend
            for (int k = 0; k < missingZeroes; k++) {
                val = "0" + val;
            }
        }

        return val;
    }

    public String computeAveragePackedHex(String destination, String source, int count){
        String convertedString = "";
        String desBit = "";
        String srcBit = "";
        BigInteger des = new BigInteger(destination, 16);
        BigInteger src = new BigInteger(source, 16);
        srcBit = binaryZeroExtend(src.toString(2), 128);
        desBit = binaryZeroExtend(des.toString(2), 128);

        int a = 0;
        BigInteger addPlusOne = BigInteger.valueOf(new Integer(1).intValue());
        for(int x = 0; x < 16; x++){
            BigInteger desBitBigInt = new BigInteger(desBit.substring(a, a + 8), 2);
            BigInteger srcBitBigInt = new BigInteger(srcBit.substring(a, a + 8), 2);
            String miniResult = binaryZeroExtend(new BigInteger(binaryZeroExtend(desBitBigInt.add(srcBitBigInt).add(addPlusOne).toString(2), 8), 2).shiftRight(1).toString(2), 8);
            a += count;

            convertedString += binaryZeroExtend(new BigInteger(miniResult, 2).toString(16), count/4);
        }
        return convertedString.toUpperCase();
    }

    public long convertToSignedInteger(BigInteger hexValue, int bitSize) {
        System.out.println("--Convert to Signed Integer--");
        long result = Long.parseLong(hexValue.toString());
        String twosComplement = hexValue.toString(2);

        int missingZeroes = bitSize - twosComplement.length();

        System.out.println("bitSize = " + bitSize);
        System.out.println("original hex = " + hexValue.toString(16));
        System.out.println("original decimal = " + hexValue.toString());
        System.out.println("original twosComplement = " + twosComplement);

        // zero extend
        for (int k = 0; k < missingZeroes; k++) {
            twosComplement = "0" + twosComplement;
        }

        System.out.println("twosComplement = " + twosComplement);

        // Negative Two's Complement
        if (twosComplement.charAt(0) == '1') {
            BigInteger biOnesComplement = hexValue.subtract(BigInteger.ONE);

            String onesComplement = biOnesComplement.toString(2);
            System.out.println("onesComplement = " + onesComplement);

            missingZeroes = bitSize - onesComplement.length();
            // zero extend
            for (int k = 0; k < missingZeroes; k++) {
                onesComplement = "0" + onesComplement;
            }

            System.out.println("zero-extend onesComplement = " + onesComplement);

            // Convert 1's Complement to Normal Binary
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < onesComplement.length(); i++) {
                if (onesComplement.charAt(i) == '1') {
                    sb.append("0");
                } else if (onesComplement.charAt(i) == '0') {
                    sb.append("1");
                }
            }

            System.out.println("decimalBinaryF = " + sb.toString());

            BigInteger decimal = new BigInteger(sb.toString(), 2);
            System.out.println("decimal = " + decimal.toString());

            String signedDecimal = "-" + decimal.toString();
            System.out.println("signedDecimal = " + Long.parseLong(signedDecimal));

            result = Long.parseLong(signedDecimal);
        }

        System.out.println("result = " + result);

        return result;
    }


    public String cutToCertainHexSize(String type, String value, int size) {
        String newValue = "";
        int missingZeroes = size - value.length();

        // zero extend
        for (int i = 0; i < missingZeroes; i++) {
            value = "0" + value;
        }

        if (type.equals("getUpper")) {
            System.out.println("getUpper");
            newValue = value.substring(0, size);
        } else if (type.equals("getLower")) {
            System.out.println("getLower");
            newValue = value.substring(value.length()-size, value.length());
        }

        System.out.println("--Cut To Certain Hex Size--");
        System.out.println("hex size = " + size);
        System.out.println("oldValue = " + value);
        System.out.println("newValue = " + newValue);

        return newValue;
    }

    public String[] cutToCertainSize(String value, int size) {
        BigInteger bi = new BigInteger(value, 16);
        String val = bi.toString(16);
        System.out.println("cut.size = " + size);

        int missingZeroes = size * 2 - val.length();
        //zero extend
        for (int k = 0; k < missingZeroes; k++) {
            val = "0" + val;
        }

        System.out.println("cut.value = " + val);

        StringBuilder sb0 = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        String[] result = new String[2];
        for (int i = 0; i < val.length(); i++) {
            if (i < size) {
                sb0.append(val.charAt(i));
            } else {
                sb1.append(val.charAt(i));
            }

            if (i - size - 1 == 0) {
                System.out.println("Initialize result[0] = " + sb0.toString());
                result[0] = sb0.toString();
            }

            if (i - size == size - 1) {
                System.out.println("Initialize result[1] = " + sb1.toString());
                result[1] = sb1.toString();
            }
        }

        return result;
    }

    public String hexZeroExtend(String value, Token des) {
        if (des.isRegister()) {
            int missingZeroes = registers.getHexSize(des) - value.length();

            //zero extend
            if (missingZeroes > 0) {
                for (int k = 0; k < missingZeroes; k++) {
                    value = "0" + value;
                }
            }

            if (value.length() > registers.getHexSize(des)) {
                value = value.substring(1);
            }
        } else if (des.isMemory()) {
            int missingZeroes = memory.getHexSize(des) - value.length();

            //zero extend
            if (missingZeroes > 0) {
                for (int k = 0; k < missingZeroes; k++) {
                    value = "0" + value;
                }
            }

            if (value.length() > memory.getHexSize(des)) {
                value = value.substring(1);
            }
        }

        return value;
    }

    public String hexZeroExtend(String value, int size) {

        int missingZeroes = size - value.length();

        //zero extend
        while (missingZeroes > 0) {
            value = "0" + value;

            missingZeroes--;
        }

        return value;
    }


    public String binaryZeroExtend(String value, Token des) {
        if (des.isRegister()) {
            int missingZeroes = registers.getBitSize(des) - value.length();

            //zero extend
            if (missingZeroes > 0) {
                for (int k = 0; k < missingZeroes; k++) {
                    value = "0" + value;
                }
            }

            if (value.length() > registers.getBitSize(des)) {
                value = value.substring(1);
            }
        } else if (des.isMemory()) {
            int missingZeroes = memory.getBitSize(des) - value.length();

            //zero extend
            if (missingZeroes > 0) {
                for (int k = 0; k < missingZeroes; k++) {
                    value = "0" + value;
                }
            }

            if (value.length() > memory.getBitSize(des)) {
                value = value.substring(1);
            }
        }

        return value;
    }

    public String binarySignExtend(String value, Token des) {
        if (des.isRegister()) {
            int missingZeroes = registers.getBitSize(des) - value.length();
            String sign = value.charAt(0) + "";

            //sign extend
            if (missingZeroes > 0) {
                for (int k = 0; k < missingZeroes; k++) {
                    value = sign + value;
                }
            }

            if (value.length() > registers.getBitSize(des)) {
                value = value.substring(1);
            }
        } else if (des.isMemory()) {
            int missingZeroes = memory.getBitSize(des) - value.length();
            String sign = value.charAt(0) + "";

            //sign extend
            if (missingZeroes > 0) {
                for (int k = 0; k < missingZeroes; k++) {
                    value = sign + value;
                }
            }

            if (value.length() > memory.getBitSize(des)) {
                value = value.substring(1);
            }
        }

        return value;
    }

    public String checkAuxiliary(String a, String b) {
        String sx = "" + a.charAt(a.length() - 1);
        String sy = "" + b.charAt(b.length() - 1);
        BigInteger x = new BigInteger(sx, 16);
        BigInteger y = new BigInteger(sy, 16);
        BigInteger result = y.add(x);

        if (result.toString(2).length() > 4) {
            return "1";
        }

        return "0";
    }

    public String checkAuxiliarySub(String src, String des) {
        String s = new StringBuffer(src).reverse().toString();
        String d = new StringBuffer(des).reverse().toString();

        int r = 0;
        int borrow = 0;

        for (int i = 0; i < d.length(); i++) {
            r = Integer.parseInt(String.valueOf(d.charAt(i)))
                    - Integer.parseInt(String.valueOf(s.charAt(i)))
                    - borrow;

            if (r < 0) {
                borrow = 1;

                if (i == 3) {
                    return "1";
                }
            } else {
                borrow = 0;

                if (i > 3) {
                    break;
                }
            }
        }
        return "0";
    }

    public String checkParity(String value) {
        String parity = new StringBuffer(value).reverse().toString();
        int count = 0;

        if (parity.length() < 8) {
            int missingZeroes = 8 - parity.length();

            for (int k = 0; k < missingZeroes; k++) {
                parity = parity + "0";
            }
        }

        for (int i = 0; i < 8; i++) {
            if (parity.charAt(i) == '1') {
                count++;
            }
        }

        if (count % 2 == 0 && count != 0) {
            return "1";
        }
        return "0";
    }

    public boolean isWithinBounds(BigInteger difference, int size) {
        boolean flag = true;
        BigInteger startBoundary = new BigInteger("0");

        switch (size) {
            case 8: // rel8
                startBoundary = new BigInteger("128");
                break;
            case 16: // rel16 32768
                startBoundary = new BigInteger("32768");
                break;
        }

        BigInteger endBoundary = new BigInteger("1");
        endBoundary = startBoundary.subtract(endBoundary).negate();

        int start = difference.compareTo(startBoundary);
        int end = difference.compareTo(endBoundary);
        flag = (start == -1 || start == 0) && (end == 1 || end == 0);

        return flag;
    }

    public String checkOverflowAdd(char src, char des, char res) {
        if ((src == '0' && des == '0' && res == '1')
                || (src == '1' && des == '1' && res == '0')) {
            return "1";
        }
        return "0";
    }

    public String checkOverflowAddWithFlag(char src, char des, char res, String flag) {
        if ((src == '0' && des == '0' && res == '1' && flag.charAt(0) == '1')
                || (src == '1' && des == '1' && res == '0' && flag.charAt(0) == '1')) {
            return "1";
        }
        return "0";
    }

    public String flipAllBits(String bInt) {
        StringBuilder bits = new StringBuilder(bInt);
        for (int x = 0; x < bInt.length(); x++) {
            if (bInt.charAt(x) == '0') {
                bits.setCharAt(x, '0');
            } else {
                bits.setCharAt(x, '1');
            }
        }
        return bits.toString();
    }

    public String binaryZeroExtend(String value, int count) {
        if (value.length() > count)
            return value.substring(1);
        else {
            int y = count - value.length();
            for (int x = 0; x < y; x++) {
                value = "0" + value;
            }
        }
        return value;
    }
    //temporary something
    public Float[] HexToSinglePrecisionFloatingPoint(String value) {
        Float[] floatingValues = new Float[4];
        int a = 0;
        for(int x = 0; x < 4; x++) {
            String doubleWordHex = value.substring(a, a + 8);
            floatingValues[x] = HexToSinglePrecisionFloatingPointSingle(doubleWordHex);
            a += 8;
        }
        return floatingValues;
    }

    public Float HexToSinglePrecisionFloatingPointSingle(String value) {

        int a = 0;
        Long i = Long.parseLong(value, 16);
        Float f = Float.intBitsToFloat(i.intValue());
        System.out.println(f);
        return f;
    }

    public String SinglePrecisionFloatingPointToHexSingle(Float floatValues) {
        return Integer.toHexString(Float.floatToIntBits(floatValues)).toUpperCase();
    }

    public String SinglePrecisionFloatingPointToHex(Float[] floatValues){

        String hexConverted = "";
        for(int x = 0; x < floatValues.length; x++) {
//            System.out.println("result:" + floatValues[x]);
            hexConverted += SinglePrecisionFloatingPointToHexSingle(floatValues[x]);
        }
        return hexConverted;
    }
//    public String concatHex

    public String cutBySizeAndCompare(String desValue, String srcValue, int hexSize, int cutSize, char operation) {
        int desMissingZeroes = hexSize - desValue.length();
        int srcMissingZeroes = hexSize - srcValue.length();
        // zero extend
        for (int i = 0; i < desMissingZeroes; i++) {
            desValue = "0" + desValue;
        }
        for (int i = 0; i < srcMissingZeroes; i++) {
            srcValue = "0" + srcValue;
        }

        String[] arrDes = new String[hexSize / cutSize];
        String[] arrSrc = new String[hexSize / cutSize];

        int index = 0;
        for (int i = 0; i < hexSize; i += cutSize) {

            if (cutSize == 2) {
                arrDes[index] = desValue.substring(i, i + 2);
                arrSrc[index] = srcValue.substring(i, i + 2);
            } else if (cutSize == 4) {
                arrDes[index] = desValue.substring(i, i + 4);
                arrSrc[index] = srcValue.substring(i, i + 4);
            } else if (cutSize == 8) {
                arrDes[index] = desValue.substring(i, i + 8);
                arrSrc[index] = srcValue.substring(i, i + 8);
            }
            index++;
//            System.out.println("Created an Array " + i);
        }

        StringBuilder sbResult = new StringBuilder();
        for (int i = 0; i < hexSize / cutSize; i++) {
            System.out.println("arrDes[" + i + "] = " + arrDes[i]);
            System.out.println("arrSrc[" + i + "] = " + arrSrc[i]);

            if (arrDes[i].equals(arrSrc[i]) && cutSize == 2 && operation == 'e')
                sbResult.append("FF");
            else if (!arrDes[1].equals(arrSrc[i]) && cutSize == 2 && operation == 'e')
                sbResult.append("00");

            if (arrDes[i].equals(arrSrc[i]) && cutSize == 4 && operation == 'e')
                sbResult.append("FFFF");
            else if (!arrDes[1].equals(arrSrc[i]) && cutSize == 4 && operation == 'e')
                sbResult.append("0000");

            if (arrDes[i].equals(arrSrc[i]) && cutSize == 8 && operation == 'e')
                sbResult.append("FFFFFFFF");
            else if (!arrDes[1].equals(arrSrc[i]) && cutSize == 8 && operation == 'e')
                sbResult.append("00000000");

            if (Long.compare(Long.parseLong(arrDes[i], 16), Long.parseLong(arrSrc[i], 16)) > 0 && cutSize == 2 && operation == 'g')
                sbResult.append("FF");
            else if (Long.compare(Long.parseLong(arrDes[i], 16), Long.parseLong(arrSrc[i], 16)) <= 0 && cutSize == 2 && operation == 'g')
                sbResult.append("00");

            if (Long.compare(Long.parseLong(arrDes[i], 16), Long.parseLong(arrSrc[i], 16)) > 0 && cutSize == 4 && operation == 'g')
                sbResult.append("FFFF");
            else if (Long.compare(Long.parseLong(arrDes[i], 16), Long.parseLong(arrSrc[i], 16)) <= 0 && cutSize == 4 && operation == 'g')
                sbResult.append("0000");

            if (Long.compare(Long.parseLong(arrDes[i], 16), Long.parseLong(arrSrc[i], 16)) > 0 && cutSize == 8 && operation == 'g')
                sbResult.append("FFFFFFFF");
            else if (Long.compare(Long.parseLong(arrDes[i], 16), Long.parseLong(arrSrc[i], 16)) <= 0 && cutSize == 8 && operation == 'g')
                sbResult.append("00000000");
        }

        System.out.println("hexSize = " + hexSize);
        System.out.println("desValue = " + desValue);
        System.out.println("srcValue = " + srcValue);
        System.out.println("resValue = " + sbResult.toString());
        return sbResult.toString();
    }


//    public float hexToSinglePrecisionFloatingPoint(String hexValue) {
//        Long l = Long.parseLong(hexValue, 16);
//
//        System.out.println("l.intValue() = " + l.intValue());
//        Float f = Float.intBitsToFloat(l.intValue());
//        System.out.println("hexValue = " + Integer.toHexString(Float.floatToIntBits(f)));
//        System.out.println("float = " + f);
//
//        return f;
//    }

    public String toHexSinglePrecisionString(long l) {
        Long signedLong = l;
        Float sp = signedLong.floatValue();

        System.out.println("signedLong = " + signedLong);
        System.out.println("sp = " + sp);
        System.out.println("result hex = " + convertSinglePrecisionToHexString(sp));

        return this.convertSinglePrecisionToHexString(sp);
    }

    public String toHexDoublePrecisionString(long l) {
        Long signedLong = l;
        Double dp = signedLong.doubleValue();

        System.out.println("signedLong = " + signedLong);
        System.out.println("dp = " + dp);
        System.out.println("result hex = " + convertDoublePrecisionToHexString(dp));

        return this.convertDoublePrecisionToHexString(dp);
    }

    private long parseUnsignedHex(String text) {
        if (text.length() == 16) {
            return (parseUnsignedHex(text.substring(0, 1)) << 60)
                    | parseUnsignedHex(text.substring(1));
        }
//        else if (text.length() == 8) {
//            return  (parseUnsignedHex(text.substring(0,1)) << 28)
//                    | parseUnsignedHex(text.substring(1));
//        }

        return Long.parseLong(text, 16);
    }

    public float convertHexToSinglePrecision(String hexValue) {
        Long l = Long.parseLong(hexValue, 16);
        Float f = Float.intBitsToFloat(l.intValue());

        return f;
    }

    public double convertHexToDoublePrecision(String hexValue) {
        Long l = parseUnsignedHex(hexValue);
        Double d = Double.longBitsToDouble(l);

        return d;
    }


//    public double hexToDoublePrecisionFloatingPoint(String hexValue) {
//        Long l = Long.parseLong(hexValue, 16);
//        Double d = Double.longBitsToDouble(l);
//        System.out.println("hexValue = " + Long.toHexString(Double.doubleToLongBits(d)));
//        System.out.println("double = " + d);
//
//        return d;
//    }

    public String convertSinglePrecisionToHexString(Float fValue) {
        return Integer.toHexString(Float.floatToIntBits(fValue));
    }

    public String convertDoublePrecisionToHexString(Double dValue) {
        return Long.toHexString(Double.doubleToLongBits(dValue));
    }

    public String convertSPToDP(String hexValue) {
        Float sp = convertHexToSinglePrecision(hexValue);
        Double dp = sp.doubleValue();

        System.out.println("hexValue = " + hexValue);
        System.out.println("result hex = " + convertDoublePrecisionToHexString(dp));

        return convertDoublePrecisionToHexString(dp);
    }

    public String convertDPToSP(String hexValue) {
        Double dp = convertHexToDoublePrecision(hexValue);
        Float sp = dp.floatValue();

        System.out.println("hexValue = " + hexValue);
        System.out.println("result hex = " + convertSinglePrecisionToHexString(sp));

        return convertSinglePrecisionToHexString(sp);
    }
//
    public String convertHexSinglePrecisionToHexInteger(String hexValue) {
        Float sp = convertHexToSinglePrecision(hexValue);
        Integer i = Float.floatToIntBits(sp);

        System.out.println("hexValue = " + hexValue);
        System.out.println("sp = " + sp);
        System.out.println("int = " + i);
        System.out.println("hex result = " + Integer.toHexString(i));

        return Integer.toHexString(i);
    }

    public String convertHexDoublePrecisionToHexInteger(String hexValue) {
        Double dp = convertHexToDoublePrecision(hexValue);
        Long l = Double.doubleToLongBits(dp);

        System.out.println("hexValue = " + hexValue);
        System.out.println("dp = " + dp);
        System.out.println("int = " + l);
        System.out.println("hex result = " + Long.toHexString(l));

        return Long.toHexString(l);
    }
}
