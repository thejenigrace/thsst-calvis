package EnvironmentConfiguration.controller;

import java.util.ArrayList;
import java.io.File;
/**
 * Created by Ivan on 12/29/2015.
 */
public class VerifierController {
    private String errorMessages;
    private File file;
    private ArrayList<String> ErrorMessageFileNotFound;
    private ArrayList<String> ErrorMessageSyntax;
    public boolean verify(ArrayList<String> ConfigurationFilePaths){
        boolean isVerified = true;
        //check for non-existing files
        isVerified = checkFileNotFoundMessage(ConfigurationFilePaths);

        return isVerified;
    }



    public ArrayList<String> getErrorMessages(){
    return ErrorMessageFileNotFound;
    }

    private boolean checkFileNotFoundMessage(ArrayList<String> ConfigurationFilePaths) {
        boolean isVerified = true;
        for (int x = 0; x < ConfigurationFilePaths.size(); x++) {
            file = new File(ConfigurationFilePaths.get(x));
            if (!file.exists()) {
                ErrorMessageFileNotFound.add("FILE NOT FOUND AT:" + file.getAbsolutePath());
                isVerified = false;
            }
        }
        return isVerified;
    }
}
