package EnvironmentConfiguration.model;

public class Register {
	
	protected String value;
	protected String name;
	
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
	public Register(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	@Override
	public String toString(){
		return this.value;
	}
}
