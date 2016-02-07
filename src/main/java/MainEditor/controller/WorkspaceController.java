package MainEditor.controller;

import EnvironmentConfiguration.controller.EnvironmentConfigurator;
import MainEditor.model.TextEditor;
import SimulatorVisualizer.controller.SystemController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import jfxtras.scene.control.window.CloseIcon;
import jfxtras.scene.control.window.Window;
import org.controlsfx.glyphfont.Glyph;
import org.fxmisc.richtext.CodeArea;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jennica Alcalde on 10/1/2015.
 */
public class WorkspaceController {

    private HashMap<Integer, int[]> findHighlightRanges;
    private int currentFindRangeIndex;

    private SystemController sysCon;
//	private CodeArea codeArea;

    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static String[] KEYWORDS;
    private static String KEYWORD_PATTERN;
    private static Pattern PATTERN;

    private String fileLocation = "";

    @FXML
	private Button btnPlay;
	@FXML
	private Button btnNext;
	@FXML
	private Button btnPrevious;

    @FXML
    private BorderPane root;

    @FXML
    private SplitPane wholeSplitPane;
	@FXML
    private AnchorPane registerPane;
    @FXML
    private SplitPane editorSplitPane;
    @FXML
    private AnchorPane memoryPane;

    @FXML
    private AnchorPane otherWindowsPane;
    @FXML
    private TabPane textEditorTabPane;
    @FXML
    private ToolBar hideToolbar;

    private void showRegisterPane() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/registers.fxml"));
        SplitPane registersView = loader.load();
        registerPane.getChildren().add(registersView);
		registersView.prefWidthProperty().bind(registerPane.widthProperty());
		registersView.prefHeightProperty().bind(registerPane.heightProperty());

