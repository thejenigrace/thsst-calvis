package thsst.calvis.configuration.controller;

import thsst.calvis.configuration.model.engine.ConfiguratorEnvironment;
import thsst.calvis.configuration.model.errorlogging.ErrorLogger;
import thsst.calvis.configuration.model.errorlogging.ErrorMessageListWithSize;
import thsst.calvis.configuration.model.errorlogging.FilePathLogger;
import thsst.calvis.configuration.model.filehandling.ChoiceBoxLogger;
import thsst.calvis.configuration.model.filehandling.FilePath;
import thsst.calvis.configuration.model.filehandling.FilePathHandler;
import thsst.calvis.configuration.model.filehandling.FilePathList;
import thsst.calvis.configuration.model.filehandling.SaveFile;
import thsst.calvis.MainApp;
import thsst.calvis.editor.controller.LoaderController;
import thsst.calvis.configuration.model.filehandling.StringCollectionContainer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Ivan on 12/29/2015.
 */
public class ConfigurationEnvironmentController implements Initializable {

    // Reference to the main application
    private MainApp mainApp;
    private VerifierController verifierController = new VerifierController();
    private FileChooser fileChooser = new FileChooser();
    private FileChooser.ExtensionFilter extensionFilter;
    private ArrayList<SaveFile> saveFilePaths = new ArrayList<>();
    private FilePathLogger filePathLogger = new FilePathLogger(new ArrayList<>());
    private ChoiceBoxLogger choiceBoxLogger = new ChoiceBoxLogger(new ArrayList<>());
    private ErrorLogger errorLogger = new ErrorLogger(new ArrayList<>());

    @FXML
    private ChoiceBox memoryChoiceBox;
    @FXML
    private ChoiceBox registerChoiceBox;
    @FXML
    private ChoiceBox instructionChoiceBox;
    @FXML
    private ProgressBar progressBarWorkspace;


    public ConfigurationEnvironmentController() {
        extensionFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Opens a FileChooser to let the user select an instruction set to load.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeDefaultChoiceBoxes();
        this.progressBarWorkspace.setVisible(false);
    }

    @FXML
    public void handleLocateMemory(ActionEvent event) {

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if ( file != null ) {
            if ( !memoryChoiceBox.getItems().contains(file.getAbsolutePath()) ) {
                memoryChoiceBox.getItems().add(file.getAbsolutePath());
            }
            for ( int x = 0; x < memoryChoiceBox.getItems().size(); x++ ) {
                if ( memoryChoiceBox.getItems().get(x).equals(file.getAbsolutePath()) ) {
                    memoryChoiceBox.getSelectionModel().select(x);
                }
            }
        }
    }

    @FXML
    public void handleLocateRegister(ActionEvent event) {

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if ( file != null ) {
            if ( !registerChoiceBox.getItems().contains(file.getAbsolutePath()) ) {
                registerChoiceBox.getItems().add(file.getAbsolutePath());
            }
            for ( int x = 0; x < registerChoiceBox.getItems().size(); x++ ) {
                if ( registerChoiceBox.getItems().get(x).equals(file.getAbsolutePath()) ) {
                    registerChoiceBox.getSelectionModel().select(x);
                }
            }
        }
    }

    @FXML
    public void handleLocateInstruction(ActionEvent event) {

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if ( file != null ) {
            if ( !instructionChoiceBox.getItems().contains(file.getAbsolutePath()) ) {
                instructionChoiceBox.getItems().add(file.getAbsolutePath());
            }
            for ( int x = 0; x < instructionChoiceBox.getItems().size(); x++ ) {
                if ( instructionChoiceBox.getItems().get(x).equals(file.getAbsolutePath()) ) {
                    instructionChoiceBox.getSelectionModel().select(x);
                }
            }
        }
    }

