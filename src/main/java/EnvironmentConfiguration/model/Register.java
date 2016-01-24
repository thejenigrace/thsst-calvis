package EnvironmentConfiguration.model;

public class Register {

	// size = number of bits
	protected String value;
	protected String name;
	protected int size;

	public Register(String name, int size) {
		this.name = name;
		this.size = size;
		initializeValue();
	}

	/*	initializeValue()
	 	populates register value with hex value of 0s depending on size
	  */
	public void initializeValue(){
		String regInitialValue = "";
		for (int i = 0; i < this.size / 4; i++) {
			regInitialValue += "0";
		}
		this.value = regInitialValue;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString(){
		return this.value;
	}
}
