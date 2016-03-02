package configuration.model.error_logging;

/**
 * Created by Ivan on 1/28/2016.
 */
public class ErrorMessageListWithSize {

    private int size;
    private ErrorMessageList errorMessageList;

    public ErrorMessageListWithSize(int size, ErrorMessageList errorMessageList) {
        this.size = size;
        this.errorMessageList = errorMessageList;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ErrorMessageList getErrorMessageList() {
        return errorMessageList;
    }

    public void setErrorMessageList(ErrorMessageList errorMessageList) {
        this.errorMessageList = errorMessageList;
    }
}
