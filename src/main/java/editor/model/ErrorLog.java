package editor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Goodwin Chua on 2/9/2016.
 */
public class ErrorLog {

    private final StringProperty errorType;
    private final StringProperty errorCause;
    private final StringProperty errorMessage;

    public ErrorLog(String errorType, String errorCause, String errorMessage) {
        this.errorType = new SimpleStringProperty(errorType);
        this.errorCause = new SimpleStringProperty(errorCause);
        this.errorMessage = new SimpleStringProperty(errorMessage);
    }

    public String getErrorType() {
        return errorType.get();
    }

    public StringProperty errorTypeProperty() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType.set(errorType);
    }

    public String getErrorCause() {
        return errorCause.get();
    }

    public StringProperty errorCauseProperty() {
        return errorCause;
    }

    public void setErrorCause(String errorCause) {
        this.errorCause.set(errorCause);
    }

    public String getErrorMessage() {
        return errorMessage.get();
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.set(errorMessage);
    }

}
