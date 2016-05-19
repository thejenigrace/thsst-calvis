package configuration.model.exceptions;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class DataOutOfRangeException extends Exception {

    public DataOutOfRangeException(String first) {
        super("Value can only be between 32767 and 0. Found at " + first);
    }

}
