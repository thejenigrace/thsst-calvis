package configuration.model.engine;

/**
 * Created by Goodwin Chua on 25/03/2016.
 */
public class X87TagRegister extends X87Register {

    public static final String VALID = "00";
    public static final String ZERO = "01";
    public static final String SPECIAL = "10"; // NaN, unsupported, infinity or denormal
    public static final String EMPTY = "11";

    public X87TagRegister() {
        super("1111111111111111", "TAG", 16);
    }

    public String getTag(String tagNumber) {
        String tagWord = "";
        switch ( tagNumber ) {
            case "0":
                tagWord = new String(new char[]{charArray[14], charArray[15]});
                break;
            case "1":
                tagWord = new String(new char[]{charArray[12], charArray[13]});
                break;
            case "2":
                tagWord = new String(new char[]{charArray[10], charArray[11]});
                break;
            case "3":
                tagWord = new String(new char[]{charArray[8], charArray[9]});
                break;
            case "4":
                tagWord = new String(new char[]{charArray[6], charArray[7]});
                break;
            case "5":
                tagWord = new String(new char[]{charArray[4], charArray[5]});
                break;
            case "6":
                tagWord = new String(new char[]{charArray[2], charArray[3]});
                break;
            case "7":
                tagWord = new String(new char[]{charArray[0], charArray[1]});
                break;
        }
        return tagWord;
    }

    public void setTag(String tagNumber, String charArray) {
        switch ( tagNumber ) {
            case "0":
                this.charArray[14] = charArray.charAt(0);
                this.charArray[15] = charArray.charAt(1);
                break;
            case "1":
                this.charArray[12] = charArray.charAt(0);
                this.charArray[13] = charArray.charAt(1);
                break;
            case "2":
                this.charArray[10] = charArray.charAt(0);
                this.charArray[11] = charArray.charAt(1);
                break;
            case "3":
                this.charArray[8] = charArray.charAt(0);
                this.charArray[9] = charArray.charAt(1);
                break;
            case "4":
                this.charArray[6] = charArray.charAt(0);
                this.charArray[7] = charArray.charAt(1);
                break;
            case "5":
                this.charArray[4] = charArray.charAt(0);
                this.charArray[5] = charArray.charAt(1);
                break;
            case "6":
                this.charArray[2] = charArray.charAt(0);
                this.charArray[3] = charArray.charAt(1);
                break;
            case "7":
                this.charArray[0] = charArray.charAt(0);
                this.charArray[1] = charArray.charAt(1);
                break;
            default:
        }
    }
}
