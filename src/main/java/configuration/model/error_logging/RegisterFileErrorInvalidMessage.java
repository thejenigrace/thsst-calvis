package configuration.model.error_logging;

/**
 * Created by Ivan on 1/29/2016.
 */
public class RegisterFileErrorInvalidMessage {

    private RegisterInvalid type;

    public RegisterFileErrorInvalidMessage(RegisterInvalid type) {
        this.type = type;
    }

    public String generateMessage() {
        String returnMessage = "";
        switch (type) {
            case invalidSourceRegister:
                returnMessage = "Invalid Source Register Name";
                break;
            case invalidNewRegister:
                returnMessage = "Invalid New Register Name";
                break;
            case invalidRegisterSize:
                returnMessage = "Invalid Register Size";
                break;
            case invalidRegisterStartIndex:
                returnMessage = "Invalid New Register Starting Index";
                break;
            case invalidRegisterEndIndex:
                returnMessage = "Invalid New Register Ending Index";
                break;
            case invalidRegisterType:
                returnMessage = "Invalid New Register Type";
                break;
        }
        return returnMessage;
    }
}
