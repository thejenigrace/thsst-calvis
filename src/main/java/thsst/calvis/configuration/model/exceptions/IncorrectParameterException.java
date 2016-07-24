package thsst.calvis.configuration.model.exceptions;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class IncorrectParameterException extends Exception {

    private int lineNumber = 0;

    public IncorrectParameterException(String name, int line) {
        super("Syntax Error: incorrect parameter/s provided for instruction: " + name);
        lineNumber = line;
    }

    public IncorrectParameterException(String name) {
        super("Syntax Error: incorrect parameter/s provided for instruction: " + name.toUpperCase() + ".");
    }

    public int getLineNumber() {
        return lineNumber;
    }

}