    @FXML
    public void handleFinish(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/loader.fxml"));
        Parent loaderView = (VBox) loader.load();

        MainApp.primaryStage.setScene(new Scene(loaderView));
        MainApp.primaryStage.setTitle("CALVIS");

        LoaderController loaderController = loader.getController();
        Task task = createTaskWorkspace();
        loaderController.setProgressBarWorkspaceProgressProperty(task.progressProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        // No Loader
//        ConfiguratorEnvironment configuratorEnvironment = new ConfiguratorEnvironment(getConfigurationFilePath());
//        ErrorMessageListWithSize errorMessageListWithSize =
//            verifierController.checkFileNotFoundMessage(getConfigurationFilePath());
//        if (errorMessageListWithSize.getSize() > 0) {
//            errorLogger.add(errorMessageListWithSize.getErrorMessageList());
//        }
//        errorLogger.combineErrorLogger(configuratorEnvironment.getMessageLists());
//        if (verifyErrorConfigurationFiles(errorLogger)) {
//            MainApp.hidePrimaryStage();
//            MainApp.showWorkspace(configuratorEnvironment);
//        }
    }


    @FXML
    public void handleCancel(ActionEvent event) {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Exit CALVIS?");
        exitAlert.setHeaderText("Are you sure you want to exit CALVIS?");
        Optional<ButtonType> result = exitAlert.showAndWait();
        if ( result.get() == ButtonType.OK ) {
            System.exit(0);
        } else {

        }
    }

    public ArrayList<String> getConfigurationFilePath() {
        ArrayList<String> ConfigurationFilePaths = new ArrayList<String>();
        ConfigurationFilePaths.add((String) memoryChoiceBox.getSelectionModel().getSelectedItem());
        ConfigurationFilePaths.add((String) registerChoiceBox.getSelectionModel().getSelectedItem());
        ConfigurationFilePaths.add((String) instructionChoiceBox.getSelectionModel().getSelectedItem());

        return ConfigurationFilePaths;
    }

    public ArrayList<FilePath> getFilePathList(ArrayList<String> filePathsOfChoiceBox) {
        ArrayList<FilePath> filePaths = new ArrayList<FilePath>();
        for ( int x = 0; x < filePathsOfChoiceBox.size(); x++ ) {
            Path path = Paths.get(filePathsOfChoiceBox.get(x));
            FilePath filePath = new FilePath(filePathsOfChoiceBox.get(x), path.getFileName().toString(),
                    FileHandlerController.getExtension(filePathsOfChoiceBox.get(x)));
            filePaths.add(filePath);
        }
        return filePaths;
    }

    private void initializeDefaultChoiceBoxes() {
        FilePathHandler readSaveFile = FileHandlerController.loadFilenames("SaveFile/savelist.txt");
//        FileHandlerController fhc = new FileHandlerController();
//        System.out.println(fhc.checkIfFileExists("SaveFile/savelist.txt"));
        AddToChoiceBoxes(memoryChoiceBox, readSaveFile.getFilePaths().get(0));
        AddToChoiceBoxes(registerChoiceBox, readSaveFile.getFilePaths().get(1));
        AddToChoiceBoxes(instructionChoiceBox, readSaveFile.getFilePaths().get(2));

        memoryChoiceBox.getSelectionModel().select(memoryChoiceBox.getItems().get(readSaveFile.getSelectedIndexes()
                .get(0)));
        registerChoiceBox.getSelectionModel().select(registerChoiceBox.getItems().get(readSaveFile
                .getSelectedIndexes().get(1)));
        instructionChoiceBox.getSelectionModel().select(instructionChoiceBox.getItems().get(readSaveFile
                .getSelectedIndexes().get(2)));
    }

    private Task<Void> createTaskWorkspace() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ConfiguratorEnvironment configuratorEnvironment =
                        new ConfiguratorEnvironment(getConfigurationFilePath());
                for ( double i = 0; i < 3; i = i + 0.1 ) {
                    if ( isCancelled() ) {
                        break;
                    }
                    updateProgress(i, 3);
                    try {
                        Thread.sleep(100);
                    } catch ( InterruptedException ex ) {
                        return null;
                    }
                }

                Platform.runLater(() -> {
                    ErrorMessageListWithSize errorMessageListWithSize =
                            verifierController.checkFileNotFoundMessage(getConfigurationFilePath());
                    if ( errorMessageListWithSize.getSize() > 0 ) {
                        errorLogger.add(errorMessageListWithSize.getErrorMessageList());
                    }
                    errorLogger.combineErrorLogger(configuratorEnvironment.getMessageLists());
                    if ( verifyErrorConfigurationFiles(errorLogger) ) {
                        MainApp.hidePrimaryStage();
                        MainApp.showWorkspace(configuratorEnvironment);
                    }
                });

                progressBarWorkspace.setVisible(false);
                updateProgress(3, 3);

                return null;
            }
        };
    }

    private boolean verifyErrorConfigurationFiles(ErrorLogger errorLogger) {
        ArrayList<FilePathList> filePathLists = new ArrayList<FilePathList>();

        if ( errorLogger.getAll().size() <= 0 ) {
            ArrayList<String> memoryArrayList = new ArrayList<String>(memoryChoiceBox.getItems());
            ArrayList<String> registerArrayList = new ArrayList<String>(registerChoiceBox.getItems());
            ArrayList<String> instructionArrayList = new ArrayList<String>(instructionChoiceBox.getItems());

            ArrayList<ArrayList<String>> choiceArrayList = new ArrayList<ArrayList<String>>();
            choiceArrayList.add(memoryArrayList);
            choiceArrayList.add(registerArrayList);
            choiceArrayList.add(instructionArrayList);

            ArrayList<ArrayList<FilePath>> filepathContentList = new ArrayList<ArrayList<FilePath>>();

            for ( int x = 0; x < choiceArrayList.size(); x++ ) {
                choiceBoxLogger.add(choiceArrayList.get(x));
            }

            for ( int x = 0; x < choiceBoxLogger.size(); x++ ) {
                filepathContentList.add(getFilePathList(choiceBoxLogger.get(x)));
            }

            filePathLists.add(new FilePathList("Memory File", filepathContentList.get(0)));
            filePathLists.add(new FilePathList("Register File", filepathContentList.get(1)));
            filePathLists.add(new FilePathList("Instruction File", filepathContentList.get(2)));

            filePathLogger.addAll(filePathLists);
            FileHandlerController.writeLocationFile("SaveFile/savelist.txt", filePathLogger.getAll(),
                    new StringCollectionContainer(
                            Integer.toString(memoryChoiceBox.getSelectionModel().getSelectedIndex()),
                            Integer.toString(registerChoiceBox.getSelectionModel().getSelectedIndex()),
                            Integer.toString(instructionChoiceBox.getSelectionModel().getSelectedIndex())).getStrArray()
            );
            return true;
        } else {
            verifierController.showErrorList(errorLogger);
            errorLogger.clearContents();
        }
        return false;
    }

    private void AddToChoiceBoxes(ChoiceBox choiceBox, ArrayList<String> choices) {
        for ( int x = 0; x < choices.size(); x++ ) {
            choiceBox.getItems().add(choices.get(x));
        }
    }

}
