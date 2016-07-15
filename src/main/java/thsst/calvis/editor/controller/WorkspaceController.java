package thsst.calvis.editor.controller;

import thsst.calvis.configuration.controller.ConfiguratorEnvironment;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import thsst.calvis.MainApp;
import thsst.calvis.editor.view.FileEditorTab;
import thsst.calvis.editor.view.FileEditorTabPane;
import thsst.calvis.editor.view.TextEditor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import thsst.calvis.simulatorvisualizer.controller.SystemController;

import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * Created by Jennica Alcalde on 10/1/2015.
 */
public class WorkspaceController implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private AnchorPane registerPane;
    @FXML
    private AnchorPane paneMemory;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnUndo;
    @FXML
    private Button btnRedo;
    @FXML
    private Button btnPlay;
    @FXML
    private Button btnStop;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnPrevious;
    @FXML
    private Button btnFindUp;
    @FXML
    private Button btnFindDown;
    @FXML
    private Button btnHide;

    @FXML
    private SplitPane fileEditorSplitPane;

    @FXML
    private TabPane bottomTabPane;

    @FXML
    private ToolBar toolbarMain;

    @FXML
    private VBox fileEditorVBox;

    private FileEditorTabPane fileEditorTabPane;

    private SystemController sysCon;

    private TextField textFieldFind;
    private boolean isHide = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize find text field
        this.textFieldFind = TextFields.createClearableTextField();
        this.textFieldFind.setPrefWidth(250.0);
        this.toolbarMain.getItems().add(15, textFieldFind);
    }

    public Alert createAlert(AlertType alertType, String title, String contentTextFormat,
                             Object... contentTextArgs) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(MessageFormat.format(contentTextFormat, contentTextArgs));
        alert.initOwner(MainApp.primaryStage);
        return alert;
    }

    private void init() {
        this.btnSave.disableProperty().bind(createActiveBooleanProperty(FileEditorTab::modifiedProperty).not());
        this.btnUndo.disableProperty().bind(createActiveBooleanProperty(FileEditorTab::canUndoProperty).not());
        this.btnRedo.disableProperty().bind(createActiveBooleanProperty(FileEditorTab::canRedoProperty).not());

        this.textFieldFind.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                fileEditorTabPane.findTextFieldChange(newValue);
            }
        });
    }

    private void showRegisterPane() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/registers.fxml"));
        SplitPane registersView = loader.load();
        SplitPane.setResizableWithParent(registersView, Boolean.TRUE);
        registerPane.getChildren().add(registersView);
        AnchorPane.setTopAnchor(registersView, 0.0);
        AnchorPane.setBottomAnchor(registersView, 0.0);
        AnchorPane.setLeftAnchor(registersView, 0.0);
        AnchorPane.setRightAnchor(registersView, 0.0);

        // Attach registersController to SystemController
        RegistersController registersController = loader.getController();
        this.sysCon.attach(registersController);
        registersController.build();
    }

    private void showMemoryPane() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation((getClass().getResource("/fxml/memory.fxml")));
            VBox memoryView = loader.load();
            SplitPane.setResizableWithParent(memoryView, Boolean.TRUE);
            AnchorPane.setTopAnchor(memoryView, 0.0);
            AnchorPane.setBottomAnchor(memoryView, 0.0);
            AnchorPane.setLeftAnchor(memoryView, 0.0);
            AnchorPane.setRightAnchor(memoryView, 0.0);

            paneMemory.getChildren().add(memoryView);

            // Attach memoryController to SystemController
            MemoryController memoryController = loader.getController();
            this.sysCon.attach(memoryController);
            memoryController.build();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private void showBottomTabPane() throws Exception {
        ConsoleController consoleController = new ConsoleController();
        this.bottomTabPane.getTabs().add(consoleController.getTab());
        this.bottomTabPane.getTabs().add(createErrorLoggerTab(null));
        this.sysCon.attach(consoleController);
        consoleController.build();

        VisualizationController visualizationController = new VisualizationController();
        this.bottomTabPane.getTabs().add(visualizationController.getTab());
        this.sysCon.attach(visualizationController);
        visualizationController.build();
    }

    @FXML
    private void handleConsole(ActionEvent event) {
        this.bottomTabPane.getSelectionModel().select(0);
    }

    @FXML
    private void handleErrorLogger(ActionEvent event) {
        this.bottomTabPane.getSelectionModel().select(1);
    }

    @FXML
    private void handleVisualizer(ActionEvent event) {
        this.bottomTabPane.getSelectionModel().select(2);
    }

    private void showFileEditorPane() {
        this.fileEditorTabPane = new FileEditorTabPane(this, this.fileEditorSplitPane.widthProperty(), this.fileEditorSplitPane.heightProperty());
        this.fileEditorVBox.getChildren().add(0, this.fileEditorTabPane.getNode());
        this.fileEditorTabPane.newFileEditor();
    }

    @FXML
    private void handleHide(ActionEvent event) {
        if ( !isHide ) {
            isHide = true;
            this.fileEditorSplitPane.setDividerPositions(1);
            this.changeIconToShow();
        } else {
            isHide = false;
            this.fileEditorSplitPane.setDividerPositions(0.65);
            this.changeIconToHide();
        }
    }

    private Tab createErrorLoggerTab(Exception e) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/error_logger.fxml"));
        SplitPane errorLoggerView = loader.load();

        ErrorLoggerController errorLoggerController = loader.getController();

        Tab tab = new Tab();
        tab.setText("Error Logger");
        tab.setContent(errorLoggerView);

        this.sysCon.attach(errorLoggerController);
        errorLoggerController.build(e);
        return tab;
    }

    public void handleErrorLoggerTab(Exception e) throws Exception {
        this.bottomTabPane.getTabs().set(1, createErrorLoggerTab(e));
        this.bottomTabPane.getSelectionModel().select(1);
    }


    /**
     * Action for Play Simulation; a MenuItem in Execute.
     *
     * @param event
     */
    @FXML
    private void handlePlay(ActionEvent event) {
        this.fileEditorTabPane.play();
    }

    @FXML
    private void handlePause(ActionEvent event) {
        this.fileEditorTabPane.pause();
    }

    /**
     * Action for Stop Simulation; a MenuItem in Execute.
     *
     * @param event
     */
    @FXML
    private void handleStop(ActionEvent event) {
        this.fileEditorTabPane.stop();
    }

    /**
     * Action for Previous Simulation; a MenuItem in Execute.
     *
     * @param event
     */
    @FXML
    private void handlePrevious(ActionEvent event) {
        this.fileEditorTabPane.previous();
    }

    /**
     * Action for Next Simulation; a MenuItem in Execute.
     *
     * @param event
     */
    @FXML
    private void handleNext(ActionEvent event) {
        this.fileEditorTabPane.next();
    }

    /**
     * Action for Reset Simulation; a MenuItem in Execute.
     *
     * @param event
     */
    @FXML
    private void handleReset(ActionEvent event) {
        this.fileEditorTabPane.reset();
    }

    /**
     * Action for New File; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleNewFile(ActionEvent event) {
        this.fileEditorTabPane.newFileEditor();
    }

    /**
     * Action for Open File; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleOpenFile(ActionEvent event) {
        this.fileEditorTabPane.openFileEditor();
    }

    /**
     * Action for Save; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleSaveFile(ActionEvent event) {
        this.fileEditorTabPane.saveFileEditor(this.fileEditorTabPane.getActiveFileEditor());
    }

    /**
     * Action for Save As; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleSaveAsFile(ActionEvent event) {
        this.fileEditorTabPane.saveAsFileEditor(this.fileEditorTabPane.getActiveFileEditor());
    }

    /**
     * Action for Settings; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleSettings(ActionEvent event) {
        try {
            // Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/settings.fxml"));
            Parent settingsView = (BorderPane) loader.load();

            Stage settingsDialogStage = new Stage();
            settingsDialogStage.initModality(Modality.APPLICATION_MODAL);
            settingsDialogStage.setTitle("Settings");
            settingsDialogStage.setScene(new Scene(settingsView));
            settingsDialogStage.setResizable(false);
            settingsDialogStage.centerOnScreen();
            settingsDialogStage.show();

            SettingsController settingsController = loader.getController();
            settingsController.setWorkspaceController(this);
            settingsController.setDialogStage(settingsDialogStage);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCut(ActionEvent event) {
        this.fileEditorTabPane.cut();
    }

    @FXML
    private void handleCopy(ActionEvent event) {
        this.fileEditorTabPane.copy();
    }

    @FXML
    private void handlePaste(ActionEvent event) {
        this.fileEditorTabPane.paste();
    }

    @FXML
    private void handleUndo(ActionEvent event) {
        this.fileEditorTabPane.undo();
    }

    @FXML
    private void handleRedo(ActionEvent event) {
        this.fileEditorTabPane.redo();
    }

    @FXML
    private void handleExitApp(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleFindAndReplace(ActionEvent event) {
        try {
            // Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/find_and_replace.fxml"));
            Parent findAndReplaceView = (BorderPane) loader.load();

            Stage findAndReplaceDialogStage = new Stage();
            findAndReplaceDialogStage.initModality(Modality.APPLICATION_MODAL);
            findAndReplaceDialogStage.setTitle("Find & Replace");
            findAndReplaceDialogStage.setScene(new Scene(findAndReplaceView));
            findAndReplaceDialogStage.setResizable(false);
            findAndReplaceDialogStage.setX(root.getWidth() / 3);
            findAndReplaceDialogStage.setY(root.getHeight() / 3);
            findAndReplaceDialogStage.show();

            // Pass the current code in the text thsst.calvis.editor to FindDialogController
            FindAndReplaceDialogController findAndReplaceDialogController = loader.getController();
            findAndReplaceDialogController.setWorkspaceController(this);
            findAndReplaceDialogController.setDialogStage(findAndReplaceDialogStage);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleFindUp(ActionEvent event) {
        this.fileEditorTabPane.onFindUp();
    }

    @FXML
    public void handleFindDown(ActionEvent event) {
        this.fileEditorTabPane.onFindDown();
    }

    public void onActionFind(HashMap<Integer, int[]> findHighlightRanges) {
        this.fileEditorTabPane.onActionFind(findHighlightRanges);
    }

    public void onActionFindAndReplace(String find, String replace) {
        this.fileEditorTabPane.onActionFindAndReplace(find, replace);
    }

    /**
     * Help Actions
     */
    @FXML
    private void handleHelpAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("De La Salle University: CALVIS x86-32");
        alert.setContentText("Copyright (c) 2016 Jennica Alcalde, Goodwin Chua, Ivan Demabildo, & Marielle Ong\n All rights reserved.");

        alert.showAndWait();
    }

    private TextEditor getActiveEditor() {
        return this.fileEditorTabPane.getActiveFileEditor().getTextEditor();
    }

    /**
     * Creates a boolean property that is bound to another boolean value
     * of the active thsst.calvis.editor.
     */
    private BooleanProperty createActiveBooleanProperty(Function<FileEditorTab, ObservableBooleanValue> func) {
        BooleanProperty bp = new SimpleBooleanProperty();
        FileEditorTab fileEditorTab = this.fileEditorTabPane.getActiveFileEditor();
        if ( fileEditorTab != null )
            bp.bind(func.apply(fileEditorTab));
        this.fileEditorTabPane.activeFileEditorProperty().addListener((observable, oldFileEditor, newFileEditor) -> {
            bp.unbind();
            if ( newFileEditor != null )
                bp.bind(func.apply(newFileEditor));
            else
                bp.set(false);
        });
        return bp;
    }

    public void changeIconToPause() {
        this.btnPlay.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PAUSE));
        this.disableStepMode(true);
    }

    public void changeIconToPlay() {
        this.btnPlay.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PLAY));
        this.disableStepMode(false);
    }

    public void changeIconToHide() {
        this.btnHide.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.COMPRESS));
        this.btnHide.setTooltip(new Tooltip("Hide Tab Pane"));
    }

    public void changeIconToShow() {
        this.btnHide.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EXPAND));
        this.btnHide.setTooltip(new Tooltip("Show Tab Pane"));
    }

    public void disablePlayNextPrevious(boolean flag) {
        disableStepMode(flag);
        this.btnPlay.setDisable(flag);
    }

    public void disableStepMode(boolean flag) {
        this.btnNext.setDisable(flag);
        this.btnPrevious.setDisable(flag);
    }

    public void disableSaveMode(boolean flag) {
        this.btnSave.setDisable(flag);
    }

    public void disableFindButton(boolean flag) {
        this.btnFindUp.setDisable(flag);
        this.btnFindDown.setDisable(flag);
    }

    public void disableCodeArea(boolean flag) {
        this.fileEditorTabPane.disableCodeArea(flag);
    }

    public void formatCodeArea(String codeBlock) {
        this.fileEditorTabPane.formatCode(codeBlock);
    }

    public SystemController getSysCon() {
        return this.sysCon;
    }

    public void buildSystem(ConfiguratorEnvironment env) {
        this.sysCon = new SystemController(env, this);
    }

    public void displayDefaultWindows() {
        try {
            this.showRegisterPane();
            this.showMemoryPane();
            this.showBottomTabPane();
            this.showFileEditorPane();
            this.disableSaveMode(true);
            this.disableFindButton(true);
            this.init();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        this.disableStepMode(true);
    }

    @FXML
    private void handleSaveAll(ActionEvent event) {
        this.fileEditorTabPane.saveAllFileEditors();
    }

    @FXML
    private void handleCloseFile(ActionEvent event) {
        this.fileEditorTabPane.closeFileEditor(this.fileEditorTabPane.getActiveFileEditor(), true);
    }

    @FXML
    private void handleCloseAllFile(ActionEvent event) {
        this.fileEditorTabPane.closeAllFileEditors();
    }
}
