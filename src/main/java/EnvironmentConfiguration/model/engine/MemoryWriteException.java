package EnvironmentConfiguration.model.engine;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class MemoryWriteException extends Exception {

    public MemoryWriteException(String writingAddress) {
        super("Invalid memory address access at: " + writingAddress);
    }

    public MemoryWriteException(String baseAddress, String value) {
        super("Memory write failed because of size mismatch between [" + baseAddress + "] and value: " + value);
    }

}
