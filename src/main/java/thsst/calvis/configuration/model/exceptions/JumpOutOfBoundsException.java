package thsst.calvis.configuration.model.exceptions;

import java.math.BigInteger;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class JumpOutOfBoundsException extends Exception {

    public JumpOutOfBoundsException(BigInteger fromAddress, BigInteger toAddress) {
        super("Jumping from [" + fromAddress + "] to [" + toAddress + "] failed.");
    }

}
