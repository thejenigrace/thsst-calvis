package EnvironmentConfiguration.model.error_logging;

import java.util.ArrayList;

/**
 * Created by Ivan on 1/27/2016.
 */
public class ErrorType {

    private Types type;

    public ErrorType(Types type) {
        this.type = type;
    }

    public String generateType(ArrayList<String> variables, String lineNumber) {
        String returningType = "";
        switch (type) {
            case registerFile:
                returningType = "Register CSV File Error:\n";
                break;
            case instructionFile:
                returningType = "Instruction CSV File Error:\n";
                break;
            case configFile:
                returningType = "Configuration CSV File Error:\n";
                break;

            case generalMissingFile:
                returningType = "Missing Configuration File List:\n";
                break;
            case missingConfigFile:
                //start here
                returningType = "File not found for Config CSV file:" + variables.get(0) + "\n";
                break;
            case missingInstructionFile:
                returningType = "File not found for Instruction List CSV file: At path " + variables.get(0) + "\n";
                break;
            case missingRegisterFile:
                returningType = "File not found for Register List CSV file:" + variables.get(0) + "\n";
                break;
            case invalidRegister:
                returningType = "ERROR: EnvironmentConfiguration.model.engine.Register :\n Invalid Register Type: " + variables.get(0) + " at line " + lineNumber + ".\n";
                break;
            case doesNotExist:
                returningType = "ERROR: EnvironmentConfiguration.model.engine.Register :\n" + variables.get(0) + " Register does not exist at line " + lineNumber + ". \n";
                break;
            case dataTypeMismatch:
                returningType = "ERROR: EnvironmentConfiguration.model.engine.Register :\nData Type Mismatch Between Register " + variables.get(0) + ": " + variables.get(1) + " and " + variables.get(2) + "\n";
                break;
            case writeRegisterFailed:
                returningType = "ERROR: EnvironmentConfiguration.model.engine.Register :\nWriting to register failed\n ";
                break;
            case instructionShouldNotBeEmpty:
                returningType = "ERROR: EnvironmentConfiguration.model.engine.Instruction :\nThere are lacking parameters in line " + lineNumber + ":\n";
                for (int x = 0; x < variables.size(); x++) {
                    returningType += variables.get(x) + "\n";
                }
                break;
            case instructionShouldNotBeInvalid:
                returningType = "ERROR: EnvironmentConfiguration.model.Instruction :\nThere are invalid parameters in line " + lineNumber + ":\n";
                for (int x = 0; x < variables.size(); x++) {
                    returningType += variables.get(x) + "\n";
                }
                break;
        }

        return returningType;
    }
}
