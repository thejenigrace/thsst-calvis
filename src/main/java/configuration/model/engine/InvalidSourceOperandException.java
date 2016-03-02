package configuration.model.engine;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class InvalidSourceOperandException extends Exception {

    public InvalidSourceOperandException(String instruction, String des, String src, int line) {
        super("Line: " + line + " Invalid source operand parameter: " + src
                + " for instruction: " + instruction + " " + des + ", " + src);
    }

}
