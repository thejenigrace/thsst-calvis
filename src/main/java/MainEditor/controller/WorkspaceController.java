package MainEditor.controller;

import EnvironmentConfiguration.controller.EnvironmentConfigurator;
import MainEditor.model.TextEditor;
import SimulatorVisualizer.controller.SystemController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.scene.control.window.CloseIcon;
import jfxtras.scene.control.window.Window;
import org.fxmisc.richtext.CodeArea;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jennica Alcalde on 10/1/2015.
 */
public class WorkspaceController {
    private SystemController sysCon;

    private HashMap<Integer, int[]> findHighlightRanges;
    private int currentFindRangeIndex;

    private final ReadOnlyObjectWrapper<TextEditor> activeFileEditor = new ReadOnlyObjectWrapper<>();

    @FXML
    private BorderPane root;

    @FXML
    private GridPane gridPaneFind;

    @FXML
    private AnchorPane registerPane;
    @FXML
    private AnchorPane memoryPane;
    @FXML
    private AnchorPane otherWindowsPane;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnPlay;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnPrevious;
    @FXML
    private Button btnFindUp;
    @FXML
    private Button btnFindDown;

    @FXML
    private SplitPane rootSplitPane;
    @FXML
    private SplitPane editorSplitPane;

    @FXML
    private TabPane textEditorTabPane;
    @FXML
    private TabPane otherWindowsTabPane;

    @FXML
    private TextField textFieldFind;

    @FXML
    private ToolBar hideToolbar;

