package EnvironmentConfiguration.model;

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

	/*
	 * For 32-bit,
	 * convert value from HEX to binary
	 * returns string (32-bit binary)
	 */
	public String hexToBinaryString(String value, Token des) {
		String val = Integer.toBinaryString(Integer.parseInt(value, 16));
		int missingZeroes = registers.getBitSize(des) - val.length();

		//zero extend
		for(int k = 0; k < missingZeroes; k++) {
			val = "0" + val;
		}

		return val;
	}

	/*
	 * For 32-bit,
	 * convert value from binary to HEX
	 * returns string (32-bit HEX)
	 */
	public String binaryToHexString(String value, Token des) {
		String val = Integer.toHexString(Integer.parseInt(value, 2));
		int missingZeroes = registers.getHexSize(des) - val.length();

		//zero extend
		for(int k = 0; k < missingZeroes; k++) {
			val = "0" + val;
		}

		return val;
	}

	/*
	 * For 32-bit,
	 * zero extend HEX
	 * returns string (32-bit HEX)
	 */
	public String hexZeroExtend(String value) {
		int missingZeroes = 8 - value.length();

		//zero extend
		for(int k = 0; k < missingZeroes; k++) {
			value = "0" + value;
		}

		return value;
	}

	/*
	 * For 32-bit,
	 * zero extend binary
	 * returns string (32-bit hinary)
	 */
	public String binaryZeroExtend(String value) {
		int missingZeroes = 32 - value.length();

		//zero extend
		for(int k = 0; k < missingZeroes; k++) {
			value = "0" + value;
		}

		return value;
	}
}
