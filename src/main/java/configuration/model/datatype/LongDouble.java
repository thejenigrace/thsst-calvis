package configuration.model.datatype;

import java.math.BigInteger;

/**
 * Created by Goodwin Chua on 23/03/2016.
 */
public class LongDouble {

	private char[] fraction;
	private char[] exponent;
	private char sign;
	private char significand;

	public LongDouble(String number) {
		this.fraction = new char[63];
		this.exponent = new char[15];
		for ( int i = 0; i < fraction.length; i++ ) {
			fraction[i] = '0';
		}
		for ( int i = 0; i < exponent.length; i++ ) {
			exponent[i] = '0';
		}
		sign = '0';
		significand = '0';

//		char[] numberBreakdown = number.toCharArray();
		if ( number.charAt(0) == '-' ) {
			sign = '1';
		}

		System.out.println(number);

		String[] splitByDecimal = number.split("\\.");

		if ( splitByDecimal[0].length() == 0 ){
			splitByDecimal[0] = "0";
		} else if ( splitByDecimal[0].charAt(0) == '-' && splitByDecimal[0].length() == 1 ){
			splitByDecimal[0] = "-0";
		}
		BigInteger left;
		if ( splitByDecimal[0].length() == 0 ){
			left = new BigInteger("0");
		} else {
			left = new BigInteger(splitByDecimal[0]);
		}

		String binaryLeft = left.toString(2);
		System.out.println("BINARY LEFT: " + binaryLeft);

		String[] splitByExponent = splitByDecimal[1].split("[eE]");
		for ( int i = 0; i < splitByExponent.length; i++ ) {
			System.out.println("exponent: " + i + " " + splitByExponent[i]);
		}

		BigInteger right;
		if ( splitByExponent.length == 1 ) {
			right = new BigInteger(splitByDecimal[1]);
			System.out.println("BINARY RIGHT: " + right.toString(2));
		} else {
			right = new BigInteger(splitByExponent[0]);
			System.out.println("BINARY RIGHT: " + right.toString(2));
		}

		if ( binaryLeft.length() > 1 ) {
			System.out.println("MOVE to right: " + binaryLeft.substring(1));
			splitByDecimal[1] = binaryLeft.substring(1) + splitByDecimal[1];
		}



	}

	public String toString() {
		return sign + new String(exponent) + significand + new String(fraction);
	}

	public String toHexString() {
		String bits = toString();
		String result = "";
		char[] binaryBits = bits.toCharArray();
		for ( int i = 0; i < binaryBits.length; i+=4 ) {
			char[] block = new char[4];
			int k = 0;
			for ( int j = 0; j < block.length; j++ ) {
				block[j] = binaryBits[i + k];
				k++;
			}
			String bitString = new String(block);
			Integer binaryValue = Integer.parseInt(bitString, 2);
			result += Integer.toHexString(binaryValue);
		}

		return result;
	}

}
