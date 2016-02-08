package EnvironmentConfiguration.model.engine;

import java.math.BigInteger;

public class Calculator {

	private RegisterList registers;
	private Memory mem;

	public Calculator(RegisterList regs, Memory m){
		this.registers = regs;
		this.mem = m;
	}

	public boolean evaluateCondition(String condition){
		String con = condition.toUpperCase();
		EFlags flags = registers.getEFlags();
		String CF = flags.getCarryFlag();
		String ZF = flags.getZeroFlag();
		String OF = flags.getOverflowFlag();
		//String AF = flags.getAuxiliaryFlag();
		String PF = flags.getParityFlag();
		String SF = flags.getSignFlag();

		switch(con){
			case "A"	:
			case "NBE"	: return ( CF.equals("0") || ZF.equals("0") );
			case "AE"	:
			case "NB"	: return CF.equals("0");
			case "B"	:
			case "NAE"	: return ( CF.equals("1") || ZF.equals("1") );
			case "BE"	:
			case "NA"	: return CF.equals("1");
			case "G"	:
			case "NLE"	: return ( (SF.equals(OF)) || (ZF.equals("1")) );
			case "GE"	:
			case "NL" 	: return SF.equals(OF);
			case "L"	:
			case "NGE"	: return !SF.equals(OF);
			case "LE"	:
			case "NG"	: return ( (!SF.equals(OF)) || (ZF.equals("1")) );
			case "E" 	:
			case "Z"	: return ZF.equals("1");
			case "NE"	:
			case "NZ"	: return ZF.equals("0");
			case "P"	:
			case "PE" 	: return PF.equals("1");
			case "NP"	:
			case "PO"	: return PF.equals("0");
			case "O"	: return OF.equals("1");
			case "NO"	: return OF.equals("0");
			case "C" 	: return CF.equals("1");
			case "NC"	: return CF.equals("0");
			case "S"	: return SF.equals("1");
			case "NS"	: return SF.equals("0");
			default		: System.out.println("Condition not found");
				return false;
		}
	}

	public String hexToBinaryString(String value, Token des){
		BigInteger bi = new BigInteger(value, 16);
		String val = bi.toString(2);

		if(des.isRegister()){
			int missingZeroes = registers.getBitSize(des) - val.length();

			//zero extend
			for(int k = 0; k < missingZeroes; k++){
				val = "0" + val;
			}
		}
		else if (des.isMemory()){
			int missingZeroes = mem.getBitSize(des) - val.length();

			//zero extend
			for(int k = 0; k < missingZeroes; k++){
				val = "0" + val;
			}
		}

		return val;
	}

	public String binaryToHexString(String value, Token des){
		BigInteger bi = new BigInteger(value, 2);
		String val = bi.toString(16);

		if(des.isRegister()) {
			int registerSize = registers.getHexSize(des);
			int missingZeroes = registerSize - val.length();

			//zero extend
			for(int k = 0; k < missingZeroes; k++) {
				val = "0" + val;
			}

			//remove carry flag
			System.out.println("val.length() = " + val.length());
			System.out.println("registerSize = " + registerSize);
			if(val.length() > registerSize) {
				System.out.println("REMOVE CARRY FLAG");
				StringBuilder sb = new StringBuilder();

				for(int i = 1; i < val.length(); i++)
					sb.append(val.charAt(i));

				val = sb.toString();
				System.out.println("val = " + val);
			}
		}
		else if (des.isMemory()){
			int missingZeroes = mem.getBitSize(des) - val.length();

			//zero extend
			for(int k = 0; k < missingZeroes; k++){
				val = "0" + val;
			}
		}
		return val;
	}

	public long convertToSignedInteger(BigInteger value, int size) {
		long result = Long.parseLong(value.toString());
		String str = value.toString(2);

		int missingZeroes = size - str.length();

		//zero extend
		for(int k = 0; k < missingZeroes; k++) {
			str = "0" + str;
		}

		if(str.charAt(0) == '1') {
			BigInteger ry = value.subtract(BigInteger.ONE);

//            System.out.println("ry = " + ry.toString(2));

			String temp = ry.toString(2);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < temp.length(); i++) {
				if (temp.charAt(i) == '1')
					sb.append("0");
				else if (temp.charAt(i) == '0')
					sb.append("1");
			}

//            System.out.println("sb = " + sb.toString());

			BigInteger z = new BigInteger(sb.toString(), 2);
//            System.out.println("b = " + z.toString());

			String f = "-" + z.toString();
			System.out.println("parse = " + Long.parseLong(f));

			result = Long.parseLong(f);
			System.out.println(Long.toHexString(result));
		}

