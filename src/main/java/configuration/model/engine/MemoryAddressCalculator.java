package configuration.model.engine;

import bsh.EvalError;
import bsh.Interpreter;
import configuration.model.exceptions.MemoryRestrictedAccessException;

/**
 * Created by Goodwin Chua on 1/27/2016.
 */
public class MemoryAddressCalculator {

    public static String extend(String value, int bitLength, String extension) {
        int missingZeroes = bitLength / 4 - value.length();
        for ( int k = 0; k < missingZeroes; k++ ) {
            value = extension + value;
        }
        return value.toUpperCase();
    }

    public static Token evaluateExpression(Token[] matched, RegisterList registers, Memory memory)
            throws NumberFormatException, EvalError, MemoryRestrictedAccessException {
        Token token1 = matched[0];
        //System.out.println(token1.getValue() + " : " + token1.getType());
        String baseAddress = "";
        int equationStartIndex = 1;

        if ( token1.isMemory() ) {
            token1 = matched[1];
            equationStartIndex++;
        }

        if ( token1.isRegister() ) {
            baseAddress = registers.get(token1);
        } else if ( token1.isHex() ) {
            baseAddress = extend(token1.getValue(), Memory.MAX_ADDRESS_SIZE, "0");
        } else if ( token1.isLabel() ) {
            baseAddress = memory.getFromVariableMap(token1.getValue());
        }

        String result = "";
        if ( memory.read(baseAddress) != null ) {
            Integer base = Integer.parseInt(baseAddress, 16);
            for ( int i = equationStartIndex; i < matched.length; i++ ) {
                Token token = matched[i];
                String equation = token.getValue();
                String[] operands = equation.split(" ");
                String semiEquation = base + " ";
                for ( String operand : operands ) {
                    //System.out.println(operand);
                    if ( registers.isExisting(operand) ) {
                        semiEquation += Integer.parseInt(registers.get(operand), 16) + " ";
                    } else if ( operand.matches("[0-9a-fA-F]{1," + Memory.MAX_ADDRESS_SIZE + "}") ) {
                        semiEquation += Integer.parseInt(operand, 16);
                    } else {
                        semiEquation += operand + " ";
                    }
                }
                Interpreter interpreter = new Interpreter();
                base = (Integer) interpreter.eval(semiEquation);
            }

            result = Integer.toHexString(base);
            result = extend(result, Memory.MAX_ADDRESS_SIZE, "0");
            if ( matched[0].isMemory() ) {
                result = matched[0].getValue() + "/" + result;
            }
            // System.out.println(base + " " + result);

        } else {
            throw new MemoryRestrictedAccessException(baseAddress);
        }

        return new Token(Token.MEM, result);
    }
}
