package thsst.calvis.editor.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import thsst.calvis.configuration.controller.ConfiguratorEnvironment;
import thsst.calvis.editor.view.FileEditorTab;
import thsst.calvis.editor.view.FileEditorTabPane;
import thsst.calvis.editor.view.TextEditor;
import thsst.calvis.simulatorvisualizer.controller.SystemController;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * Created by Jennica Alcalde on 10/1/2015.
 */
public class WorkspaceController implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private AnchorPane anchorPaneRegister;
    @FXML
    private AnchorPane anchorPaneMemory;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnUndo;
    @FXML
    private Button btnRedo;
    @FXML
    private Button btnPlayStepMode;
    @FXML
    private Button btnPlay;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnPrevious;
    @FXML
    private Button btnFindMoveUpward;
    @FXML
    private Button btnFindMoveDownward;
    @FXML
    private Button btnHide;

    @FXML
    private SplitPane splitPaneFileEditor;

    @FXML
    private TabPane tabPaneBottom;

    @FXML
    private ToolBar toolbarMain;

    @FXML
    private VBox vBoxFileEditor;

    private TextField textFieldFind;
    private boolean isHideTabPaneBottom = false;

    private FileEditorTabPane fileEditorTabPane;

    private SystemController sysCon;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize find text field
        this.textFieldFind = TextFields.createClearableTextField();
        this.textFieldFind.setPrefWidth(250.0);
        this.toolbarMain.getItems().add(21, textFieldFind);
    }

    private void initBinding() {
        this.btnSave.disableProperty().bind(createActiveBooleanProperty(FileEditorTab::modifiedProperty).not());
        this.btnUndo.disableProperty().bind(createActiveBooleanProperty(FileEditorTab::canUndoProperty).not());
        this.btnRedo.disableProperty().bind(createActiveBooleanProperty(FileEditorTab::canRedoProperty).not());

        this.textFieldFind.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                fileEditorTabPane.onActionFind(newValue);
            }
        });
    }

    private void showRegisterPane() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/registers.fxml"));
        SplitPane registersView = loader.load();
        SplitPane.setResizableWithParent(registersView, Boolean.TRUE);
        AnchorPane.setTopAnchor(registersView, 0.0);
        AnchorPane.setBottomAnchor(registersView, 0.0);
        AnchorPane.setLeftAnchor(registersView, 0.0);
        AnchorPane.setRightAnchor(registersView, 0.0);

        this.anchorPaneRegister.getChildren().add(registersView);

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

            this.anchorPaneMemory.getChildren().add(memoryView);

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
        this.tabPaneBottom.getTabs().add(consoleController.getTab());
        this.tabPaneBottom.getTabs().add(createErrorLoggerTab(null));
        this.sysCon.attach(consoleController);
        consoleController.build();

        VisualizationController visualizationController = new VisualizationController();
        this.tabPaneBottom.getTabs().add(visualizationController.getTab());
        this.sysCon.attach(visualizationController);
        visualizationController.build();
    }

    @FXML
    private void handleConsole(ActionEvent event) {
        this.tabPaneBottom.getSelectionModel().select(0);
    }

    @FXML
    private void handleErrorLogger(ActionEvent event) {
        this.tabPaneBottom.getSelectionModel().select(1);
    }

    @FXML
    private void handleVisualizer(ActionEvent event) {
        this.tabPaneBottom.getSelectionModel().select(2);
    }

    @FXML
    private void handleHide(ActionEvent event) {
        if ( !this.isHideTabPaneBottom ) {
            this.isHideTabPaneBottom = true;
            this.splitPaneFileEditor.setDividerPositions(1);
            this.changeIconToShow();
        } else {
            this.isHideTabPaneBottom = false;
            this.splitPaneFileEditor.setDividerPositions(0.65);
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
        this.tabPaneBottom.getTabs().set(1, createErrorLoggerTab(e));
        this.tabPaneBottom.getSelectionModel().select(1);
    }

    private void showFileEditorTabPane() {
        this.fileEditorTabPane = new FileEditorTabPane(this, this.splitPaneFileEditor.widthProperty(), this.splitPaneFileEditor.heightProperty());
        this.vBoxFileEditor.getChildren().add(0, this.fileEditorTabPane.getTabPane());
        this.fileEditorTabPane.newFileEditor();
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

    @FXML
    private void handleSaveAllFile(ActionEvent event) {
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
    private void handleExitApp(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleUndo(ActionEvent event) {
        this.getActiveEditor().undo();
    }

    @FXML
    private void handleRedo(ActionEvent event) {
        this.getActiveEditor().redo();
    }

    @FXML
    private void handleCut(ActionEvent event) {
        this.getActiveEditor().cut();
    }

    @FXML
    private void handleCopy(ActionEvent event) {
        this.getActiveEditor().copy();
    }

    @FXML
    private void handlePaste(ActionEvent event) {
        this.getActiveEditor().paste();
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
            findAndReplaceDialogStage.setX(this.root.getWidth() / 3);
            findAndReplaceDialogStage.setY(this.root.getHeight() / 3);
            findAndReplaceDialogStage.show();

            // Pass the current code in the text editor to FindDialogController
            FindAndReplaceDialogController findAndReplaceDialogController = loader.getController();
            findAndReplaceDialogController.setWorkspaceController(this);
            findAndReplaceDialogController.setDialogStage(findAndReplaceDialogStage);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public void onActionFindAndReplace(String find, String replace) {
        this.fileEditorTabPane.onActionFindAndReplace(find, replace);
    }

    @FXML
    public void handleFindUpward(ActionEvent event) {
        this.fileEditorTabPane.onActionFindMoveUpward();
    }

    @FXML
    public void handleFindDownward(ActionEvent event) {
        this.fileEditorTabPane.onActionFindMoveDownward();
    }

    /**
     * Action for Play Simulation; a MenuItem in Execute.
     *
     * @param event
     */
    @FXML
    private void handlePlay(ActionEvent event) {
        this.fileEditorTabPane.simulationAction("PLAY");
    }

    @FXML
    private void handlePause(ActionEvent event) {
        this.fileEditorTabPane.simulationAction("PAUSE");
    }

    /**
     * Action for Stop Simulation; a MenuItem in Execute.
     *
     * @param event
     */
    @FXML
    private void handleStop(ActionEvent event) {
        this.fileEditorTabPane.simulationAction("STOP");
    }

    /**
     * Action for Previous Simulation; a MenuItem in Execute.
     *
     * @param event
     */
    @FXML
    private void handlePrevious(ActionEvent event) {
        this.fileEditorTabPane.simulationAction("PREVIOUS");
    }

    /**
     * Action for Next Simulation; a MenuItem in Execute.
     *
     * @param event
     */
    @FXML
    private void handleNext(ActionEvent event) {
        this.fileEditorTabPane.simulationAction("NEXT");
    }

    /**
     * Action for Reset Simulation; a MenuItem in Execute.
     *
     * @param event
     */
    @FXML
    private void handleReset(ActionEvent event) {
        this.fileEditorTabPane.simulationAction("RESET");
    }

    @FXML
    private void handleConverter(ActionEvent event) {
        try {
            // Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/converter.fxml"));
            Parent converterView = (BorderPane) loader.load();

            Stage converterStage = new Stage();
            converterStage.initModality(Modality.WINDOW_MODAL);
            converterStage.setTitle("Converter Calculator");
            converterStage.setScene(new Scene(converterView));
            converterStage.setResizable(false);
            converterStage.setX(this.root.getWidth() / 3);
            converterStage.setY(this.root.getHeight() / 3);
            converterStage.show();

            // Pass the current code in the text editor to ConverterController
            ConverterController converterController = loader.getController();
            converterController.setWorkspaceController(this);
            converterController.setDialogStage(converterStage);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHelpAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("De La Salle University: CALVIS x86-32");
        alert.setContentText("Copyright (c) 2016 Jennica Alcalde, Goodwin Chua, Ivan Demabildo, & Marielle Ong\n All rights reserved.");

        alert.showAndWait();
    }


    /**
     * MARK -- Helper Methods --
     */
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
        this.btnFindMoveUpward.setDisable(flag);
        this.btnFindMoveDownward.setDisable(flag);
    }

    public void disableCodeArea(boolean flag) {
        this.fileEditorTabPane.disableCodeArea(flag);
    }

    public void formatCodeArea(String codeBlock) {
        this.fileEditorTabPane.formatCode(codeBlock);
    }

    private TextEditor getActiveEditor() {
        return this.fileEditorTabPane.getActiveFileEditor().getTextEditor();
    }

    /**
     * Creates a boolean property that is bound to another boolean value
     * of the active editor.
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

    public SystemController getSysCon() {
        return this.sysCon;
    }

    public void buildSystem(ConfiguratorEnvironment configEnv) {
        this.sysCon = new SystemController(configEnv, this);
    }

    public void displayDefaultWindows() {
        try {
            this.showRegisterPane();
            this.showMemoryPane();
            this.showBottomTabPane();
            this.showFileEditorTabPane();
            this.disableSaveMode(true);
            this.disableFindButton(true);
            this.initBinding();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        this.disableStepMode(true);
    }
}