		return result;
	}

	public String cutToCertainHexSize(String value, int size) {
		StringBuilder sb = new StringBuilder();
//        int i = value.length()-1;
//        while(sb.length() != size) {
//            sb.append(value.charAt(i));
//            i--;
//        }

		for(int i = value.length()-1; i >= 0; i--) {
			if(sb.length() < size)
				sb.append(value.charAt(i));
		}
		return sb.reverse().toString();
	}

	public String[] cutToCertainSize(String value, Token src) {
		BigInteger bi = new BigInteger(value, 16);
		String val = bi.toString(16);
		int size = registers.getHexSize(src);
		System.out.println("c.size = " + size);
		int missingZeroes = size * 2 - val.length();

		//zero extend
		for(int k = 0; k < missingZeroes; k++) {
			val = "0" + val;
		}

		System.out.println("c.val = " + val);

		StringBuilder sb0 = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		String[] result = new String[2];
		for(int i = 0; i < val.length(); i++){
			if(i < size)
				sb0.append(val.charAt(i));
			else
				sb1.append(val.charAt(i));

//            System.out.println("i-size == size-1 : " + (i-size) + " == " + (size-1));

			if(i-size-1 == 0){
				System.out.println("Initialize result[0]");
				result[0] = sb0.toString();
			}

			if (i-size == size-1){
				System.out.println("Initialize result[1]");
				result[1]=sb1.toString();
			}
		}

		return result;
	}

	/*
	 * For 32-bit,
	 * zero extend HEX
	 * returns string (32-bit HEX)
	 */
	public String hexZeroExtend(String value, Token des) {
		if(des.isRegister()) {
			int missingZeroes = registers.getHexSize(des) - value.length();

			//zero extend
			if(missingZeroes > 0) {
				for(int k = 0; k < missingZeroes; k++) {
					value = "0" + value;
				}
			}
		}
		else if(des.isMemory()) {
			int missingZeroes = mem.getHexSize(des) - value.length();

			//zero extend
			if(missingZeroes > 0) {
				for(int k = 0; k < missingZeroes; k++) {
					value = "0" + value;
				}
			}
		}


		return value;
	}

	/*
	 * For 32-bit,
	 * zero extend binary
	 * returns string (32-bit hinary)
	 */
	public String binaryZeroExtend(String value, Token des) {
		if(des.isRegister()) {
			int missingZeroes = registers.getBitSize(des) - value.length();

			//zero extend
			if(missingZeroes > 0) {
				for(int k = 0; k < missingZeroes; k++) {
					value = "0" + value;
				}
			}
		}
		else if(des.isMemory()) {
			int missingZeroes = mem.getBitSize(des) - value.length();

			//zero extend
			if(missingZeroes > 0) {
				for(int k = 0; k < missingZeroes; k++) {
					value = "0" + value;
				}
			}
		}

		return value;
	}

	public String checkAuxiliary(String a, String b) {
		String sx = "" + a.charAt(a.length()-1);
		String sy = "" + b.charAt(b.length()-1);
		BigInteger x = new BigInteger(sx, 16);
		BigInteger y = new BigInteger(sy, 16);
		BigInteger result = y.add(x);

		System.out.println("AF result = " + result.toString(2));
		System.out.println("result.toString(2).length() = " + result.toString(2).length());
		System.out.println("y.toString(2).length() = " + y.toString(2).length());

		if(result.toString(2).length() > 4)
			return "1";

		return "0";
	}

	public String checkParity(String value) {
		String parity = new StringBuffer(value).reverse().toString();
		int count = 0;

		if(parity.length() < 8) {
			int missingZeroes = 8 - parity.length();

			for(int k = 0; k < missingZeroes; k++) {
				parity = parity + "0";
			}
		}

		for(int i = 0; i < 8; i++) {
			if(parity.charAt(i) == '1') {
				count++;
			}
		}

		if(count % 2 == 0 && count != 0) {
			return "1";
		}
		return "0";
	}
}
