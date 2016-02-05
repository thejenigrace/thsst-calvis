package EnvironmentConfiguration.model.engine;

import bsh.EvalError;
import bsh.ParseException;

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

    public void execute() throws NumberFormatException, EvalError {
        int numParameters = 0;
        if ( params != null ){
            numParameters = params.length;
        }
        Token[] tokens = evaluateParameters(numParameters);
            switch(numParameters){
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

    private Token[] evaluateParameters(int size) throws  NumberFormatException, EvalError{
        Token[] tokens = new Token[size];
        for (int i = 0; i < size; i++) {
            if ( params[i] instanceof Token ) {
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
