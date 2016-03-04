package configuration.model.engine;

import MainEditor.controller.ConsoleController;
import bsh.EvalError;

import java.util.ArrayList;

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
    private boolean isVerifiable = true;
    private ArrayList<String> allowable;
    private ConsoleController console;

    public CALVISInstruction(Instruction ins, String name, RegisterList registers, Memory memory) {
        this.ins = ins;
        this.name = name;
        this.registers = registers;
        this.memory = memory;
    }

    public CALVISInstruction(Instruction ins, String name, Object[] params,
            RegisterList registers, Memory memory, boolean isConditional, ArrayList<String> allowable) {
        this.ins = ins;
        this.name = name;
        this.params = params;
        this.registers = registers;
        this.memory = memory;
        this.isConditional = isConditional;
        this.allowable = allowable;
    }

    public boolean execute(ConsoleController consoleController) throws Exception {
        try {
            switch (tokens.length) {
                case 0:
                    if ( name.equalsIgnoreCase("printf") ||
                            name.equalsIgnoreCase("scanf") || name.equalsIgnoreCase("cls")) {
                        consoleController.attachCALVISInstruction(this);
                        this.console = consoleController;
                        this.ins.consoleExecute(registers, memory, consoleController);
                    } else {
                        this.ins.execute(registers, memory);
                    }
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
        } catch (Exception e) {
            throw e;
        }

        String nameCopy = name.toUpperCase();
        return !nameCopy.matches("J.*|LOOP.*|CALL|RET");
    }

    private Token[] evaluateParameters(int size) throws NumberFormatException,
            EvalError, MemoryRestrictedAccessException {
        Token[] tokens = new Token[size];
        for (int i = 0; i < size; i++) {
            // System.out.println(params[i] + " : " + params[i].getClass());
            if (params[i] instanceof String) {
                tokens[i] = new Token(Token.REG, params[i].toString());
            } else if (params[i] instanceof Token) {
                tokens[i] = (Token) params[i];
            } else if (params[i] instanceof Token[]) {
                tokens[i] = MemoryAddressCalculator.evaluateExpression((Token[]) params[i], registers, memory);
            }
        }
        return tokens;
    }

    public void executeScan() {
        String text = console.retrieveScanned();
        System.out.println(text);
    }

    public void verifyParameters(int lineNumber) throws MemoryRestrictedAccessException, EvalError,
            MemoryToMemoryException, DataTypeMismatchException,
            MissingSizeDirectiveException, InvalidSourceOperandException {
        int numParameters = 0;
        if (params != null) {
            numParameters = params.length;
        }
        this.tokens = evaluateParameters(numParameters);
//	    System.out.println(numParameters);
        int line = lineNumber + 1;
//		for (int i = 0; i < tokens.length; i++) {
//			System.out.println(tokens[i].getValue() + " -> " + tokens[i].getType());
//		}

        int clIndex = 0;
        if(allowable != null){
            for (String parameterSpecification : allowable) {
                String[] instance = parameterSpecification.split("/");
                for (int i = 0; i < instance.length; i++) {
                    if (instance[i].equals("c")) {
                        clIndex = i;
                        break;
                    }
                }
            }
        }

        if (tokens.length > 1 & !isConditional) {
            Token first = tokens[0];
            Token second = tokens[1];
            if ( isVerifiable ) {
                enforce2ParameterValidation(first, second, line, clIndex);
            } else if ( name.equalsIgnoreCase("MOVSX") || name.equalsIgnoreCase("MOVZX") ){
                enforce2ParameterValidation(first, second, line, clIndex);
            }
        } else if (tokens.length > 2 && isConditional) { //for cmov
            Token first = tokens[1];
            Token second = tokens[2];
            enforce2ParameterValidation(first, second, line, clIndex);
        }
    }

    public void setVerifiable(boolean verifiable) {
        isVerifiable = verifiable;
    }

    private void enforce2ParameterValidation(Token first, Token second, int line, int clIndex)
            throws MemoryRestrictedAccessException, EvalError, MemoryToMemoryException,
            DataTypeMismatchException, MissingSizeDirectiveException, InvalidSourceOperandException {

        if (first.isMemory() && second.isMemory()) {
            throw new MemoryToMemoryException(first.getValue(), second.getValue(), line);
        }

        if (first.isRegister()) {
            int firstSize = registers.getBitSize(first);
            if (second.isRegister()) {
                int secondSize = registers.getBitSize(second);
                if (clIndex == 0) {
                    if (isVerifiable) {
                        if (firstSize != secondSize) {
                            throw new DataTypeMismatchException(first.getValue(), second.getValue(), line);
                        }
                    } else { // for movsx, movzx
                        if (firstSize <= secondSize ) {
                            throw new InvalidSourceOperandException(name, first.getValue(), second.getValue(), line);
                        }
                    }
                }
            } else if (second.isMemory()) {
                int secondSize = memory.getBitSize(second);
                if (secondSize != 0) { // this memory has a size directive
                    if (isVerifiable) {
                        if (firstSize != secondSize) {
                            throw new DataTypeMismatchException(first.getValue(), second.getValue(), line);
                        }
                    } else { // for movsx, movzx
                        if (firstSize <= secondSize ) {
                            throw new InvalidSourceOperandException(name, first.getValue(), second.getValue(), line);
                        }
                    }
                } else {
                    if (!isVerifiable) { // for movsx, movzx
                        throw new MissingSizeDirectiveException(second.getValue(), line);
                    }
                }
            } else if (second.isHex()) {
                int secondSize = second.getValue().length();
                if (firstSize / 4 < secondSize) {
                    throw new DataTypeMismatchException(first.getValue(), second.getValue(), line);
                }
            }
        } else if (first.isMemory()) {
            int firstSize = memory.getBitSize(first);
            if (second.isRegister()) {
                int secondSize = registers.getBitSize(second);
                if (firstSize != 0) { // this memory has a size directive
                    if (firstSize != secondSize) {
                        throw new DataTypeMismatchException(first.getValue(), second.getValue(), line);
                    }
                }
            } else if (second.isHex()) {
                int secondSize = second.getValue().length();
                if (firstSize == 0) {
                    throw new MissingSizeDirectiveException(first.getValue(), line);
                } else if (firstSize / 4 < secondSize) {
                    throw new DataTypeMismatchException(first.getValue(), second.getValue(), line);
                }
            }
        }
    }

//    public String getInstruction(){
//        // TO DO
//        return "";
//    }
}