    private void init() {
        // update activeFileEditor property
        textEditorTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            activeFileEditor.set((newTab != null) ? (TextEditor) newTab.getUserData() : null);
        });

        textFieldFind.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Tab tab = (Tab) textEditorTabPane.getSelectionModel().getSelectedItem();
                if (!newValue.isEmpty() && tab != null) {
                    CodeArea codeArea = (CodeArea) tab.getContent();
                    String find_pattern = "\\b(" + newValue + ")\\b";
                    Pattern pattern = Pattern.compile("(?<FIND>" + find_pattern + ")");
                    System.out.println("PATTERN: " + pattern.toString());

                    Matcher matcher = pattern.matcher(codeArea.getText());

                    findHighlightRanges = new HashMap<>();
                    int c = 0;
                    while (matcher.find()) {
                        System.out.println("matcher.group(\"FIND\"): " + matcher.group("FIND"));

                        System.out.println("matcher.end() " + matcher.end());
                        System.out.println("matcher.start() " + matcher.start());

                        int[] arrRange = new int[2];
                        arrRange[0] = matcher.start();
                        arrRange[1] = matcher.end();

                        findHighlightRanges.put(c, arrRange);

                        c++;
                    }

                    if (c > 0) {
                        onActionFind(findHighlightRanges);
                        disableFindButton(false);
                    } else {
                        disableFindButton(true);
                    }
                } else {
                    disableFindButton(true);
                }
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

    private void showMemoryPane() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation((getClass().getResource("/fxml/memory.fxml")));
        TableView memoryView = loader.load();
        SplitPane.setResizableWithParent(memoryView, Boolean.TRUE);
        AnchorPane.setTopAnchor(memoryView, 0.0);
        AnchorPane.setBottomAnchor(memoryView, 0.0);
        AnchorPane.setLeftAnchor(memoryView, 0.0);
        AnchorPane.setRightAnchor(memoryView, 0.0);
        memoryPane.getChildren().add(memoryView);

        // Attach memoryController to SystemController
        MemoryController memoryController = loader.getController();
        this.sysCon.attach(memoryController);
        memoryController.build();
    }

    private void showTextEditorPane() throws Exception {
        this.newFile();

        ConsoleController consoleController = new ConsoleController();
        this.otherWindowsTabPane.getTabs().add(consoleController.getTab());
        this.otherWindowsTabPane.getTabs().add(createErrorLoggerTab(null));
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
		this.otherWindowsTabPane.getTabs().set(1, createErrorLoggerTab(e));
		this.otherWindowsTabPane.getSelectionModel().select(1);
	}

    public void newFile() {
        TextEditor textEditor = new TextEditor(this);
        textEditorTabPane.getTabs().add(textEditor.getTab());
        textEditorTabPane.getSelectionModel().select(textEditor.getTab());
        textEditorTabPane.prefWidthProperty().bind(editorSplitPane.widthProperty());
        textEditorTabPane.prefHeightProperty().bind(editorSplitPane.heightProperty());

        this.sysCon.attach(textEditor);
        textEditor.build();
        this.sysCon.clear();
    }

    @FXML
    private void handleHide(ActionEvent event) {
        this.editorSplitPane.setDividerPositions(1);
    }

    @FXML
    private void handleWindowConsole(ActionEvent event) {
        Window w = initWindowProperties(
                "Console",
                root.getWidth() / 2 - 20,
                root.getHeight() / 2 - 110,
                10,
                root.getHeight() / 2 + 100
        );
        w.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        root.getChildren().add(w);
    }

    @FXML
    private void handleWindowVisualizer(ActionEvent event) throws Exception {
        Window w = initWindowProperties(
                "Visualizer",
                root.getWidth() / 2 - 10,
                root.getHeight() / 2 - 110,
                root.getWidth() / 2,
                root.getHeight() / 2 + 100
        );

        w.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        //root.setBottom(w);
        root.getChildren().add(w);
    }

    /**
     * Action for Play Simulation; a MenuItem in Execute.
     *
     * @param event
     */
    @FXML
    private void handlePlay(ActionEvent event) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();

        if (codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("")) {
            this.sysCon.play(codeArea.getText());
        }
    }

    /**
     * Action for Stop Simulation; a MenuItem in Execute.
     *
     * @param event
     */
    @FXML
    private void handleStop(ActionEvent event) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();

        if (codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("")) {
            this.sysCon.end();
        }
    }

    /**
     * Action for Next Simulation; a MenuItem in Execute.
     *
     * @param event
     */
    @FXML
    private void handleNext(ActionEvent event) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();

        if (codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("")) {
            this.sysCon.next();
        }
    }

    /**
     * Action for Previous Simulation; a MenuItem in Execute.
     *
     * @param event
     */
    @FXML
    private void handlePrevious(ActionEvent event) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();

        if (codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("")) {
            this.sysCon.previous();
        }
    }

    /**
     * Action for Reset Simulation; a MenuItem in Execute.
     *
     * @param event
     */
    @FXML
    private void handleReset(ActionEvent event) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();

        if (codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("")) {
            this.sysCon.reset();
        }
    }

    /**
     * Action for New File; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleNewFile(ActionEvent event) {
//        if (codeArea != null && codeArea.isVisible()) {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Confirmation Dialog");
//            alert.setHeaderText("Do you want to create a new file?");
//            alert.setContentText("Unsaved changes will be lost if you continue.");
//
//            Optional<ButtonType> result = alert.showAndWait();
//            if (result.get() == ButtonType.OK){
//                newFile();
//                fileLocation = "";
//                MainApp.primaryStage.setTitle("CALVIS x86-32 Workspace");
//            } else {
//                // ... user chose CANCEL or closed the dialog
//            }
//        }

        this.newFile();
    }

    /**
     * Action for Open File; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleOpenFile(ActionEvent event) {
        this.newFile();
        Tab tab = textEditorTabPane.getSelectionModel().getSelectedItem();
        TextEditor textEditor = (TextEditor) tab.getUserData();
        CodeArea codeArea = (CodeArea) tab.getContent();

        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CALVIS files (*.calvis)", "*.calvis");
        fileChooser.getExtensionFilters().add(extFilter);
        // Show open file dialog
        File file = fileChooser.showOpenDialog(MainApp.primaryStage);

        if (codeArea != null && file != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Do you want to open " + file.getName() + "?");
            alert.setContentText("Unsaved changes will be lost if you continue.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (file != null) {
//                    newFile();
                    codeArea.replaceText(readFile(file));
                    Path path = Paths.get(file.getAbsolutePath());
                    textEditor.setPath(path);
                    tab.setText(file.getName());
//                    MainApp.primaryStage.setTitle("CALVIS x86-32 Workspace - " + file.getName());
                }
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
    }

    /**
     * Action for Save; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleSaveFile(ActionEvent event) {
        Tab tab = textEditorTabPane.getSelectionModel().getSelectedItem();

        if (tab != null) {
            TextEditor textEditor = (TextEditor) tab.getUserData();
            CodeArea codeArea = (CodeArea) tab.getContent();

            FileChooser fileChooser = new FileChooser();
            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CALVIS files (*.calvis)", "*.calvis");
            fileChooser.getExtensionFilters().add(extFilter);

            if (textEditor.getPath() == null) {
                //Show save file dialog
                File file = fileChooser.showSaveDialog(MainApp.primaryStage);

                if (file != null) {
                    writeFile(codeArea.getText(), file);
//                    MainApp.primaryStage.setTitle("CALVIS x86-32 Workspace - " + file.getName());
                    tab.setText(file.getName());
                    Path path = Paths.get(file.getAbsolutePath());
                    textEditor.setPath(path);
                }
            } else {
                File file = new File(textEditor.getPath().toAbsolutePath().toString());
                writeFile(codeArea.getText(), file);
//                MainApp.primaryStage.setTitle("CALVIS x86-32 Workspace - " + file.getName());
                tab.setText(file.getName());
                Path path = Paths.get(file.getAbsolutePath());
                textEditor.setPath(path);
                disableSaveMode(true);
//                fileLocation = file.getAbsolutePath();
            }


            tab.setGraphic(null);
        }
    }

    /**
     * Action for Save As; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleSaveAsFile(ActionEvent event) {
        Tab tab = textEditorTabPane.getSelectionModel().getSelectedItem();
        TextEditor textEditor = (TextEditor) tab.getUserData();
        CodeArea codeArea = (CodeArea) tab.getContent();

        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CALVIS files (*.calvis)", "*.calvis");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(MainApp.primaryStage);

        if (file != null) {
            writeFile(codeArea.getText(), file);
//            MainApp.primaryStage.setTitle("CALVIS x86-32 Workspace - " + file.getName());
            tab.setText(file.getName());
            Path path = Paths.get(file.getAbsolutePath());
            textEditor.setPath(path);
            disableSaveMode(true);
//            fileLocation = file.getAbsolutePath();
        }
    }

    /**
     * Action for Cut; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleCut(ActionEvent event) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();
        codeArea.cut();
    }

    /**
     * Action for Copy; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleCopy(ActionEvent event) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();
        codeArea.copy();
    }

    /**
     * Action for Paste; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handlePaste(ActionEvent event) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();
        codeArea.paste();
    }

    /**
     * Action for Undo; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleUndo(ActionEvent event) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();
        codeArea.undo();
    }

    /**
     * Action for Redo; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleRedo(ActionEvent event) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();
        codeArea.redo();
    }

    /**
     * Action for Exit; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleExitApp(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleFindAndReplace(ActionEvent event) throws IOException {
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

        // Pass the current code in the text editor to FindDialogController
        FindAndReplaceDialogController findAndReplaceDialogController = loader.getController();
        findAndReplaceDialogController.setWorkspaceController(this);
        findAndReplaceDialogController.setDialogStage(findAndReplaceDialogStage);
    }

    public void onActionFind(HashMap<Integer, int[]> findHighlightRanges) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();
        System.out.println("onActionFind");

        this.findHighlightRanges = findHighlightRanges;
        if (findHighlightRanges.size() != 0) {
            currentFindRangeIndex = 0;
            int[] range = findHighlightRanges.get(0);
            codeArea.selectRange(range[0], range[1]);
        }
    }

    @FXML
    public void handleFindUp(ActionEvent event) {
        Tab tab = (Tab) textEditorTabPane.getSelectionModel().getSelectedItem();

        if (tab != null) {
            CodeArea codeArea = (CodeArea) tab.getContent();
            int[] range;
            if (findHighlightRanges.size() != 0) {
                System.out.println("currentFindRangeIndex: " + currentFindRangeIndex);
                if (currentFindRangeIndex > 0 && currentFindRangeIndex < findHighlightRanges.size()) {
                    currentFindRangeIndex--;
                    System.out.println("u currentFindRangeIndex: " + currentFindRangeIndex);
                    range = findHighlightRanges.get(currentFindRangeIndex);
                    codeArea.selectRange(range[0], range[1]);
                }
            }
        }
    }

    @FXML
    public void handleFindDown(ActionEvent event) {
        Tab tab = (Tab) textEditorTabPane.getSelectionModel().getSelectedItem();

        if (tab != null) {
            CodeArea codeArea = (CodeArea) tab.getContent();
            int[] range;
            if (findHighlightRanges.size() != 0) {
                System.out.println("currentFindRangeIndex: " + currentFindRangeIndex);
                if (currentFindRangeIndex >= 0 && currentFindRangeIndex < findHighlightRanges.size()) {
                    currentFindRangeIndex++;
                    System.out.println("u currentFindRangeIndex: " + currentFindRangeIndex);
                    range = findHighlightRanges.get(currentFindRangeIndex);
                    codeArea.selectRange(range[0], range[1]);
                }
            }
        }
    }

    public void onActionFindAndReplace(String find, String replace) {
        System.out.println("BTW find: " + find);
        System.out.println("BTW replace: " + replace);

        Tab tab = (Tab) textEditorTabPane.getSelectionModel().getSelectedItem();

        if (tab != null) {
            CodeArea codeArea = (CodeArea) tab.getContent();
            String text = codeArea.getText();
            Pattern p = Pattern.compile(find);
            Matcher m = p.matcher(text);

            StringBuffer sb = new StringBuffer();
            int c = 0;
            while (m.find()) {
                m.appendReplacement(sb, replace);
                c++;
            }

            System.out.println("count: " + c);
            m.appendTail(sb);
            System.out.println("sb: " + sb);
            codeArea.replaceText(sb.toString());
        }
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
//        alert.initOwner(getScene().getWindow());

        alert.showAndWait();
    }

    private String readFile(File file) {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String text;
            while ((text = bufferedReader.readLine()) != null) {
                stringBuffer.append(text + "\n");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WorkspaceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Logger.getLogger(WorkspaceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return stringBuffer.toString();
    }

    private void writeFile(String content, File file) {
        try {
            FileWriter fileWriter = null;
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Window initWindowProperties(String title, double width, double height, double x, double y) {
        Window window = new Window(title);
        window.setPrefSize(width, height);
        window.setLayoutX(x);
        window.setLayoutY(y);
        window.getLeftIcons().add(new CloseIcon(window));

        return window;
    }

    public void buildSystem(EnvironmentConfigurator env) {
        this.sysCon = new SystemController(env, this);
    }

    public void displayDefaultWindows() {
        try {
            showRegisterPane();
            showMemoryPane();
            showTextEditorPane();
            disableSaveMode(true);
            disableFindButton(true);
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        disableStepMode(true);
    }

    public void changeIconToPause() {
        btnPlay.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PAUSE));
        disableStepMode(true);
    }

    public void changeIconToPlay() {
        btnPlay.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PLAY));
        disableStepMode(false);
    }

    public void disableStepMode(boolean flag) {
        btnNext.setDisable(flag);
        btnPrevious.setDisable(flag);
    }

    public void disableSaveMode(boolean flag) {
        btnSave.setDisable(flag);
    }

    public void disableFindButton(boolean flag) {
        btnFindUp.setDisable(flag);
        btnFindDown.setDisable(flag);
    }

    public void enableCodeArea(boolean flag) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();
        codeArea.setDisable(!flag);
    }

    public void formatCodeArea(String codeBlock) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();
        String[] arr = this.sysCon.getInstructionKeywords();
        String expression =  String.join("|", arr) ;
        String pat = "[^\\S\\n]+(?=(([a-zA-Z_][a-zA-Z\\d_]*:\\s*)?(" + expression + ")))";
        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(codeBlock);
        String replacedCodeAreaText = matcher.replaceAll("\r\n");
        replacedCodeAreaText = replacedCodeAreaText.replaceAll("\\s*,\\s*", ", ");
        replacedCodeAreaText = replacedCodeAreaText.replaceAll("\\s*:\\s*", ": ");
        codeArea.replaceText(replacedCodeAreaText);
        codeArea.redo();
    }
}
