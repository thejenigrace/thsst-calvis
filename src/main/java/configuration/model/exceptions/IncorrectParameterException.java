package configuration.model.exceptions;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class IncorrectParameterException extends Exception {

    public IncorrectParameterException(String name, int line) {
        super("Syntax Error: incorrect parameter/s provided for instruction: " + name + " at line number: " + line);
    }

    public IncorrectParameterException(String name) {
        super("Syntax Error: incorrect parameter/s provided for instruction: " + name + ".");
    }
}
