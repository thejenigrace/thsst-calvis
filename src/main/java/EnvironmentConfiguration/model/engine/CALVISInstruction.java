package EnvironmentConfiguration.model.engine;

import bsh.EvalError;

/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class CALVISInstruction {
    private Instruction ins;
    private String name;
    private Object[] params;
    private RegisterList registers;
    private Memory memory;

    public CALVISInstruction(Instruction ins, String name, Object[] params, RegisterList registers, Memory memory) {
        this.ins = ins;
        this.name = name;
        this.params = params;
        this.registers = registers;
        this.memory = memory;
    }

    public boolean execute() throws Exception {
        int numParameters = 0;
        if ( params != null ){
            numParameters = params.length;
        }
//	    System.out.println(numParameters);
	    Token[] tokens = evaluateParameters(numParameters);
	    try {
		    switch (numParameters) {
			    case 0:
				    this.ins.execute(registers, memory);
				    break;
			    case 1:
				    this.ins.execute(tokens[0], registers, memory);
				    break;
			    case 2:
				    this.ins.execute(tokens[0], tokens[1], registers, memory);
				    break;
			    case 3:
				    this.ins.execute(tokens[0], tokens[1], tokens[2], registers, memory);
				    break;
		    }
	    }
	    catch (Exception e){
		    throw e;
	    }

	    String nameCopy = name.toUpperCase();
	    if ( nameCopy.matches("J.*|LOOP.*|CALL|RET") ){
		    return false;
	    }
	    return true;
    }

    private Token[] evaluateParameters(int size) throws  NumberFormatException, EvalError{
        Token[] tokens = new Token[size];
        for (int i = 0; i < size; i++) {
	       // System.out.println(params[i] + " : " + params[i].getClass());
			if ( params[i] instanceof String ){
				tokens[i] = new Token(Token.REG, params[i].toString());
			}
	        else if ( params[i] instanceof Token ) {
                tokens[i] = (Token) params[i];
            }
            else if (params[i] instanceof Token[]) {
                tokens[i] = MemoryAddressCalculator.evaluateExpression((Token[]) params[i], registers, memory);
            }
        }
        return tokens;
    }

//    public String getInstruction(){
//        // TO DO
//        return "";
//    }
}
