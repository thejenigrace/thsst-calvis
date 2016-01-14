package MainEditor.controller;

import MainEditor.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Ivan on 12/29/2015.
 */
public class ConfigurationEnvironmentController implements Initializable{
    // Reference to the main application
    private MainApp mainApp;
    private FileChooser fileChooser = new FileChooser();
    private FileChooser.ExtensionFilter extensionFilter;
    /**
     * Is called by the main application to give a reference back to itself.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    public ConfigurationEnvironmentController(){
        extensionFilter =  new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);
    }
    /**
     * Opens a FileChooser to let the user select an instruction set to load.
     */

    @FXML
    private ChoiceBox memoryChoiceBox;

    @FXML
    private ChoiceBox registerChoiceBox;

    @FXML
    private ChoiceBox instructionChoiceBox;

    @FXML
    public void handleLocateMemory(ActionEvent event) {

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if(file != null) {
            if(!memoryChoiceBox.getItems().contains(file.getAbsolutePath()))
                memoryChoiceBox.getItems().add(file.getAbsolutePath());
        }
    }

    @FXML
    public void handleLocateRegister(ActionEvent event) {

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if(file != null) {
            if(!registerChoiceBox.getItems().contains(file.getAbsolutePath()))
                registerChoiceBox.getItems().add(file.getAbsolutePath());
        }
    }

    @FXML
    public void handleLocateInstruction(ActionEvent event) {

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if(file != null) {
            if(!instructionChoiceBox.getItems().contains(file.getAbsolutePath()))
                instructionChoiceBox.getItems().add(file.getAbsolutePath());
        }
    }

    @FXML
    public void handleSave(ActionEvent event) throws IOException {
//        mainApp.hidePrimaryStage();
//        mainApp.showWorkspace();
    }

    @FXML
    public void handleProceedWorkSpace(ActionEvent event) throws IOException {
        mainApp.hidePrimaryStage();
        if(verifyErrorConfigurationFiles())
        mainApp.showWorkspace();
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Exit CALVIS?");
        exitAlert.setHeaderText("Are you sure you want to exit CALVIS?");
        Optional<ButtonType> result = exitAlert.showAndWait();
        if (result.get() == ButtonType.OK){
           System.exit(0);
        } else {

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDefaultChoiceBoxes();
    }

    private void initializeDefaultChoiceBoxes(){
        memoryChoiceBox.getItems().add("Memory\\config.csv");
        registerChoiceBox.getItems().add("Registers\\register_list.csv");
        instructionChoiceBox.getItems().add("Instructions\\instruction_list.csv");

        memoryChoiceBox.getSelectionModel().selectFirst();
        registerChoiceBox.getSelectionModel().selectFirst();
        instructionChoiceBox.getSelectionModel().selectFirst();
    }

    public ArrayList<String> getConfigurationFilePath(){
        ArrayList<String> ConfigurationFilePaths = new ArrayList<String>();
        ConfigurationFilePaths.add((String)memoryChoiceBox.getSelectionModel().getSelectedItem());
        ConfigurationFilePaths.add((String)registerChoiceBox.getSelectionModel().getSelectedItem());
        ConfigurationFilePaths.add((String)instructionChoiceBox.getSelectionModel().getSelectedItem());

        return ConfigurationFilePaths;
    }

    private boolean verifyErrorConfigurationFiles(){
        return true;
    }
}
