package EnvironmentConfiguration.controller;

import EnvironmentConfiguration.model.ErrorMessage;
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
    private ArrayList<ErrorMessage> ErrorMessagesList = new ArrayList<ErrorMessage>();

    public ArrayList<ErrorMessage> getErrorMessages(){
    return ErrorMessagesList;
    }

    public ArrayList<ErrorMessage> checkFileNotFoundMessage(ArrayList<String> ConfigurationFilePaths) {
        boolean isVerified = true;
        boolean isAlreadyError = false;
        for (int x = 0; x < ConfigurationFilePaths.size(); x++) {
            file = new File(ConfigurationFilePaths.get(x));
            if (!file.exists()) {
                if (!isAlreadyError) {
                    ErrorMessagesList.add(new ErrorMessage("missing file", "Missing files:" + "\n"));
                    isAlreadyError = true;
                }
                ErrorMessagesList.add(new ErrorMessage("missing file", "FILE NOT FOUND AT:" + file.getAbsolutePath() + "\n"));
                isVerified = false;
            }
        }
        return ErrorMessagesList;
    }

    public void showErrorList(ArrayList<ErrorMessage> errorMessages){
        ArrayList<ErrorMessage> errorList = errorMessages;
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Configuration Exception");
        alert.setHeaderText("There is an error based on your configuration files");
        //create an expandable shit
        Label label = new Label("The exception list are as of follows:");
        String Error = "";
        for(int x = 0; x < errorList.size(); x++)
            Error = Error + errorList.get(x).getMessage();

        TextArea textArea = new TextArea(Error);
        textArea.setEditable(false);
        textArea.setWrapText(true);


        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();

    }
}
