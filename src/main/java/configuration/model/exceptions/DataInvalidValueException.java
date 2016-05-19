package configuration.model.exceptions;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class DataInvalidValueException extends Exception {
    public DataInvalidValueException(String foundStr, String reqStr, String name) {
        super("Data Type Mismatch Exception. Found: " + foundStr + ", Required: " + reqStr + ". Found in " + name );
    }

}
