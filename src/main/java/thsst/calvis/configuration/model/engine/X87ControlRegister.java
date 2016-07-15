package thsst.calvis.configuration.model.engine;

/**
 * Created by Goodwin Chua on 25/03/2016.
 */
public class X87ControlRegister extends X87Register {

    public X87ControlRegister() {
        super("0000001101111111", "CONTROL", 16);
    }

    public int getRoundingMode() {
        return Integer.parseInt(getRoundingControl(), 2);
    }

    private String getRoundingControl() {
        String binaryTop = new String(new char[]{charArray[4], charArray[5]});
        return binaryTop;
    }

    private void setRoundingControl(int number) {
        String binaryNumber = Integer.toBinaryString(number);

        while ( binaryNumber.length() < 2 ) {
            binaryNumber = "0" + binaryNumber;
        }

        charArray[4] = binaryNumber.charAt(0);
        charArray[5] = binaryNumber.charAt(1);
    }

}
