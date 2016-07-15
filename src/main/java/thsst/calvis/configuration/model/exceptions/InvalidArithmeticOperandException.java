package thsst.calvis.configuration.model.exceptions;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class InvalidArithmeticOperandException extends Exception {

    public InvalidArithmeticOperandException(long l, int size) {
        super(l + " cannot be represented with a size of : " + size);
    }

}
