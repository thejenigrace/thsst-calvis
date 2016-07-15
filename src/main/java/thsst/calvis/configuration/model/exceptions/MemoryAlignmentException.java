package thsst.calvis.configuration.model.exceptions;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class MemoryAlignmentException extends Exception {

    public MemoryAlignmentException(String writingAddress) {
        super("Unaligned memory write at: " + writingAddress);
    }

}
