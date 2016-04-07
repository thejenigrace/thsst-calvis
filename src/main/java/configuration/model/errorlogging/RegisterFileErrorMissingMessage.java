package configuration.model.errorlogging;

/**
 * Created by Ivan on 1/29/2016.
 */
public class RegisterFileErrorMissingMessage {

    private RegisterMissing type;

    public RegisterFileErrorMissingMessage(RegisterMissing type) {
        this.type = type;
    }

    public String generateMessage() {
        String returnMessage = "";
        switch ( type ) {
            case missingSourceRegister:
                returnMessage = "Missing Source Register Name";
                break;
            case missingNewRegister:
                returnMessage = "Missing New Register Name";
                break;
            case missingRegisterSize:
                returnMessage = "Missing Register Size";
                break;
            case missingRegisterStartIndex:
                returnMessage = "Missing New Register Starting Index";
                break;
            case missingRegisterEndIndex:
                returnMessage = "Missing New Register Ending Index";
                break;
            case missingRegisterType:
                returnMessage = "Missing New Register Type";
                break;
            default:
        }
        return returnMessage;
    }
}
