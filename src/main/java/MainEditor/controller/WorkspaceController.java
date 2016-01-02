package MainEditor.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.scene.control.window.CloseIcon;
import jfxtras.scene.control.window.Window;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jennica Alcalde on 10/1/2015.
 */
public class WorkspaceController implements Initializable {

    private HashMap<Integer, int[]> findHighlightRanges;
    private int currentFindRangeIndex;

    private CodeArea codeArea;
    private ExecutorService executor;

    private static final String[] KEYWORDS = new String[]{
            "mov", "lea"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {

//            System.out.println("matcher.group(): " + matcher.group("KEYWORD"));

            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
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

    @FXML
    private BorderPane root;

    @FXML
    public void handleTextEditorWindow(ActionEvent event) {
        Window w = initWindowProperties(
                "Text Editor",
                root.getWidth() / 3 - 10,
                root.getHeight() / 2 + 10,
                10,
                80
        );

        // add some content
        executor = Executors.newSingleThreadExecutor();
        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.richChanges().subscribe(change -> {
            codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));

//            find(codeArea.getText());
        });

        w.getContentPane().getChildren().add(codeArea);

        // add the window to the canvas
        root.getChildren().add(w);
    }

    private void find(String code) {
        System.out.println(code);
    }

    @FXML
    private void handleConsoleWindow(ActionEvent event) {
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
    private void handleRegistersWindow(ActionEvent event) throws Exception {
        Window w = initWindowProperties(
                "Registers",
                root.getWidth() / 3 - 20,
                root.getHeight() / 2 + 10,
                root.getWidth() / 3 + 10,
                80
        );

        SplitPane registersView = FXMLLoader.load(getClass().getResource("/fxml/registers.fxml"));
        w.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        w.getContentPane().getChildren().add(registersView);

        root.getChildren().add(w);
    }

    @FXML
    private void handleMemoryWindow(ActionEvent event) throws Exception {
        Window w = initWindowProperties(
                "Memory",
                root.getWidth() / 3 - 20,
                root.getHeight() / 2 + 10,
                root.getWidth() - root.getWidth() / 3,
                80
        );

        ScrollPane memoryView = FXMLLoader.load(getClass().getResource("/fxml/memory.fxml"));
        w.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        w.getContentPane().getChildren().add(memoryView);

        root.getChildren().add(w);
    }

    @FXML
    private void handleVisualizerWindow(ActionEvent event) throws Exception {
        Window w = initWindowProperties(
                "Visualizer",
                root.getWidth() / 2 - 10,
                root.getHeight() / 2 - 110,
                root.getWidth() / 2,
                root.getHeight() / 2 + 100
        );

        w.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        root.getChildren().add(w);
    }

    /**
     * Action for New File; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleNewFile(ActionEvent event) {

    }

    /**
     * Action for Open File; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleOpenFile(ActionEvent event) {

    }

    /**
     * Action for Save; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleSaveFile(ActionEvent event) {

    }

    /**
     * Action for Save As; a MenuItem in File.
     *
     * @param event
     */
    @FXML
    private void handleSaveAsFile(ActionEvent event) {

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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/find.fxml"));
        Parent findView = (BorderPane) loader.load();

        Stage findDialogStage = new Stage();
        findDialogStage.initModality(Modality.APPLICATION_MODAL);
        findDialogStage.setTitle("Find");
        findDialogStage.setScene(new Scene(findView));
        findDialogStage.setResizable(false);
        findDialogStage.setX(root.getWidth() / 3);
        findDialogStage.setY(root.getHeight() / 3);
        findDialogStage.show();

        // Pass the current code in the text editor to FindDialogController
        FindDialogController findDialogController = loader.getController();
        findDialogController.setWorkspaceController(this);
        findDialogController.setDialogStage(findDialogStage);
        findDialogController.setCode(codeArea.getText());
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
        System.out.println("onActionFind");

        this.findHighlightRanges = findHighlightRanges;
        if (findHighlightRanges.size() != 0) {
            currentFindRangeIndex = 0;
            int[] range = findHighlightRanges.get(0);
            codeArea.selectRange(range[0], range[1]);
        }
    }

    public void onActionUp() {
        int[] range;
        if(findHighlightRanges.size() != 0) {
            System.out.println("currentFindRangeIndex: " + currentFindRangeIndex);
            if(currentFindRangeIndex >= 0 && currentFindRangeIndex < findHighlightRanges.size()) {
                currentFindRangeIndex++;
                System.out.println("u currentFindRangeIndex: " + currentFindRangeIndex);
                range = findHighlightRanges.get(currentFindRangeIndex);
                codeArea.selectRange(range[0], range[1]);
            }
        }
    }

    public  void onActionDown() {
        int[] range;
        if(findHighlightRanges.size() != 0) {
            System.out.println("currentFindRangeIndex: " + currentFindRangeIndex);
            if(currentFindRangeIndex > 0 && currentFindRangeIndex < findHighlightRanges.size()) {
                currentFindRangeIndex--;
                System.out.println("u currentFindRangeIndex: " + currentFindRangeIndex);
                range = findHighlightRanges.get(currentFindRangeIndex);
                codeArea.selectRange(range[0], range[1]);
            }
        }
    }

    public void onActionFindAndReplace(String find, String replace) {
        System.out.println("BTW find: " + find);
        System.out.println("BTW replace: " + replace);

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
