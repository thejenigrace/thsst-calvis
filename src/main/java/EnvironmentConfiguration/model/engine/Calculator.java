package EnvironmentConfiguration.model.engine;

import java.math.BigInteger;

public class Calculator {

	private RegisterList registers;
	private Memory memory;

	public Calculator(RegisterList registers, Memory memory) {
		this.registers = registers;
		this.memory = memory;
	}
	
	public boolean evaluateCondition(String condition) {
		String con = condition.toUpperCase();
		EFlags flags = registers.getEFlags();
		String CF = flags.getCarryFlag();
		String ZF = flags.getZeroFlag();
		String OF = flags.getOverflowFlag();
		//String AF = flags.getAuxiliaryFlag();
		String PF = flags.getParityFlag();
		String SF = flags.getSignFlag();
		
		switch(con) {
			case "A"	: // fall through
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
			case "CXZ"  : return registers.get("CX").equals("0000");
			case "ECXZ" : return registers.get("ECX").equals("00000000");
			default		: System.out.println("Condition not found");
						  return false;	
		}	
	}

	public String hexToBinaryString(String value, Token des) {
		BigInteger bi = new BigInteger(value, 16);
		String val = bi.toString(2);

		if( des.isRegister() ) {
			int missingZeroes = registers.getBitSize(des) - val.length();

			//zero extend
			for(int k = 0; k < missingZeroes; k++){
				val = "0" + val;
			}
		}
		else if ( des.isMemory() ) {
			int missingZeroes = memory.getBitSize(des) - val.length();

			//zero extend
			for(int k = 0; k < missingZeroes; k++){
				val = "0" + val;
			}
		}

		return val;
	}

	public String binaryToHexString(String value, Token des) {
		BigInteger bi = new BigInteger(value, 2);
		String val = bi.toString(16);

		if( des.isRegister() ) {
			int registerSize = registers.getHexSize(des);
			int missingZeroes = registerSize - val.length();

			//zero extend
			for(int k = 0; k < missingZeroes; k++) {
				val = "0" + val;
			}

			//remove carry flag
			if( val.length() > registerSize ) {
				StringBuilder sb = new StringBuilder();

				for(int i = 1; i < val.length(); i++)
					sb.append(val.charAt(i));

				val = sb.toString();
			}
		}
		else if ( des.isMemory() ) {
			int missingZeroes = memory.getHexSize(des) - val.length();

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

		if( str.charAt(0) == '1' ) {
			BigInteger ry = value.subtract(BigInteger.ONE);

			String temp = ry.toString(2);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < temp.length(); i++) {
				if ( temp.charAt(i) == '1' )
					sb.append("0");
				else if ( temp.charAt(i) == '0' )
					sb.append("1");
			}

			BigInteger z = new BigInteger(sb.toString(), 2);

			String f = "-" + z.toString();

			result = Long.parseLong(f);
		}

		return result;
	}

	public String cutToCertainHexSize(String value, int size) {
		StringBuilder sb = new StringBuilder();

		for(int i = value.length()-1; i >= 0; i--) {
			if( sb.length() < size )
				sb.append(value.charAt(i));
		}
		return sb.reverse().toString();
	}

	public String[] cutToCertainSize(String value, Token src) {
		BigInteger bi = new BigInteger(value, 16);
		String val = bi.toString(16);

		int size = registers.getHexSize(src);
		int missingZeroes = size * 2 - val.length();

		//zero extend
		for(int k = 0; k < missingZeroes; k++) {
			val = "0" + val;
		}

		StringBuilder sb0 = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		String[] result = new String[2];
		for(int i = 0; i < val.length(); i++){
			if( i < size ) {
				sb0.append(val.charAt(i));
			}
			else {
				sb1.append(val.charAt(i));
			}

			if( i-size-1 == 0 ) {
				result[0] = sb0.toString();
			}

			if ( i-size == size-1 ) {
				result[1]=sb1.toString();
			}
		}

		return result;
	}

	public String hexZeroExtend(String value, Token des) {
		if(des.isRegister()) {
			int missingZeroes = registers.getHexSize(des) - value.length();

			//zero extend
			if( missingZeroes > 0 ) {
				for(int k = 0; k < missingZeroes; k++) {
					value = "0" + value;
				}
			}

			if( value.length() > registers.getHexSize(des) ) {
				value = value.substring(1);
			}
		}
		else if( des.isMemory() ) {
			int missingZeroes = memory.getHexSize(des) - value.length();

			//zero extend
			if(missingZeroes > 0) {
				for(int k = 0; k < missingZeroes; k++) {
					value = "0" + value;
				}
			}

			if( value.length() > memory.getHexSize(des) ) {
				value = value.substring(1);
			}
		}

		return value;
	}

	public String binaryZeroExtend(String value, Token des) {
		if( des.isRegister() ) {
			int missingZeroes = registers.getBitSize(des) - value.length();

			//zero extend
			if( missingZeroes > 0 ) {
				for(int k = 0; k < missingZeroes; k++) {
					value = "0" + value;
				}
			}

			if( value.length() > registers.getBitSize(des) ) {
				value = value.substring(1);
			}
		}
		else if( des.isMemory() ) {
			int missingZeroes = memory.getBitSize(des) - value.length();

			//zero extend
			if( missingZeroes > 0 ) {
				for(int k = 0; k < missingZeroes; k++) {
					value = "0" + value;
				}
			}

			if( value.length() > memory.getBitSize(des) ) {
				value = value.substring(1);
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

		if( result.toString(2).length() > 4 ) {
			return "1";
		}

		return "0";
	}

	public String checkAuxiliarySub(String src, String des) {
		String s = new StringBuffer(src).reverse().toString();
		String d = new StringBuffer(des).reverse().toString();

		int r = 0;
		int borrow = 0;

		for(int i = 0; i < d.length(); i++) {
			r = Integer.parseInt(String.valueOf(d.charAt(i)))
					- Integer.parseInt(String.valueOf(s.charAt(i)))
					- borrow;

			if( r < 0 ) {
				borrow = 1;

				if( i == 3 ) {
					return "1";
				}
			}
			else {
				borrow = 0;

				if( i > 3 ) {
					break;
				}
			}
		}
		return "0";
	}

	public String checkParity(String value) {
		String parity = new StringBuffer(value).reverse().toString();
		int count = 0;

		if( parity.length() < 8 ) {
			int missingZeroes = 8 - parity.length();

			for(int k = 0; k < missingZeroes; k++) {
				parity = parity + "0";
			}
		}

		for(int i = 0; i < 8; i++) {
			if( parity.charAt(i) == '1' ) {
				count++;
			}
		}

		if( count % 2 == 0 && count != 0 ) {
			return "1";
		}
		return "0";
	}

	public boolean isWithinBounds(BigInteger difference, int size) {
		boolean flag = true;
		BigInteger startBoundary = new BigInteger("0");

		switch (size){
			case 8: // rel8
				startBoundary = new BigInteger("128");
				break;
			case 16: // rel16 32768
				startBoundary = new BigInteger("32768");
				break;
		}

		BigInteger endBoundary = new BigInteger("1");
		endBoundary = startBoundary.subtract(endBoundary).negate();

		int start = difference.compareTo(startBoundary);
		int end = difference.compareTo(endBoundary);
		flag = ( start == -1 || start == 0 ) && ( end == 1 || end == 0 );

		return flag;
	}

}