        // Attach registersController to SystemController
        RegistersController registersController = loader.getController();
        this.sysCon.attach(registersController);
        registersController.build();
    }

    private void showMemoryPane() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation((getClass().getResource("/fxml/memory.fxml")));
        StackPane memoryView = loader.load();
        memoryPane.getChildren().add(memoryView);
        memoryView.prefWidthProperty().bind(memoryPane.widthProperty());
        memoryView.prefHeightProperty().bind(memoryPane.heightProperty());

        // Attach registersController to SystemController
        MemoryController memoryController = loader.getController();
        this.sysCon.attach(memoryController);
        memoryController.build();
    }


    private void showTextEditorPane() throws Exception {
        this.newTextEditorTab();
    }

    private void newTextEditorTab() {
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
    private void handleTextEditorWindow(ActionEvent event) {
        newFile();
    }

	@FXML
	private void handleRegistersWindow(ActionEvent event) throws Exception {
		Window w = initWindowProperties(
				"Registers",
				root.getWidth()/3.5-20,
				root.getHeight()/2+10,
				root.getWidth()/3+10,
				80
		);

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/registers.fxml"));
		Parent registersView = (SplitPane) loader.load();

		w.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		w.getContentPane().getChildren().add(registersView);

		root.setLeft(w);
		//root.getChildren().add(w);

		// Attach registersController to SystemController
		RegistersController registersController = loader.getController();
		this.sysCon.attach(registersController);
		registersController.build();
	}

	@FXML
	private void handleMemoryWindow(ActionEvent event) throws Exception {
		Window w = initWindowProperties(
				"Memory",
				root.getWidth()/4-20,
				root.getHeight()/2+10,
				root.getWidth()-root.getWidth()/3,
				80
		);

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation((getClass().getResource("/fxml/memory.fxml")));
		Parent memoryView = loader.load();

		w.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		w.getContentPane().getChildren().add(memoryView);

		root.setRight(w);
		//root.getChildren().add(w);

		// Attach registersController to SystemController
		MemoryController memoryController = loader.getController();
		this.sysCon.attach(memoryController);
		memoryController.build();
	}

	@FXML
	private void handleVisualizerWindow(ActionEvent event) throws Exception {
		Window w = initWindowProperties(
				"Visualizer",
				root.getWidth()/2-10,
				root.getHeight()/2-110,
				root.getWidth()/2,
				root.getHeight()/2+100
		);

		w.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

		//root.setBottom(w);
		root.getChildren().add(w);
	}

	@FXML
	private void handleConsoleWindow(ActionEvent event) {
		Window w = initWindowProperties(
				"Console",
				root.getWidth()/2-20,
				root.getHeight()/2-110,
				10,
				root.getHeight()/2+100
		);
		w.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
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

        if (codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("") ) {
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

		if (codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("") ) {
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

		if (codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("") ) {
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

		if (codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("") ) {
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

		if (codeArea != null && codeArea.isVisible() && !codeArea.getText().trim().equals("") ) {
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

        this.newTextEditorTab();
    }

	/**
	 * Action for Open File; a MenuItem in File.
	 *
	 * @param event
	 */
	@FXML
	private void handleOpenFile(ActionEvent event) {
//		FileChooser fileChooser = new FileChooser();
//
//        // Set extension filter
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CALVIS files (*.calvis)", "*.calvis");
//        fileChooser.getExtensionFilters().add(extFilter);
//
//		// Show open file dialog
//		File file = fileChooser.showOpenDialog(MainApp.primaryStage);
//
//        if (codeArea != null && codeArea.isVisible() && file != null) {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Confirmation Dialog");
//            alert.setHeaderText("Do you want to open " + file.getName() + "?");
//            alert.setContentText("Unsaved changes will be lost if you continue.");
//
//            Optional<ButtonType> result = alert.showAndWait();
//            if (result.get() == ButtonType.OK){
//                if(file != null) {
//                    newFile();
//                    codeArea.replaceText(readFile(file));
//                    fileLocation = file.getAbsolutePath();
//                    MainApp.primaryStage.setTitle("CALVIS x86-32 Workspace - " + file.getName());
//                }
//            } else {
//                // ... user chose CANCEL or closed the dialog
//            }
//        }
    }

	/**
	 * Action for Save; a MenuItem in File.
	 *
	 * @param event
	 */
	@FXML
	private void handleSaveFile(ActionEvent event) {
//		FileChooser fileChooser = new FileChooser();
//
//        //Set extension filter
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CALVIS files (*.calvis)", "*.calvis");
//        fileChooser.getExtensionFilters().add(extFilter);
//
//        if( fileLocation.equals("") ) {
//            //Show save file dialog
//            File file = fileChooser.showSaveDialog(MainApp.primaryStage);
//
//            if(file != null) {
//                writeFile(codeArea.getText(), file);
//                MainApp.primaryStage.setTitle("CALVIS x86-32 Workspace - " + file.getName());
//                fileLocation = file.getAbsolutePath();
//            }
//        }
//        else {
//            File file = new File(fileLocation);
//            writeFile(codeArea.getText(), file);
//            MainApp.primaryStage.setTitle("CALVIS x86-32 Workspace - " + file.getName());
//            fileLocation = file.getAbsolutePath();
//        }
    }

	/**
	 * Action for Save As; a MenuItem in File.
	 *
	 * @param event
	 */
	@FXML
	private void handleSaveAsFile(ActionEvent event) {
//		FileChooser fileChooser = new FileChooser();
//
//        //Set extension filter
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CALVIS files (*.calvis)", "*.calvis");
//        fileChooser.getExtensionFilters().add(extFilter);
//
//		//Show save file dialog
//		File file = fileChooser.showSaveDialog(MainApp.primaryStage);
//
//        if(file != null) {
//            writeFile(codeArea.getText(), file);
//            MainApp.primaryStage.setTitle("CALVIS x86-32 Workspace - " + file.getName());
//            fileLocation = file.getAbsolutePath();
//        }
    }

	/**
	 * Action for Cut; a MenuItem in File.
	 * @param event
	 */
	@FXML
	private void handleCut(ActionEvent event) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();
		codeArea.cut();
	}

	/**
	 * Action for Copy; a MenuItem in File.
	 * @param event
	 */
	@FXML
	private void handleCopy(ActionEvent event) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();
        codeArea.copy();
	}

	/**
	 * Action for Paste; a MenuItem in File.
	 * @param event
	 */
	@FXML
	private void handlePaste(ActionEvent event) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();
        codeArea.paste();
	}

	/**
	 * Action for Undo; a MenuItem in File.
	 * @param event
	 */
	@FXML
	private void handleUndo(ActionEvent event) {
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();
		codeArea.undo();
    }

	/**
	 * Action for Redo; a MenuItem in File.
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
	private void handleFind(ActionEvent event) throws IOException {
		// Load root layout from fxml file
//		FXMLLoader loader = new FXMLLoader();
//		loader.setLocation(getClass().getResource("/fxml/find.fxml"));
//		Parent findView = (BorderPane) loader.load();
//
//		Stage findDialogStage = new Stage();
//		findDialogStage.initModality(Modality.APPLICATION_MODAL);
//		findDialogStage.setTitle("Find");
//		findDialogStage.setScene(new Scene(findView));
//		findDialogStage.setResizable(false);
//		findDialogStage.setX(root.getWidth() / 3);
//		findDialogStage.setY(root.getHeight() / 3);
//		findDialogStage.show();
//
//		// Pass the current code in the text editor to FindDialogController
//		FindDialogController findDialogController = loader.getController();
//		findDialogController.setWorkspaceController(this);
//		findDialogController.setDialogStage(findDialogStage);
//		findDialogController.setCode(codeArea.getText());
	}

	@FXML
	private void handleFindAndReplace(ActionEvent event) throws IOException {
		// Load root layout from fxml file
//		FXMLLoader loader = new FXMLLoader();
//		loader.setLocation(getClass().getResource("/fxml/find_and_replace.fxml"));
//		Parent findAndReplaceView = (BorderPane) loader.load();
//
//		Stage findAndReplaceDialogStage = new Stage();
//		findAndReplaceDialogStage.initModality(Modality.APPLICATION_MODAL);
//		findAndReplaceDialogStage.setTitle("Find & Replace");
//		findAndReplaceDialogStage.setScene(new Scene(findAndReplaceView));
//		findAndReplaceDialogStage.setResizable(false);
//		findAndReplaceDialogStage.setX(root.getWidth() / 3);
//		findAndReplaceDialogStage.setY(root.getHeight() / 3);
//		findAndReplaceDialogStage.show();
//
//		// Pass the current code in the text editor to FindDialogController
//		FindAndReplaceDialogController findAndReplaceDialogController = loader.getController();
//		findAndReplaceDialogController.setWorkspaceController(this);
//		findAndReplaceDialogController.setDialogStage(findAndReplaceDialogStage);
	}

    public void buildSystem(EnvironmentConfigurator env){
        this.sysCon = new SystemController(env, this);
    }

    public void displayDefaultWindows(){
        try {
            showRegisterPane();
            showMemoryPane();
            showTextEditorPane();
        } catch (Exception e){
            e.printStackTrace();
        }
	    disableStepMode(true);
    }

    //create text editor window
    public void newFile() {
        Window w = initWindowProperties(
                "Text Editor",
                root.getWidth()/2-10,
                root.getHeight()/2+10,
                10,
                80
        );

        // add the window to the canvas
        root.setCenter(w);
        //root.getChildren().add(w);
    }

    public void changeIconToPause(){
		btnPlay.setGraphic(new Glyph("FontAwesome", "PAUSE"));
	    disableStepMode(true);
    }

    public void changeIconToPlay(){
        btnPlay.setGraphic(new Glyph("FontAwesome", "PLAY"));
	    disableStepMode(false);
    }

	public void disableStepMode(boolean flag){
		btnNext.setDisable(flag);
		btnPrevious.setDisable(flag);
	}

	public void enableCodeArea(boolean flag){
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();
		codeArea.setDisable(!flag);
	}

    public void formatCodeArea(String codeBlock){
        CodeArea codeArea = (CodeArea) textEditorTabPane.getSelectionModel().getSelectedItem().getContent();
	    String[] arr = this.sysCon.getInstructionKeywords();
	    String expression =  String.join("|", arr) ;
	    String pat = "[^\\S\\n]+(?=(" + expression + "))";
        Pattern pattern = Pattern.compile(pat);
	    Matcher matcher = pattern.matcher(codeBlock);
	    String replacedCodeAreaText = matcher.replaceAll("\n");
	    replacedCodeAreaText = replacedCodeAreaText.replaceAll("\\s*,\\s*", ", ");
	    codeArea.replaceText(replacedCodeAreaText);
	    codeArea.redo();
    }

    public void onActionFind(HashMap<Integer, int[]> findHighlightRanges) {
//        System.out.println("onActionFind");
//
//        this.findHighlightRanges = findHighlightRanges;
//        if (findHighlightRanges.size() != 0) {
//            currentFindRangeIndex = 0;
//            int[] range = findHighlightRanges.get(0);
//            codeArea.selectRange(range[0], range[1]);
//        }
    }

    public void onActionUp() {
//        int[] range;
//        if(findHighlightRanges.size() != 0) {
//            System.out.println("currentFindRangeIndex: " + currentFindRangeIndex);
//            if(currentFindRangeIndex >= 0 && currentFindRangeIndex < findHighlightRanges.size()) {
//                currentFindRangeIndex++;
//                System.out.println("u currentFindRangeIndex: " + currentFindRangeIndex);
//                range = findHighlightRanges.get(currentFindRangeIndex);
//                codeArea.selectRange(range[0], range[1]);
//            }
//        }
    }

    public void onActionDown() {
//        int[] range;
//        if(findHighlightRanges.size() != 0) {
//            System.out.println("currentFindRangeIndex: " + currentFindRangeIndex);
//            if(currentFindRangeIndex > 0 && currentFindRangeIndex < findHighlightRanges.size()) {
//                currentFindRangeIndex--;
//                System.out.println("u currentFindRangeIndex: " + currentFindRangeIndex);
//                range = findHighlightRanges.get(currentFindRangeIndex);
//                codeArea.selectRange(range[0], range[1]);
//            }
//        }
    }

    public void onActionFindAndReplace(String find, String replace) {
//        System.out.println("BTW find: " + find);
//        System.out.println("BTW replace: " + replace);
//
//        String text = codeArea.getText();
//        Pattern p = Pattern.compile(find);
//        Matcher m = p.matcher(text);
//
//        StringBuffer sb = new StringBuffer();
//        int c = 0;
//        while (m.find()) {
//            m.appendReplacement(sb, replace);
//            c++;
//        }
//
//        System.out.println("count: " + c);
//        m.appendTail(sb);
//        System.out.println("sb: " + sb);
//        codeArea.replaceText(sb.toString());
    }

	private String readFile(File file){
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
//        System.out.println("root width = " + root.getWidth());
//        System.out.println("root height = " + root.getHeight());
		Window window = new Window(title);
		window.setPrefSize(width, height);
		window.setLayoutX(x);
		window.setLayoutY(y);
		window.getLeftIcons().add(new CloseIcon(window));

		return window;
	}
}
