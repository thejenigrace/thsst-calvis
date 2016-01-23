package EnvironmentConfiguration.controller;

import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.io.File;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
/**
 * Created by Ivan on 12/29/2015.
 */
public class VerifierController {
    private String errorMessages;
    private File file;
    private ArrayList<String> ErrorMessagesList = new ArrayList<String>();
    private ArrayList<String> ErrorMessageSyntax;
    public boolean verify(ArrayList<String> ConfigurationFilePaths){
        boolean isVerified = true;
        //check for non-existing files
        isVerified = checkFileNotFoundMessage(ConfigurationFilePaths);

        return isVerified;
    }



    public ArrayList<String> getErrorMessages(){
    return ErrorMessagesList;
    }

    public boolean checkFileNotFoundMessage(ArrayList<String> ConfigurationFilePaths) {
        boolean isVerified = true;
        boolean isAlreadyError = false;
        for (int x = 0; x < ConfigurationFilePaths.size(); x++) {
            file = new File(ConfigurationFilePaths.get(x));
            if (!file.exists()) {
                if (!isAlreadyError) {
                    ErrorMessagesList.add("Error List:\n");
                    isAlreadyError = true;
                }
                ErrorMessagesList.add("FILE NOT FOUND AT:" + file.getAbsolutePath() + "\n");
                isVerified = false;
            }
        }
        if(!isVerified)
            ErrorMessagesList.add("None");
        return isVerified;
    }

    public void showErrorList(){
        ArrayList<String> errorList = ErrorMessagesList;
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Configuration Exception");
        alert.setHeaderText("There is an error based on your configuration files");
        //create an expandable shit
        String Error = "";
        for(int x = 0; x < errorList.size(); x++)
            Error = Error + errorList.get(x);

        TextArea textArea = new TextArea(Error);
        textArea.setEditable(false);
        textArea.setWrapText(true);


        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();

    }
}
