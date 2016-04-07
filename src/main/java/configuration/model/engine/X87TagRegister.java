package configuration.model.engine;

/**
 * Created by Goodwin Chua on 25/03/2016.
 */
public class X87TagRegister {

    private char[] value;

    public static final String VALID = "00";
    public static final String ZERO = "01";
    public static final String SPECIAL = "10"; // NaN, unsupported, infinity or denormal
    public static final String EMPTY = "11";


    public X87TagRegister() {
        this.value = new char[16];
        clear();
    }

    public void clear() {
        String binary = "1111111111111111";
        if ( binary.length() == 16 ) {
            this.value = binary.toCharArray();
        }
    }

    public String getTag(String tagNumber) {
        String tagWord = "";
        switch ( tagNumber ) {
            case "0":
                tagWord = new String(new char[]{value[14], value[15]});
                break;
            case "1":
                tagWord = new String(new char[]{value[12], value[13]});
                break;
            case "2":
                tagWord = new String(new char[]{value[10], value[11]});
                break;
            case "3":
                tagWord = new String(new char[]{value[8], value[9]});
                break;
            case "4":
                tagWord = new String(new char[]{value[6], value[7]});
                break;
            case "5":
                tagWord = new String(new char[]{value[4], value[5]});
                break;
            case "6":
                tagWord = new String(new char[]{value[2], value[3]});
                break;
            case "7":
                tagWord = new String(new char[]{value[0], value[1]});
                break;
        }
        return tagWord;
    }

    public void setTag(String tagNumber, String value) {
        switch ( tagNumber ) {
            case "0":
                this.value[14] = value.charAt(0);
                this.value[15] = value.charAt(1);
                break;
            case "1":
                this.value[12] = value.charAt(0);
                this.value[13] = value.charAt(1);
                break;
            case "2":
                this.value[10] = value.charAt(0);
                this.value[11] = value.charAt(1);
                break;
            case "3":
                this.value[8] = value.charAt(0);
                this.value[9] = value.charAt(1);
                break;
            case "4":
                this.value[6] = value.charAt(0);
                this.value[7] = value.charAt(1);
                break;
            case "5":
                this.value[4] = value.charAt(0);
                this.value[5] = value.charAt(1);
                break;
            case "6":
                this.value[2] = value.charAt(0);
                this.value[3] = value.charAt(1);
                break;
            case "7":
                this.value[0] = value.charAt(0);
                this.value[1] = value.charAt(1);
                break;
            default:
        }
    }

    public String toHexString() {
        Integer binaryValue = Integer.parseInt(new String(this.value), 2);
        String hexString = Integer.toHexString(binaryValue);
        while ( hexString.length() < 4 ) {
            hexString = "0" + hexString;
        }
        return hexString;
    }

    public void setValue(String hexValue) {
        if ( hexValue.length() == 4 ) {
            // convert hex string to binary string
            Integer i = Integer.parseInt(hexValue, 16);
            String binaryString = Integer.toBinaryString(i);
            while ( binaryString.length() < 16 ) {
                binaryString = "0" + binaryString;
            }
            // store binary string to char[] value
            this.value = binaryString.toCharArray();
        }
    }

}
