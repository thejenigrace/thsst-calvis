package EnvironmentConfiguration.model.error_logging;

/**
 * Created by Ivan on 1/29/2016.
 */
public class InstructionFileErrorInvalidMessage {
    private InstructionInvalid type;
    public InstructionFileErrorInvalidMessage(InstructionInvalid type){
        this.type = type;
    }
//
//    public String generateMessage(ArrayList<String> variables){
//        String returnMessage = "";
//        switch(type){
//            case invalidFilePath:
//                returnMessage = "Invalid File Path: " + variables.get(0);
//                break;
//            case invalidAddressingMode:
//               returnMessage = "Invalid New Register Name";
//                break;
//            case invalidFileNegative:
//                returnMessage = "Parameter Count for Instruction Should not be negative.";
//                break;
//            case duplicateFileRegisterDestinationParameter:
//                returnMessage = "Duplicate Parameter Specification for:" + variables.get(0);
//                break;
//            case invalidRegisterType:
//                returnMessage = "Invalid New Register Type";
//                break;
//        }
//        return  returnMessage;
//    }
}
