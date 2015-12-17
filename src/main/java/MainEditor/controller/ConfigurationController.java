package MainEditor.controller;

import MainEditor.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jennica Alcalde on 10/13/2015.
 */
public class ConfigurationController {

    // Reference to the main application
    private MainApp mainApp;

    /**
     * Is called by the main application to give a reference back to itself.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Opens a FileChooser to let the user select an instruction set to load.
     */
    @FXML
    private void handleLocate(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extensionFilter
                = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");

        fileChooser.getExtensionFilters().add(extensionFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if(file != null) {

        }
    }

    @FXML
    private void handleSave(ActionEvent event) throws IOException {
//        mainApp.hidePrimaryStage();
//        mainApp.showWorkspace();
    }

    @FXML
    private void handleSkip(ActionEvent event) throws IOException {
        mainApp.hidePrimaryStage();
        mainApp.showWorkspace();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        System.exit(0);
    }
}
