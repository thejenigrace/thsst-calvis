package configuration.model.engine;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class DataTypeMismatchException extends Exception {

    public DataTypeMismatchException(String first, String second) {
        super("Size mismatch between " + first + " and " + second);
    }

    public DataTypeMismatchException(String first, String second, int line) {
        super("Size mismatch between " + first + " and " + second + " at line number: " + line);
    }

    public DataTypeMismatchException(String variableName, String type, String value) {
        super("Data type mismatch on " + variableName
                + " type: " + type + " value: " + value);
    }

}
