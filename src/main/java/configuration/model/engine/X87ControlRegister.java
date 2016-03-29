package configuration.model.engine;

import java.math.BigDecimal;

/**
 * Created by Goodwin Chua on 25/03/2016.
 */
public class X87ControlRegister {

	private char[] value;

	public X87ControlRegister() {
		this.value = new char[16];
		clear();
	}

	public void clear() {
		String binary = "0000001101111111";
		if ( binary.length() == 16 ) {
			this.value = binary.toCharArray();
		}
	}

	public int getRoundingMode() {
		return Integer.parseInt(getRoundingControl(), 2);
	}

	private String getRoundingControl() {
		String binaryTop = new String(new char[]{value[4], value[5]});
		return binaryTop;
	}

	private void setRoundingControl(int number) {
		String binaryNumber = Integer.toBinaryString(number);

		while ( binaryNumber.length() < 2 ) {
			binaryNumber = "0" + binaryNumber;
		}

		value[4] = binaryNumber.charAt(0);
		value[5] = binaryNumber.charAt(1);
	}

	public String toHexString() {
		Integer binaryValue = Integer.parseInt(new String(this.value), 2);
		return Integer.toHexString(binaryValue);
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
