package thsst.calvis.configuration.model.engine;

/**
 * Created by Goodwin Chua on 16 Jun 2016.
 */
public class X87Register extends Register{

    protected char[] charArray;
    protected String defaultValues;

    public X87Register(String binary, String name, int size){
        super(name, size);
        this.charArray = new char[16];
        if ( binary.length() == 16 ) {
            this.defaultValues = binary;
            this.charArray = this.defaultValues.toCharArray();
        }
    }

    @Override
    public void initializeValue() {
        if ( this.defaultValues != null && this.defaultValues.length() == 16 ) {
            this.charArray = this.defaultValues.toCharArray();
        }
    }

    @Override
    public String getValue() {
        Integer binaryValue = Integer.parseInt(new String(this.charArray), 2);
        String hexString = Integer.toHexString(binaryValue);
        while ( hexString.length() < 4 ) {
            hexString = "0" + hexString;
        }
        return hexString.toUpperCase();
    }

    @Override
    public void setValue(String hexValue) {
        if ( hexValue.length() == 4 ) {
            // convert hex string to binary string
            Integer i = Integer.parseInt(hexValue, 16);
            String binaryString = Integer.toBinaryString(i);
            while ( binaryString.length() < 16 ) {
                binaryString = "0" + binaryString;
            }
            // store binary string to char[] value
            this.charArray = binaryString.toCharArray();
        }
    }
}
