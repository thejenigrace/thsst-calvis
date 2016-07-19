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

}
