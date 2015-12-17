package MainEditor.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import jfxtras.scene.control.window.CloseIcon;
import jfxtras.scene.control.window.Window;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jennica Alcalde on 10/1/2015.
 */
public class WorkspaceController {

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

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */ assert styleClass != null;
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
    private void handleTextEditorWindow(ActionEvent event) {
        Window w = initWindowProperties(
                "Text Editor",
                root.getWidth()/3-10,
                root.getHeight()/2+10,
                10,
                80
        );

        // add some content
        executor = Executors.newSingleThreadExecutor();
        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.richChanges().subscribe(change -> {
            codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
        });
        w.getContentPane().getChildren().add(codeArea);

        // add the window to the canvas
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

    @FXML
    private void handleRegistersWindow(ActionEvent event) throws Exception {
        Window w = initWindowProperties(
                "Registers",
                root.getWidth()/3-20,
                root.getHeight()/2+10,
                root.getWidth()/3+10,
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
                root.getWidth()/3-20,
                root.getHeight()/2+10,
                root.getWidth()-root.getWidth()/3,
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
                root.getWidth()/2-10,
                root.getHeight()/2-110,
                root.getWidth()/2,
                root.getHeight()/2+100
        );

        w.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        root.getChildren().add(w);
    }

    /**
     * Action for New File; a MenuItem in File.
     * @param event
     */
    @FXML
    private void handleNewFile(ActionEvent event) {

    }

    /**
     * Action for Open File; a MenuItem in File.
     * @param event
     */
    @FXML
    private void handleOpenFile(ActionEvent event) {

    }

    /**
     * Action for Save; a MenuItem in File.
     * @param event
     */
    @FXML
    private void handleSaveFile(ActionEvent event) {

    }

    /**
     * Action for Save As; a MenuItem in File.
     * @param event
     */
    @FXML
    private void handleSaveAsFile(ActionEvent event) {

    }

    /**
     * Action for Exit; a MenuItem in File.
     * @param event
     */
    @FXML
    private void handleExitApp(ActionEvent event) {
        System.exit(0);
    }
}
