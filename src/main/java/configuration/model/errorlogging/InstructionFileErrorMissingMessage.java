package configuration.model.errorlogging;

/**
 * Created by Ivan on 1/30/2016.
 */
public class InstructionFileErrorMissingMessage {

    private InstructionMissing type;

    public InstructionFileErrorMissingMessage(InstructionMissing type) {
        this.type = type;
    }

    public String generateMessage() {
        String returnMessage = "";
        switch ( type ) {
            case missingInstructionName:
                returnMessage = "Missing Instruction Name";
                break;
            case missingInstructionFileName:
                returnMessage = "Missing Instruction File Location";
                break;
            case missingInstructionParameterSize:
                returnMessage = "Missing Instruction Parameter Size";
                break;
            default:
        }
        return returnMessage;
    }
}
