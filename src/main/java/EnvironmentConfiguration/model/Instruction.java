package EnvironmentConfiguration.model;

public interface Instruction {

	
	public void execute(RegisterList r, Memory m);
	
	public void execute(Token a, RegisterList r, Memory m);
	
	public void execute(Token a, Token b, RegisterList r, Memory m);
	
	public void execute(Token a, Token b, Token c, RegisterList r, Memory m);
	

}
