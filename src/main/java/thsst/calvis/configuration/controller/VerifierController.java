package thsst.calvis.configuration.controller;

import thsst.calvis.configuration.model.errorlogging.ErrorLogger;
import thsst.calvis.configuration.model.errorlogging.ErrorMessage;
import thsst.calvis.configuration.model.errorlogging.ErrorMessageList;
import thsst.calvis.configuration.model.errorlogging.ErrorMessageListWithSize;
import thsst.calvis.configuration.model.errorlogging.ErrorType;
import thsst.calvis.configuration.model.errorlogging.Types;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ivan on 12/29/2015.
 */
public class VerifierController {

    public ErrorMessageListWithSize checkFileNotFoundMessage(ArrayList<String> ConfigurationFilePaths) {
        ArrayList<ErrorMessage> ErrorMessagesList = new ArrayList<>();
        File file;
        for ( int x = 0; x < ConfigurationFilePaths.size(); x++ ) {
            file = new File(ConfigurationFilePaths.get(x));
            if ( !file.exists() ) {
                switch ( x ) {
                    case 0:
                        ErrorMessagesList.add(new ErrorMessage(Types.missingConfigFile,
                                HandleConfigFunctions.generateArrayListString(file.getAbsolutePath()), ""));
                        break;
                    case 1:
                        ErrorMessagesList.add(new ErrorMessage(Types.missingRegisterFile,
                                HandleConfigFunctions.generateArrayListString(file.getAbsolutePath()), ""));
                        break;
                    case 2:
                        ErrorMessagesList.add(new ErrorMessage(Types.missingInstructionFile,
                                HandleConfigFunctions.generateArrayListString(file.getAbsolutePath()), ""));
                        break;
                    default:
                }
            }
        }
        if ( ErrorMessagesList.size() == 0 ) {
            return new ErrorMessageListWithSize(0, new ErrorMessageList(Types.generalMissingFile, ErrorMessagesList));
        } else {
            return new ErrorMessageListWithSize(ErrorMessagesList.size(),
                        new ErrorMessageList(Types.generalMissingFile, ErrorMessagesList));
        }
    }

    public void showErrorList(ErrorLogger errorLogger) {
        ArrayList<ErrorMessageList> errorList = errorLogger.getAll();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Configuration Exception");
        alert.setHeaderText("There is an error based on your thsst.calvis.configuration files");
        //create an expandable
        Label label = new Label("The exception list are as of follows:");
        String Error = "";
        for ( int x = 0; x < errorList.size(); x++ ) {
            Error = Error + new ErrorType(errorList.get(x).getType()).generateType(new ArrayList<>(), "");
            for ( int y = 0; y < errorList.get(x).getSizeofErrorMessages(); y++ ) {
                Error = Error + new ErrorType(
                        errorList.get(x).getListOfErrorMessages().get(y).getType()).generateType(
                        errorList.get(x).getListOfErrorMessages().get(y).getVariables(),
                        errorList.get(x).getListOfErrorMessages().get(y).getErrorLine()
                );

            }
        }

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
