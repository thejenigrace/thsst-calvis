package configuration.model.exceptions;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class MemoryRestrictedAccessException extends Exception {

    public MemoryRestrictedAccessException(String baseAddress) {
        super("Restricted Memory Access on Base Address: " + baseAddress);
    }

}
