package SimulatorVisualizer.model;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class MemoryWriteException extends Exception {

	public MemoryWriteException(String writingAddress){
		super("Invalid memory address access at: " + writingAddress);
	}

}
