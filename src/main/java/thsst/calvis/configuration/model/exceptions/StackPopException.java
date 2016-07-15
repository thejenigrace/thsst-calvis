package thsst.calvis.configuration.model.exceptions;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class StackPopException extends Exception {

    public StackPopException(int pop) {
        super("Pop operand size: " + pop + " (bytes) is greater than current stack size");
    }

}
