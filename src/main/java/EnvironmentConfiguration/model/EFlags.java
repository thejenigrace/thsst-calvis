package EnvironmentConfiguration.model;

public class EFlags extends Register {

	public EFlags(String name, int size) {
		super(name, size);
	}

	@Override
	public void initializeValue(){
		super.initializeValue();
		this.value = this.value.substring(1) + "2";
	}
	
	public char getFlagIndex(int index){
		int hex = Integer.parseInt(this.value, 16);
		String val = Integer.toBinaryString(hex);
		int missingZeroes = 32 - val.length();
		
		//zero extend
		for ( int k = 0; k < missingZeroes; k++){
			val = "0" + val;
		}
		return val.charAt(32 - 1 - index);
	}
	
	public void setFlagIndex(int index, String a){
		if ( a.equals("0") || a.equals("1") ){
			int hex = Integer.parseInt(this.value, 16);
			String val = Integer.toBinaryString(hex);
			int missingZeroes = 32 - val.length();
			
			//zero extend
			for ( int k = 0; k < missingZeroes; k++){
				val = "0" + val;
			}

			char[] extended = val.toCharArray();
			extended[32 - 1 - index] = a.charAt(0);
			val = new String(extended);
			String hexvalue = Integer.toHexString(Integer.parseInt(val, 2));
			int zeroExtendHexValue = 8 - hexvalue.length();
			
			//zero extend
			for ( int k = 0; k < zeroExtendHexValue; k++){
				hexvalue = "0" + hexvalue;
			}
			this.value = hexvalue.toUpperCase();
			
			
		}
		else {
			System.out.println("Invalid flag value");
		}
	}

	public String getCarryFlag() {
		return getFlagIndex(0) + "";
	}
	
	public String getParityFlag() {
		return getFlagIndex(2) + "";
	}
	
	public String getAuxiliaryFlag() {
		return getFlagIndex(4) + "";
	}
	
	public String getZeroFlag() {
		return getFlagIndex(6) + "";
	}
	
	public String getSignFlag() {
		return getFlagIndex(7) + "";
	}
	
	public String getOverflowFlag() {
		return getFlagIndex(11) + "";
	}
	
	public void setCarryFlag(String value) {
		setFlagIndex(0, value);
	}
	
	public void setParityFlag(String value) {
		setFlagIndex(2, value);
	}
	
	public void setAuxiliaryFlag(String value) {
		setFlagIndex(4, value);
	}
	
	public void setZeroFlag(String value) {
		setFlagIndex(6, value);
	}
	
	public void setSignFlag(String value) {
		setFlagIndex(7, value);
	}
	
	public void setOverflowFlag(String value) {
		setFlagIndex(11, value);
	}
}
