package thsst.calvis.configuration.model.engine;

import java.math.BigInteger;

/**
 * Created by Goodwin Chua on 18 Jul 2016.
 */
public class Converter {

    private String value;

    public Converter(String value) {
        this.value = value;
    }

    private long parseUnsignedHex(String text) {
        if ( text.length() == 16 ) {
            return (parseUnsignedHex(text.substring(0, 1)) << 60)
                    | parseUnsignedHex(text.substring(1));
        }
        return Long.parseLong(text, 16);
    }

    private String getUnsigned(int size, String stringBits){
        String temp = stringBits;
        // zero extend
        while ( temp.length() < size ) {
            temp = "0" + temp;
        }

        StringBuilder tempBit = new StringBuilder(temp);
        String returnable = "";

        if (tempBit.charAt(0) == '1') {
            tempBit.setCharAt(0, '0');
            tempBit.insert(1, "1");
            BigInteger bi = new BigInteger(tempBit.toString(), 2);
            returnable = bi.toString(10);
        } else {
            BigInteger bi = new BigInteger(tempBit.toString(), 2);
            returnable = bi.toString(10);
        }
        return returnable;
    }

    private String zeroExtendHex(String value, int bitLength) {
        String result = value;
        while ( result.length() != bitLength / 4  ) {
            result = "0" + result;
        }
        return result;
    }

    public float toSinglePrecision() {
        BigInteger s = new BigInteger(value, 16);
        int hex = s.intValue();
        float f = Float.intBitsToFloat(hex);
        return f;
    }

    public String toSinglePrecisionHex() {
        Float singlePrecision = Float.parseFloat(value);
        if ( !value.equals("0.0") && String.valueOf(singlePrecision).equals("0.0") ) {
            throw new NumberFormatException(value + " cannot be parsed to IEEE Single Precision");
        }
        String value = Integer.toHexString(Float.floatToIntBits(singlePrecision));
        return value;
    }

    public double toDoublePrecision() {
        long longHex = parseUnsignedHex(value);
        double d = Double.longBitsToDouble(longHex);
        return d;
    }

    public String toDoublePrecisionHex() {
        Double doublePrecision = Double.parseDouble(value);
        if ( !value.equals("0.0") && String.valueOf(doublePrecision).equals("0.0") ) {
            throw new NumberFormatException(value + " cannot be parsed to IEEE Double Precision");
        }
        String value = Long.toHexString(Double.doubleToLongBits(doublePrecision));
        return value;
    }

    public short to16BitSignedInteger() {
        short s = (short) Integer.parseInt(value, 16);
        return s;
    }

    public String to16BitSignedIntegerHex() {
        Short realShort = Short.parseShort(value);
        String shortToHex = Integer.toHexString(realShort.intValue());
        shortToHex = zeroExtendHex(shortToHex, 16);
        return shortToHex;
    }

    public int to32BitSignedInteger() {
        BigInteger s = new BigInteger(value, 16);
        return s.intValue();
    }

    public String to32BitSignedIntegerHex() {
        BigInteger realInt = new BigInteger(value);
        String intToHex = Integer.toHexString(realInt.intValue());
        intToHex = zeroExtendHex(intToHex, 32);
        return intToHex;
    }

    public int to16BitUnsignedInteger() {
        BigInteger b = new BigInteger(value, 16);
        String unsigned = getUnsigned(16, b.toString(2));
        BigInteger b2 = new BigInteger(unsigned);
        return b2.intValueExact();
    }

    public String to16BitUnsignedIntegerHex() {
        BigInteger b = new BigInteger(value);
        String unsigned = getUnsigned(16, b.toString(2));
        BigInteger b2 = new BigInteger(unsigned);
        if ( b2.compareTo(new BigInteger("65535")) == 1 || b2.signum() == -1 ) {
            throw new NumberFormatException("Value out of range for 16 bit Unsigned Integer value:" + value);
        }
        String unsigned16 = Integer.toHexString(b2.intValue());
        unsigned16 = zeroExtendHex(unsigned16, 16);
        return unsigned16;
    }

    public long to32BitUnsignedInteger() {
        BigInteger b = new BigInteger(value, 16);
        String unsigned = getUnsigned(32, b.toString(2));
        BigInteger b2 = new BigInteger(unsigned);
        return b2.longValue();
    }

    public String to32BitUnsignedIntegerHex() {
        BigInteger b = new BigInteger(value);
        String unsigned = getUnsigned(32, b.toString(2));
        BigInteger b2 = new BigInteger(unsigned);
        if ( b2.compareTo(new BigInteger("4,294,967,295")) == 1 || b2.signum() == -1 ) {
            throw new NumberFormatException("Value out of range for 32 bit Unsigned Integer value:" + value);
        }
        String unsigned32 = Long.toHexString(b2.longValue());
        unsigned32 = zeroExtendHex(unsigned32, 32);
        return unsigned32;
    }

    public long to64BitSignedInteger() {
        BigInteger b = new BigInteger(value, 16);
        return b.longValue();
    }

    public String to64BitSignedIntegerHex() {
        BigInteger realInt = new BigInteger(value);
        String intToHex = Long.toHexString(realInt.longValue());
        intToHex = zeroExtendHex(intToHex, 64);
        return intToHex;
    }

    public String to64BitUnsignedInteger() {
        Long l = Long.parseUnsignedLong(value, 16);
        return Long.toUnsignedString(l);
    }

    public String toBinaryString() {
        String result = "";
        for ( int i = 0; i < value.length(); i++ ){
            String oneHex = value.charAt(i) + "";
            oneHex = Integer.toBinaryString(Integer.parseInt(oneHex, 16));
            while ( oneHex.length() < 4 ) {
                oneHex = "0" + oneHex;
            }
            result = result + oneHex;
        }
        return result;
    }

}
