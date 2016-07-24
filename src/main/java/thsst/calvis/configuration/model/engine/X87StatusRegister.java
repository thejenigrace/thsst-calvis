package thsst.calvis.configuration.model.engine;

/**
 * Created by Goodwin Chua on 25/03/2016.
 */
public class X87StatusRegister extends X87Register {

    public X87StatusRegister() {
        super("0000000000000000", "STATUS", 16);
    }

    public int getTop() {
        return Integer.parseInt(getBinaryTop(), 2);
    }

    public void set(String flag, char val) {
        int index = 0;
        switch ( flag ) {
            case "C0":
                index = 7;
                break;
            case "C1":
                index = 6;
                break;
            case "C2":
                index = 5;
                break;
            case "C3":
                index = 1;
                break;
            default:
        }
        charArray[index] = val;
    }

    public void clearExceptions() {
        this.charArray[0] = '0';
        for ( int i = 7; i < 16; i++ ) {
            this.charArray[i] = '0';
        }
    }

    private String getBinaryTop() {
        String binaryTop = new String(new char[]{charArray[2], charArray[3], charArray[4]});
        return binaryTop;
    }

    public void setBinaryTop(int number) {
        String binaryNumber = Integer.toBinaryString(number);

        while ( binaryNumber.length() < 3 ) {
            binaryNumber = "0" + binaryNumber;
        }

        charArray[2] = binaryNumber.charAt(0);
        charArray[3] = binaryNumber.charAt(1);
        charArray[4] = binaryNumber.charAt(2);
    }

    public void setStackFaultFlag() {
        charArray[9] = '1';
    }

    public String getStackFaultFlag() {
        return charArray[9] + "";
    }

    public void setInvalidOperationFlag() {
        charArray[15] = '1';
    }

    public char getFlag(String name){
        int index = 0;
        switch ( name ) {
            case "C0":
                index = 7;
                break;
            case "C1":
                index = 6;
                break;
            case "C2":
                index = 5;
                break;
            case "C3":
                index = 1;
                break;
            default:
        }
        return charArray[index];
    }
    public String getInvalidOperationFlag() {
        return charArray[15] + "";
    }

}
