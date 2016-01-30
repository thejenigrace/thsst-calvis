package EnvironmentConfiguration.model.engine;

public class Token {
	
	static final String HEX = "type_HEX";
	static final String REG = "type_REG";
	static final String MEM = "type_MEM";
	static final String DEC = "type_DEC";
	static final String LABEL = "type_LBL";
	
	private String value;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public Token(String type, String value) {
		this.type = type;
		this.value = value;
		formatValue();
	}
	
	private void formatValue(){
		switch(this.type){
			case Token.HEX: // remove 0x from HEX
				if (this.value.contains("0x")){
					this.value = this.value.substring(2).toUpperCase();
				}
				break;
			case Token.DEC:
				this.value = Integer.toHexString(Integer.parseInt(this.value));
				this.value = this.value.toUpperCase();
				this.type = Token.HEX;
				break;
			case Token.MEM:
			case Token.REG:
				this.value = this.value.toUpperCase(); break;
			case Token.LABEL:
				break;
		}
	}

	public boolean isRegister(){
		return this.type.equals(Token.REG);
	}
	
	public boolean isHex(){
		return this.type.equals(Token.HEX);
	}
	
	public boolean isMemory(){
		return this.type.equals(Token.MEM);
	}
	
	public boolean isLabel(){
		return this.type.equals(Token.LABEL);
	}
}