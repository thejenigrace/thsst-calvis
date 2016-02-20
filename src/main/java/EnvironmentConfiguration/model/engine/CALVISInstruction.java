package EnvironmentConfiguration.model.engine;

import bsh.EvalError;

/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class CALVISInstruction {
    private Instruction ins;
    private String name;
    private Object[] params;
	private Token[] tokens;
    private RegisterList registers;
    private Memory memory;
	private boolean isConditional;

    public CALVISInstruction(Instruction ins, String name, RegisterList registers, Memory memory) {
        this.ins = ins;
        this.name = name;
        this.registers = registers;
        this.memory = memory;
    }

	public CALVISInstruction(Instruction ins, String name, Object[] params,
	                         RegisterList registers, Memory memory, boolean isConditional) {
		this.ins = ins;
		this.name = name;
		this.params = params;
		this.registers = registers;
		this.memory = memory;
		this.isConditional = isConditional;
	}

    public boolean execute() throws Exception {
	    try {
		    switch (tokens.length) {
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

    private Token[] evaluateParameters(int size) throws NumberFormatException,
		    EvalError, MemoryRestrictedAccessException {
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

	public void verifyParameters(String lineNumber) throws MemoryRestrictedAccessException, EvalError,
			MemoryToMemoryException, DataTypeMismatchException, MissingSizeDirectiveException {
		int numParameters = 0;
		if ( params != null ){
			numParameters = params.length;
		}
		this.tokens = evaluateParameters(numParameters);
//	    System.out.println(numParameters);
		int line = Integer.parseInt(lineNumber, 16);
		for (int i = 0; i < tokens.length; i++) {
			System.out.println(tokens[i].getValue() + " -> " + tokens[i].getType());
		}

		if ( tokens.length > 1 && !isConditional ) {
			Token first = tokens[0];
			Token second = tokens[1];
			if ( first.isMemory() && second.isMemory() ) {
				throw new MemoryToMemoryException(first.getValue(), second.getValue(), line);
			}

			if ( first.isRegister() ) {
				int firstSize = registers.getBitSize(first);
				if ( second.isRegister() ) {
					int secondSize = registers.getBitSize(second);
					if ( firstSize != secondSize ) {
						throw new DataTypeMismatchException(first.getValue(), second.getValue(), line);
					}
				} else if ( second.isMemory() ) {
					int secondSize = memory.getBitSize(second);
					if (secondSize != 0) { // this memory has a size directive
						if ( firstSize != secondSize ) {
							throw new DataTypeMismatchException(first.getValue(), second.getValue(), line);
						}
					}
				} else if ( second.isHex() ) {
					int secondSize = second.getValue().length();
					if ( firstSize / 4 < secondSize ) {
						throw new DataTypeMismatchException(first.getValue(), second.getValue(), line);
					}
				}
			} else if ( first.isMemory() ) {
				int firstSize = memory.getBitSize(first);
				if ( second.isRegister() ) {
					int secondSize = registers.getBitSize(second);
					if ( firstSize != 0 ) { // this memory has a size directive
						if ( firstSize != secondSize ) {
							throw new DataTypeMismatchException(first.getValue(), second.getValue(), line);
						}
					}
				} else if ( second.isHex() ) {
					int secondSize = second.getValue().length();
					if ( firstSize == 0 ) {
						throw new MissingSizeDirectiveException(first.getValue());
					}
					else if ( firstSize / 4 < secondSize ) {
						throw new DataTypeMismatchException(first.getValue(), second.getValue(), line);
					}
				}
			}
		}
	}

//    public String getInstruction(){
//        // TO DO
//        return "";
//    }
}
