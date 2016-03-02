package configuration.model.engine;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class MemoryReadException extends Exception {

    public MemoryReadException(String baseAddress, int offset) {
        super("Memory read failed at base address: " + baseAddress + " with offset: " + offset);
    }

}
