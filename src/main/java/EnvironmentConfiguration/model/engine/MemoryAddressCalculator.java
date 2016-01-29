package EnvironmentConfiguration.model.engine;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * Created by Goodwin Chua on 1/27/2016.
 */
public class MemoryAddressCalculator {

    public static String extend(String value, int bitLength, String extension) {
        int missingZeroes = bitLength/4 - value.length();
        for(int k = 0; k < missingZeroes; k++) {
            value = extension + value;
        }
        return value.toUpperCase();
    }

    public static Token evaluateExpression(Token[] matched, RegisterList registers, Memory memory) {
        Token t = matched[0];
        String baseAddress = "";
        if ( t.isRegister() ){
            baseAddress = registers.get(t);
        }
        else if (t.isHex()){
            baseAddress = extend(t.getValue(), Memory.MAX_ADDRESS_SIZE, "0");
        }
//        System.out.println(t.getValue() + t.getType());
//        System.out.println("base address: " + baseAddress);

        String result = "";
        try {
            if (memory.read(baseAddress) != null) {
                Integer base = Integer.parseInt(baseAddress, 16);
                for (int i = 1; i < matched.length; i++) {
                    Token token = matched[i];
                    String equation = token.getValue();
//            System.out.println(equation);
                    String[] operands = equation.split(" ");
                    String semiEquation = base + " ";
                    for (String operand : operands) {
                        if (registers.isExisting(operand)) {
                            semiEquation += Integer.parseInt(registers.get(operand), 16) + " ";
                        } else {
                            semiEquation += operand + " ";
                        }
                    }
//            System.out.println(semiEquation);
                    Interpreter interpreter = new Interpreter();
                    try {
                        base = (Integer) interpreter.eval(semiEquation);
                    } catch (EvalError evalError) {
                        evalError.printStackTrace();
                    }
                }
                result = Integer.toHexString(base);
                result = extend(result, Memory.MAX_ADDRESS_SIZE, "0");
                System.out.println(base + " " + result);

            } else {
                throw new NumberFormatException("Restricted Access, Base Address: " + baseAddress);
            }
        } catch (NumberFormatException e){
            e.printStackTrace();
        } finally {
            return new Token(Token.MEM, result);
        }
    }
}
