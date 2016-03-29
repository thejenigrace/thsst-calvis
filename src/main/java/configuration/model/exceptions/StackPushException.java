package configuration.model.exceptions;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class StackPushException extends Exception {

    public StackPushException() {
        super("Push failed because it exceeds the maximum stack size");
    }

}
