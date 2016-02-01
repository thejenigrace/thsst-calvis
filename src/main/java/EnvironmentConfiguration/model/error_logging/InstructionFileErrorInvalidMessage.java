package EnvironmentConfiguration.model.error_logging;

import java.util.ArrayList;

/**
 * Created by Ivan on 1/29/2016.
 */
public class InstructionFileErrorInvalidMessage {
    private InstructionInvalid type;
    public InstructionFileErrorInvalidMessage(InstructionInvalid type){
        this.type = type;
    }

    public String generateMessage(ArrayList<String> variables){
        String returnMessage = "";
        switch(type){
            case invalidFilePath:
                returnMessage = "Invalid File Path: " + variables.get(0);
                break;
            case invalidFileParamterCount:
               returnMessage = "Parameter Count " + variables.get(0) + " Should be an Integer For Instructions.";
                break;
            case invalidFileNegativeCount:
                returnMessage = "Parameter Count for Instruction Should not be negative.";
                break;
            case invalidDuplicateFileRegisterDestinationParameter:
                returnMessage = "Duplicate Specification For Parameter : "+ variables.get(0) + " \n";
                for(int x = 1; x < variables.size(); x++)
                    returnMessage += "       " + variables.get(x) + "\n";
                break;
            case invalidFileNoSuchAddressingVariable:
                returnMessage = "Non-Existent Specification For Parameter : "+ variables.get(0) + " \n";
                for(int x = 1; x < variables.size(); x++)
                    returnMessage += "       " + variables.get(x) + "\n";
                break;
            case invalidFileExceededParameterCount:
                returnMessage = "Exceeded Number of Parameters. Number Required:" + variables.get(0) + ", Recieved: "+
                        variables.get(1);
                break;
            case invalidFileLackingParameterCount:
                returnMessage = "Lacked the Number of Parameters. Number Required:" + variables.get(0) + ", Recieved: "+
                        variables.get(1);
                break;
            case invalidParameterFormat:
                returnMessage = "Invalid Value Specification For Parameter : "+ variables.get(0) + " \n";
                for(int x = 1; x < variables.size(); x++)
                    returnMessage += "       " + variables.get(x) + "\n";
                break;
        }
        return  returnMessage;
    }
}
