package configuration.model.errorlogging;

import java.util.ArrayList;

/**
 * Created by Ivan on 1/27/2016.
 */
public class ErrorMessageList {

    private ArrayList<ErrorMessage> errorMessage;
    private Types errorType;

    public ErrorMessageList(Types errorType, ArrayList<ErrorMessage> errorMessage) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    public void add(ArrayList<ErrorMessage> newErrorMessage) {
        errorMessage.addAll(newErrorMessage);
    }

    public ArrayList<ErrorMessage> getListOfErrorMessages() {
        return errorMessage;
    }

    public Types getType() {
        return errorType;
    }

    public int getSizeofErrorMessages() {
        return errorMessage.size();
    }

}
