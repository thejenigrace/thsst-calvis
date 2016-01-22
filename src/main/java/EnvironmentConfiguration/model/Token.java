package EnvironmentConfiguration.model;

public class Token {
	
	static final String hex = "type_HEX";
	static final String reg = "type_REG";
	static final String mem = "type_MEM";
	static final String label = "type_LBL";
	
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
	
//	public void appendValue(String value){
//		this.value = this.value.concat(value);
//	}

	public Token(String type, String value) {
		this.type = type;
		this.value = value;
		formatValue();
	}
	
	private void formatValue(){
		switch(this.type){
			case Token.hex : // remove 0x from hex
							 this.value = this.value.substring(2).toUpperCase(); break;
			case Token.mem : 
			case Token.reg : this.value = this.value.toUpperCase(); break;
			case Token.label : break;
		}
	}

	public boolean isRegister(){
		return this.type.equals(Token.reg);
	}
	
	public boolean isHex(){
		return this.type.equals(Token.hex);
	}
	
	public boolean isMemory(){
		return this.type.equals(Token.mem);
	}
	
	public boolean isLabel(){
		return this.type.equals(Token.label);
	}
}
