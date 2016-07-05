package configuration.model.engine;

import bsh.EvalError;
import configuration.model.exceptions.DataTypeMismatchException;
import configuration.model.exceptions.IncorrectParameterException;
import configuration.model.exceptions.InvalidSourceOperandException;
import configuration.model.exceptions.MemoryRestrictedAccessException;
import configuration.model.exceptions.MemoryToMemoryException;
import configuration.model.exceptions.MissingSizeDirectiveException;
import editor.controller.ConsoleController;
import editor.controller.VisualizationController;

import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class CalvisFormattedInstruction {

    private Instruction ins;
    private String name;
    private Object[] params;
    private Token[] tokens;
    private RegisterList registers;
    private Memory memory;
    private int appendType;
    private boolean isVerifiable = true;
    private ArrayList<String> allowable;
    private ConsoleController console;

    public CalvisFormattedInstruction(Instruction ins, String name, RegisterList registers, Memory memory) {
        this.ins = ins;
        this.name = name;
        this.registers = registers;
        this.memory = memory;
    }

    public CalvisFormattedInstruction(Instruction ins, String name, Object[] params,
                                      RegisterList registers, Memory memory, int appendType, ArrayList<String> allowable) {
        this.ins = ins;
        this.name = name;
        this.params = params;
        this.registers = registers;
        this.memory = memory;
        this.appendType = appendType;
        this.allowable = allowable;
    }

    public boolean execute(ConsoleController consoleController,
                           VisualizationController visualizationController) throws Exception {
        int numParameters = 0;
        if ( params != null ) {
            numParameters = params.length;
        }
        this.tokens = evaluateParameters(numParameters);
        try {
            switch ( tokens.length ) {
                case 0:
                    if ( name.equalsIgnoreCase("printf") ||
                            name.equalsIgnoreCase("scanf") || name.equalsIgnoreCase("cls") ) {
                        consoleController.attachCalvisInstruction(this);
                        this.console = consoleController;
                        this.ins.consoleExecute(registers, memory, consoleController);
                    } else {
                        visualizationController.attachCalvisInstruction(this);
                        this.ins.execute(registers, memory);
                    }
                    break;
                case 1:
                    visualizationController.attachCalvisInstruction(this);
                    this.ins.execute(tokens[0], registers, memory);
                    break;
                case 2:
                    visualizationController.attachCalvisInstruction(this);
                    this.ins.execute(tokens[0], tokens[1], registers, memory);
                    break;
                case 3:
                    visualizationController.attachCalvisInstruction(this);
                    this.ins.execute(tokens[0], tokens[1], tokens[2], registers, memory);
                    break;
                default:
            }
        } catch ( Exception e ) {
            throw e;
        }

        String nameCopy = name.toUpperCase();
        return !nameCopy.matches("J.*|LOOP.*|CALL|RET");
    }

    public void executeScan() {
        String text = console.retrieveScanned();
        System.out.println(text);
    }

    public void verifyParameters(int lineNumber) throws MemoryRestrictedAccessException, EvalError,
            MemoryToMemoryException, DataTypeMismatchException,
            MissingSizeDirectiveException, InvalidSourceOperandException, IncorrectParameterException {

        int numParameters = 0;
        if ( params != null ) {
            numParameters = params.length;
        }
        this.tokens = evaluateParameters(numParameters);

        int line = lineNumber + 1;

        String currentSpecification = "";

        for ( int i = 0; i < tokens.length; i++ ) {
            boolean isCC = false;
            String tokenValue = tokens[i].getValue();
            if ( tokens[i].isRegister() ) {
                if ( tokenValue.equals("CL") ) {
                    currentSpecification += "(r8|c)";
                } else if ( registers.find(tokenValue)[RegisterList.TYPE].equals("4") ) {
                    currentSpecification += "d";
                } else if ( registers.find(tokenValue)[RegisterList.TYPE].equals("5") ) {
                    currentSpecification += "s";
                } else if ( registers.find(tokenValue)[RegisterList.TYPE].equals("6") ) {
                    currentSpecification += "x";
                } else {
                    currentSpecification += "r" + registers.getBitSize(tokens[i]);
                }
            } else if ( tokens[i].isMemory() ) {
                currentSpecification += "m";
                int size = memory.getBitSize(tokens[i]);
                if ( size != 0 ) {
                    currentSpecification += size;
                } else {
                    currentSpecification += "[0-9]+";
                }
            } else if ( tokens[i].isCC() ) {
                // ignore
                isCC = true;
            } else {
                currentSpecification += tokens[i].getType().charAt(0);
            }

            if ( !isCC ) {
                currentSpecification += "/";
            }

        }
        if ( currentSpecification.endsWith("/") ) {
            currentSpecification = currentSpecification.substring(0, currentSpecification.length() - 1);
        }

        boolean parameterCheck = false;
        // instruction must have parameters if we want to verify it
        if ( allowable != null && tokens.length > 0 ) {
            for ( String specification : allowable ) {
//				System.out.println(specification + " " + currentSpecification
//						+ " " + specification.matches(currentSpecification));
                if ( specification.matches(currentSpecification) ) {
                    // matched, go outside of the loop
                    parameterCheck = true;
                    break;
                }
            }
        }

        if ( tokens.length == 0 ) {
            parameterCheck = true;
        }

        if ( parameterCheck ) {
            String upperCaseName = name.toUpperCase();
            // special check for FCMOV
            if ( upperCaseName.matches("(FCMOV|FADDP|FSUBP|FSUBRP|FMULP|FDIVP|FDIVRP)") ) {
                if ( !tokens[1].getValue().equals("ST0") ) {
                    throw new IncorrectParameterException(name, line);
                }
            } else if ( upperCaseName.matches("(FCOMI|FCOMIP|FUCOMI|FUCOMIP)") ) {
                if ( !tokens[0].getValue().equals("ST0") ) {
                    throw new IncorrectParameterException(name, line);
                }
            } else {
                int clIndex = 0;
                if ( allowable != null ) {
                    for ( String parameterSpecification : allowable ) {
                        String[] instance = parameterSpecification.split("/");
                        for ( int i = 0; i < instance.length; i++ ) {
                            if ( instance[i].equals("c") ) {
                                clIndex = i;
//							System.out.println("found CL");
                                break;
                            }
                        }
                    }
                }

                if ( tokens.length > 1 & appendType == 0 ) {
                    Token first = tokens[0];
                    Token second = tokens[1];

                    if ( first.isMemory() && second.isMemory() ) {
                        throw new MemoryToMemoryException(first.getValue(), second.getValue(), line);
                    } else {
                        if ( isVerifiable ) {
                            enforce2ParameterValidation(first, second, line, clIndex);
                        } else if ( name.equalsIgnoreCase("MOVSX") || name.equalsIgnoreCase("MOVZX") ) {
                            enforce2ParameterValidation(first, second, line, clIndex);
                        }
                    }
                } else if ( tokens.length > 2 && appendType != 0 ) { //for cmov
                    Token first = tokens[1];
                    Token second = tokens[2];

                    if ( first.isMemory() && second.isMemory() ) {
                        throw new MemoryToMemoryException(first.getValue(), second.getValue(), line);
                    } else {
                        enforce2ParameterValidation(first, second, line, clIndex);
                    }
                }
            }
        } else {
            throw new IncorrectParameterException(name, line);
        }
    }

    public void setVerifiable(boolean verifiable) {
        isVerifiable = verifiable;
    }

    private void enforce2ParameterValidation(Token first, Token second, int line, int clIndex)
            throws MemoryRestrictedAccessException, EvalError, MemoryToMemoryException,
            DataTypeMismatchException, MissingSizeDirectiveException, InvalidSourceOperandException {

        if ( first.isRegister() ) {
            int firstSize = registers.getBitSize(first);
            if ( second.isRegister() ) {
                int secondSize = registers.getBitSize(second);
                if ( clIndex == 0 ) {
                    if ( isVerifiable ) {
                        if ( firstSize != secondSize ) {
                            throw new DataTypeMismatchException(first.getValue(), second.getValue(), line);
                        }
                    } else { // for movsx, movzx
                        if ( firstSize <= secondSize ) {
                            throw new InvalidSourceOperandException(name, first.getValue(), second.getValue(), line);
                        }
                    }
                }
            } else if ( second.isMemory() ) {
                int secondSize = memory.getBitSize(second);
                if ( secondSize != 0 ) { // this memory has a size directive
                    if ( isVerifiable ) {
                        if ( firstSize != secondSize ) {
                            throw new DataTypeMismatchException(first.getValue(), second.getValue(), line);
                        }
                    } else { // for movsx, movzx
                        if ( firstSize <= secondSize ) {
                            throw new InvalidSourceOperandException(name, first.getValue(), second.getValue(), line);
                        }
                    }
                } else {
                    if ( !isVerifiable ) { // for movsx, movzx
                        throw new MissingSizeDirectiveException(second.getValue(), line);
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
                    throw new MissingSizeDirectiveException(first.getValue(), line);
                } else if ( firstSize / 4 < secondSize ) {
                    throw new DataTypeMismatchException(first.getValue(), second.getValue(), line);
                }
            }
        }
    }

    private Token[] evaluateParameters(int size) throws NumberFormatException, EvalError,
            MemoryRestrictedAccessException {
        Token[] tokens = new Token[size];
        for ( int i = 0; i < size; i++ ) {
            // System.out.println(params[i] + " : " + params[i].getClass());
            if ( params[i] instanceof String ) {
                tokens[i] = new Token(Token.CC, params[i].toString());
            } else if ( params[i] instanceof Token ) {
                tokens[i] = (Token) params[i];
            } else if ( params[i] instanceof Token[] ) {
                tokens[i] = MemoryAddressCalculator.evaluateExpression((Token[]) params[i], registers, memory);
            }
        }
        return tokens;
    }

    @Override
    public String toString(){
        return this.name + " : " + this.tokens.toString();
    }

    public String getName(){
        return this.name.toUpperCase();
    }

    public Token[] getParameterTokens() {
        return this.tokens;
    }

    public RegisterList getRegisters() {
        return this.registers;
    }

    public Memory getMemory() {
        return this.memory;
    }

}
