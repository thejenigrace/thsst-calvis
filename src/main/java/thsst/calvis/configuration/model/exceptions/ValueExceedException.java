package thsst.calvis.configuration.model.exceptions;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class ValueExceedException extends Exception {

    public ValueExceedException(String registerName) {
        super("Value of " + registerName + " exceeded. Values can only be between 2^-64 and 2^64.");
    }

}
