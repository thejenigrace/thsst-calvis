package configuration.model.engine;

import bsh.EvalError;
import configuration.model.exceptions.*;
import editor.controller.ConsoleController;
import editor.controller.VisualizationController;

import java.math.BigInteger;
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

    public boolean execute() throws Exception {
        int numParameters = 0;
        if ( params != null ) {
            numParameters = params.length;
        }
        this.tokens = evaluateParameters(numParameters);
        try {
            switch ( tokens.length ) {
                case 0:
                    String bigName = name.toUpperCase();
                    if ( !bigName.matches("PRINTF|SCANF|CLS") ) {
                        // console execution is at Console Controller
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
                default:
            }
        } catch ( Exception e ) {
            throw e;
        }

        String nameCopy = name.toUpperCase();
        return !nameCopy.matches("J.*|LOOP.*|CALL|RET");
    }

    public void executeScan() throws StackPopException, MemoryReadException,
            MemoryWriteException, DataTypeMismatchException, MemoryAlignmentException {
        String input = console.retrieveScanned();

        String stackPointer = registers.get("ESP");
        String pointingTo = memory.read(stackPointer, 32);
        BigInteger pointer = new BigInteger(pointingTo, 16);

        String scanFormat = "";
        // Read the format; assume it's only 1
        while ( !memory.read(pointer.toString(16), 8).equals("00") ) {
            // get one byte
            String first = memory.read(pointer.toString(16), 8);
            char ascii = (char) Integer.parseInt(first, 16);
            pointer = pointer.add(new BigInteger("1"));
            scanFormat += ascii;
        }
        // Get base address of where we store the scanned number
        // always add 4 to stack pointer since we're dealing with addresses
        BigInteger stackAddress = new BigInteger(stackPointer, 16);
        BigInteger offset = new BigInteger("4");
        stackAddress = stackAddress.add(offset);

        if ( stackAddress.compareTo(new BigInteger("FFFE", 16)) == 1 ) {
            throw new StackPopException(offset.intValue());
        } else {
            String storeToAddress = memory.read(stackAddress.toString(16), 32);
            storeToMemory(scanFormat, input, storeToAddress);
        }
    }

    private void storeToMemory(String format, String input, String baseAddress)
            throws MemoryWriteException, DataTypeMismatchException, MemoryReadException, MemoryAlignmentException {
        if ( format.matches("%d") ) {
            Short realShort = Short.parseShort(input);
            String shortToHex = Integer.toHexString(realShort.intValue());
            memory.write(baseAddress, shortToHex, 16);
        } else if ( format.matches("%ld") ) {
            BigInteger realInt = new BigInteger(input);
            String intToHex = Integer.toHexString(realInt.intValue());
            memory.write(baseAddress, intToHex, 32);
        } else if ( format.matches("%u") ) {
            BigInteger b = new BigInteger(input);
            String unsigned = getUnsigned(16, b.toString(2));
            BigInteger b2 = new BigInteger(unsigned);
            if ( b2.compareTo(new BigInteger("65535")) == 1 || b2.signum() == -1 ) {
                throw new NumberFormatException("Value out of range. Value:" + input + " Type: %u");
            }
            String unsigned16 = Integer.toHexString(b2.intValue());
            memory.write(baseAddress, unsigned16, 16);
        } else if ( format.matches("%lu") ) {
            BigInteger b = new BigInteger(input);
            String unsigned = getUnsigned(32, b.toString(2));
            BigInteger b2 = new BigInteger(unsigned);
            if ( b2.compareTo(new BigInteger("4,294,967,295")) == 1 || b2.signum() == -1 ) {
                throw new NumberFormatException("Value out of range. Value:" + input + " Type: %lu");
            } else {
                String unsigned32 = Long.toHexString(b2.longValue());
                memory.write(baseAddress, unsigned32, 32);
            }
        } else if ( format.matches("%x") ) {
            if ( !input.matches(PatternList.hexPattern) ) {
                throw new NumberFormatException("Value is not a hex Value:" + input + " Type: %x");
            } else if ( input.length() > 6 ) {
                throw new NumberFormatException("Value out of range. Value:" + input + " Type: %x");
            } else {
                String formatHex = input.substring(2);
                memory.write(baseAddress, formatHex, 16);
            }
        } else if ( format.matches("%lx") ) {
            if ( !input.matches(PatternList.hexPattern) ) {
                throw new NumberFormatException("Value is not a hex Value:" + input + " Type: %lx");
            } else if ( input.length() > 10 ) {
                throw new NumberFormatException("Value out of range. Value:" + input + " Type: %lx");
            } else {
                String formatHex = input.substring(2);
                memory.write(baseAddress, formatHex, 32);
            }
        } else if ( format.matches("%f") ) {
            float floatValue = Float.parseFloat(input);
            String representation = Integer.toHexString(Float.floatToIntBits(floatValue));
            memory.write(baseAddress, representation, 32);
        } else if ( format.matches("%lf") ) {
            Double doubleValue = Double.parseDouble(input);
            String representation = Long.toHexString(Double.doubleToLongBits(doubleValue));
            memory.write(baseAddress, representation, 64);
        } else if ( format.matches("%c") ) {
            if ( input.length() > 1 ) {
                throw new NumberFormatException("Not a character: " + input + " Type: %c");
            } else {
                byte[] realChar = input.getBytes();
                String asciiHexValue = String.format("%2X", realChar[0]);
                memory.write(baseAddress, asciiHexValue, 16);
            }
        } else if ( format.matches("%s") ) {
            byte[] bytes = input.getBytes();
            BigInteger stringPointer = new BigInteger(baseAddress, 16);
            for ( int i = 0; i < bytes.length; i++ ) {
                String asciiHexValue = String.format("%2X", bytes[i]);
                String address = stringPointer.toString(16);
                address = MemoryAddressCalculator.extend(address, 32, "0");
                memory.write(address, asciiHexValue, 8);
                stringPointer = stringPointer.add(new BigInteger("1"));
            }
            // add last 0 to end of scanned string
            String lastAddress = stringPointer.toString(16);
            lastAddress = MemoryAddressCalculator.extend(lastAddress, 32, "0");
            memory.write(lastAddress, "00", 8);
        }
    }

    private String getUnsigned(int size, String stringBits) {
        String temp = stringBits;
        // zero extend
        while ( temp.length() < 16 ) {
            temp = "0" + temp;
        }

        StringBuilder tempBit = new StringBuilder(temp);
        String returnable = "";

        if ( tempBit.charAt(0) == '1' ) {
            tempBit.setCharAt(0, '0');
            tempBit.insert(1, "1");
            BigInteger bi = new BigInteger(tempBit.toString(), 2);
            returnable = bi.toString(10);
        } else {
            BigInteger bi = new BigInteger(tempBit.toString(), 2);
            returnable = bi.toString(10);
        }
        return returnable;
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

        // instruction has no parameters
        if ( tokens.length == 0 ) {
            parameterCheck = true;
        }

        // instruction has no specifications
        if ( allowable != null && allowable.size() == 0 ) {
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
//             System.out.println(params[i] + " : " + params[i].getClass());
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
    public String toString() {
        return this.name + " : " + this.tokens.toString();
    }

    public String getName() {
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

    public void setConsole(ConsoleController console) {
        this.console = console;
    }

    public Instruction getInstruction() {
        return this.ins;
    }

}
