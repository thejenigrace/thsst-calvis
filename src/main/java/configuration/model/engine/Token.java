package configuration.model.engine;

public class Token {

	public static final String HEX = "ihex_type";
	public static final String REG = "reg_type";
	public static final String MEM = "mem_type";
	public static final String DEC = "dec_type";
	public static final String LABEL = "lbl_type";
	public static final String STRING = "str_type";
	public static final String FLOAT = "flt_type";
	public static final String CC = "cc_type";

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

	private void formatValue() {
		switch ( this.type ) {
			case Token.HEX: // remove 0x from HEX
				if ( this.value.contains("x") | this.value.contains("X") ) {
					this.value = this.value.substring(2).toUpperCase();
				}
				this.value = this.value.toUpperCase();
				break;
			case Token.DEC:
//				if (this.value.contains("_d") | this.value.contains("_D") ){
//					this.value = this.value.substring(0, this.value.length()-2).toUpperCase();
//				}
				this.value = Integer.toHexString(Integer.parseInt(this.value));
				this.value = this.value.toUpperCase();
				this.type = Token.HEX;
				break;
			case Token.MEM: //fall through
			case Token.REG:
				this.value = this.value.toUpperCase();
				break;
			case Token.LABEL: //fall through
			case Token.CC:
				break;
			case Token.STRING:
				this.value = this.value.substring(1, this.value.length() - 1);
				break;
		}
	}

	public boolean isRegister() {
		return this.type.equals(Token.REG);
	}

	public boolean isHex() {
		return this.type.equals(Token.HEX);
	}

	public boolean isMemory() {
		return this.type.equals(Token.MEM);
	}

	public boolean isLabel() {
		return this.type.equals(Token.LABEL);
	}

	public boolean isStringLiteral() {
		return this.type.equals(Token.STRING);
	}

	public boolean isFloatLiteral() {
		return this.type.equals(Token.FLOAT);
	}

	public boolean isCC() {
		return this.type.equals(Token.CC);
	}

	public String toString() {
		return "Token[" + this.type + " : " + this.value + "]";
	}
}
