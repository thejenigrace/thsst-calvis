package configuration.model.engine;

/**
 * Created by Goodwin Chua on 25/03/2016.
 */
public class X87StatusRegister {

    private char[] value;

    public X87StatusRegister() {
        this.value = new char[16];
        clear();
    }

    public void clear() {
        String binary = "0000000000000000";
        if ( binary.length() == 16 ) {
            this.value = binary.toCharArray();
        }
    }

    public int getTop() {
        return Integer.parseInt(getBinaryTop(), 2);
    }

    public void incrementTop() {
        int top = getTop();
        if ( top == 7 ) {
            top = 0;
        } else {
            top++;
        }
        setBinaryTop(top);
    }

    public void decrementTop() {
//		int top = getTop() - 1;
//		if ( top < 0 ) {
//			// stack underflow?
//			System.out.println("x87 stack underflow");
//		} else {
//			setBinaryTop(top);
//		}
        int top = getTop();
        if ( top == 7 ) {
            top = 0;
        } else {
            top++;
        }
        setBinaryTop(top);
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
        value[index] = val;
    }

    public void clearExceptions() {
        this.value[0] = '0';
        for ( int i = 7; i < 16; i++ ) {
            this.value[i] = '0';
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

    private String getBinaryTop() {
        String binaryTop = new String(new char[]{value[2], value[3], value[4]});
        return binaryTop;
    }

    private void setBinaryTop(int number) {
        String binaryNumber = Integer.toBinaryString(number);

        while ( binaryNumber.length() < 3 ) {
            binaryNumber = "0" + binaryNumber;
        }

        value[2] = binaryNumber.charAt(0);
        value[3] = binaryNumber.charAt(1);
        value[4] = binaryNumber.charAt(2);
    }

}
