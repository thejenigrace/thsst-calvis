package EnvironmentConfiguration.model;

import java.math.BigInteger;

public class Calculator {
	
	static final String times = "*";
	static final String add = "+";
	
	private RegisterList registers;
	private Memory mem;

	//to delete
	public Calculator() {

	}

	public Calculator(RegisterList regs, Memory m){
		this.registers = regs;
		this.mem = m;
	}
		
	public Token compute(Token[] list, String operation){
		
		// if operand is a register, set var address with the value of the register
		String operands[] = new String[list.length];
		for ( int k = 0; k < list.length; k++){
			operands[k] = list[k].getValue();
		}
		
		for ( int g = 0; g < operands.length; g++){
			operands[g] = operands[g];
			if ( this.registers.isExisting(operands[g])){
				String regName = operands[g];
				operands[g] = this.registers.get(operands[g]);
				//System.out.println(regName + " => " + operands[g]);
				
				char[] addressCharArray = operands[g].toCharArray();
				int indexOfStringAddress = operands[g].length() - (Memory.MAX_ADDRESS_SIZE/4);

//				Check for upper half
//				if ( registers.getBitSize(regName) > EnvironmentConfiguration.model.Memory.MAX_ADDRESS_SIZE ){
//					for(int i = 0; i < indexOfStringAddress; i++){
//						if (addressCharArray[i] != '0'){
//							System.out.println("Can't use register " + regName + ":" + operands[g]);
//							System.out.println("Violated on index: " + i + " value: " + addressCharArray[i]);
//							//return null;
//
//						}
//					}
//				}
				operands[g] = operands[g].substring(indexOfStringAddress, operands[g].length());
				//System.out.println("new operands[g] " + operands[g]);
			}
		}
		int total = 0;
		int hexValues[] = new int[operands.length];
		
		for ( int m = 0; m < hexValues.length; m++){
			hexValues[m] = Integer.parseInt(operands[m], 16);
		}

		switch(operation){
			case "+" : total = hexValues[0] + hexValues[1]; break;
			case "*" : total = hexValues[0] * hexValues[1]; break;
			case "nop" : total = hexValues[0]; break;
		}
		//System.out.println("total: " + total + "returning " + Integer.toHexString(total));
		return new Token(Token.hex, "0x" + Integer.toHexString(total));
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

	public String reformatAddress(String add){
		String newAdd = add.toUpperCase();
		for (int i = 0; i < (Memory.MAX_ADDRESS_SIZE / 2) - add.length(); i++){
			newAdd = "0" + newAdd;
		}
		return newAdd;
	}

	/*
	 * For 32-bit,
	 * convert value from hex to binary
	 * returns string (32-bit binary)
	 */
	public String hexToBinaryString(String value, Token des) {
        BigInteger bi = new BigInteger(value, 16);
		String val = bi.toString(2);
		int missingZeroes = registers.getBitSize(des) - val.length();

		//zero extend
		for(int k = 0; k < missingZeroes; k++) {
			val = "0" + val;
		}

		return val;
	}

	/*
	 * For 32-bit,
	 * convert value from binary to hex
	 * returns string (32-bit hex)
	 */
	public String binaryToHexString(String value, Token des) {
        BigInteger bi = new BigInteger(value, 2);
        String val = bi.toString(16);
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

		return val;
	}

	/*
	 * For 32-bit,
	 * zero extend hex
	 * returns string (32-bit hex)
	 */
	public String hexZeroExtend(String value, Token des) {
		int missingZeroes = registers.getHexSize(des) - value.length();

		//zero extend
		if(missingZeroes > 0) {
			for(int k = 0; k < missingZeroes; k++) {
				value = "0" + value;
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
		int missingZeroes = registers.getBitSize(des) - value.length();

		//zero extend
		if(missingZeroes > 0) {
			for(int k = 0; k < missingZeroes; k++) {
				value = "0" + value;
			}
		}

		return value;
	}

	public String checkParity(String value, Token des) {
		String parity = new StringBuffer(value).reverse().toString();
		int count = 0;

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
