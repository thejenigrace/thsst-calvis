package EnvironmentConfiguration.model;

import java.util.ArrayList;

/**
 * Created by Ivan on 1/27/2016.
 */
public class ErrorType {
    private Types type;
    public ErrorType(Types type){
        this.type = type;
    }

    public String generateType(ArrayList<String> variables, String lineNumber){
        String returningType = "";
        switch(type){
            case registerFile:
                returningType =  "Register CSV File Error:\n";
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
                returningType = "File not found for Instruction List CSV file: At path " + variables.get(0) + "\n" ;
                break;
            case missingRegisterFile:
                returningType = "File not found for Register List CSV file:" + variables.get(0) + "\n";
                break;
            case invalidRegister:
                returningType = "ERROR: EnvironmentConfiguration.model.Register :\n Invalid Register Type: " + variables.get(0)+ "at line " + lineNumber + ".\n";
                break;
            case doesNotExist:
                returningType = "ERROR: EnvironmentConfiguration.model.Register :\n" + variables.get(0) + " Register does not exist at line " + lineNumber + ". \n";
                break;
            case dataTypeMismatch:
                returningType = "ERROR: EnvironmentConfiguration.model.Register :\nData Type Mismatch Between Register " + variables.get(0) + ": " + variables.get(1) + " and " + variables.get(2) + "\n";
                break;
            case writeRegisterFailed:
                returningType = "ERROR: EnvironmentConfiguration.model.Register :\nWriting to register failed\n ";
                break;

            case registerShouldNumber:
                returningType = "ERROR: EnvironmentConfiguration.model.Register :\nThe value " + variables.get(0) +" in line " + lineNumber + " should be castable to Integer.\n";
                break;
            case registerShouldNotBeEmpty:
                returningType = "ERROR: EnvironmentConfiguration.model.Register :\nThere are lacking parameters in line " + lineNumber + ":\n";
                for(int x = 0; x < variables.size(); x++)
                    returningType += variables.get(x) + "\n";
                break;
            case registerShouldNotBeInvalid:
                returningType = "ERROR: EnvironmentConfiguration.model.Register :\nThere are invalid parameters in line " + lineNumber + ":\n";
                for(int x = 0; x < variables.size(); x++)
                    returningType += variables.get(x) + "\n";
                break;
            case registerInvalidSizeFormat:
                returningType = "ERROR: EnvironmentConfiguration.model.Register :\nThe The starting or end index of The declared register " + variables.get(0) + " is invalid at line " + lineNumber + ".\n";
        }

        return returningType;
    }
}
