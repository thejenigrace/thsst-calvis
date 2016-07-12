package editor.model;

import configuration.model.engine.CalvisFormattedInstruction;
import editor.MainApp;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import org.fxmisc.richtext.*;
import org.fxmisc.undo.UndoManager;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jennica on 04/02/2016.
 */
public class TextEditorPane extends AssemblyComponent {

    private CodeArea codeArea;
    private Pattern lineByLinePattern;

    // The existing autocomplete entries.
    private SortedSet<String> entries;
    // The popup used to select an entry.
    private ContextMenu entriesPopup;

    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = ";[^\n]*";
    private static String[] INSTRUCTION_KEYWORDS;
    private static String[] REGISTER_KEYWORDS;
    private static String[] MEMORY_KEYWORDS;
    private static String INSTRUCTION_PATTERN;
    private static String REGISTER_PATTERN;
    private static String MEMORY_PATTERN;
    private static Pattern PATTERN;

    public TextEditorPane() {
        this.codeArea = new CodeArea();
        codeArea.setStyle("-fx-highlight-fill: lightgray; -fx-font-size: 14px;");
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.richChanges().subscribe(change -> {
            codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
        });
        codeArea.setOnKeyReleased(event -> {
            try {
                String text = codeArea.getText();
                int caretPosition = codeArea.getCaretPosition();
                int caretColumn = codeArea.getCaretColumn();
                System.out.println("caretPosition = " + caretPosition);
                System.out.println("caretColumn = " + caretColumn);

                text = text.replaceAll("[\\t\\n\\r\\f\\v]", " ");

                int wordCount = 0;
                for ( int i = caretPosition - 1; i > 0; i-- ) {

                    if ( text.substring(i, caretPosition - wordCount).equals(" ") )
                        break;
                    wordCount++;
                }

                int afterEnter = 0;
                for ( int i = caretPosition - 1; i > 0; i-- ) {
                    if ( codeArea.getText(i, caretPosition - afterEnter).equals("\n") )
                        break;
                    afterEnter++;
                }

                if ( !event.getCode().equals(KeyCode.UP) && !event.getCode().equals(KeyCode.DOWN) &&
                        !event.getCode().equals(KeyCode.LEFT) && !event.getCode().equals(KeyCode.RIGHT)
                        && !event.getCode().equals(KeyCode.ENTER) && !event.getCode().equals(KeyCode.TAB) ) {
                    // Check if the current character is whitespace; start autocomplete after the whitespace

//                    System.out.println("wordCount = " + wordCount);
//
//                    System.out.println("text = " + text.substring(caretPosition-wordCount-1, caretPosition));

//                    if ( caretPosition > 0 && codeArea.getText(caretPosition - wordCount - 1, caretPosition - wordCount).equals(" ") )
//                        this.autocomplete(codeArea.getText(caretPosition - wordCount, caretPosition), caretPosition - wordCount, caretPosition);
//                    else if( caretPosition > 0 && codeArea.getText(caretPosition - afterEnter - 1, caretPosition - afterEnter).equals("\n") )
//                        this.autocomplete(codeArea.getText(caretPosition - afterEnter, caretPosition), caretPosition - wordCount, caretPosition);
                    if( caretPosition > 1 && text.substring(caretPosition-wordCount-1, caretPosition-wordCount).equals(" "))
                        this.autocomplete(text.substring(caretPosition-wordCount, caretPosition), caretPosition-wordCount, caretPosition);
                    else
                        this.autocomplete(codeArea.getText(0, caretPosition), 0, caretPosition);
                }

//                } else if ( event.getCode().equals(KeyCode.ENTER) ) {
//                    System.out.println("catch enter");
//                    System.out.println(caretPosition-afterEnter);
//                    this.autocomplete(codeArea.getText(caretPosition-afterEnter, caretPosition), caretPosition-1, caretPosition);
//                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        });
    }

    // 'path' property
    private final ObjectProperty<Path> path = new SimpleObjectProperty<>();

    public Path getPath() {
        return path.get();
    }

    public void setPath(Path path) {
        this.path.set(path);
    }

    public ObjectProperty<Path> pathProperty() {
        return path;
    }

    /**
     * Method for configuring the highlighted keywords within the text editor
     */
    private void setCodeEnvironment() {
        INSTRUCTION_KEYWORDS = this.sysCon.getKeywordBuilder().getInstructionKeywords();
        INSTRUCTION_PATTERN = "\\b(" + String.join("|", INSTRUCTION_KEYWORDS) + ")\\b";
        REGISTER_KEYWORDS = this.sysCon.getKeywordBuilder().getRegisterKeywords();
        REGISTER_PATTERN = "\\b(" + String.join("|", REGISTER_KEYWORDS) + ")\\b";
        MEMORY_KEYWORDS = this.sysCon.getKeywordBuilder().getMemoryKeywords();
        MEMORY_PATTERN = "\\b(" + String.join("|", MEMORY_KEYWORDS) + ")\\b";
        PATTERN = Pattern.compile(
                "(?<INSTRUCTIONPATTERN>" + INSTRUCTION_PATTERN + ")"
                        + "|(?<REGISTERPATTERN>" + REGISTER_PATTERN + ")"
                        + "|(?<MEMORYPATTERN>" + MEMORY_PATTERN + ")"
                        + "|(?<PAREN>" + PAREN_PATTERN + ")"
                        + "|(?<BRACE>" + BRACE_PATTERN + ")"
                        + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                        + "|(?<STRING>" + STRING_PATTERN + ")"
                        + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
        );
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while ( matcher.find() ) {
            String styleClass = matcher.group("INSTRUCTIONPATTERN") != null ? "instruction"
                    : matcher.group("REGISTERPATTERN") != null ? "register"
                    : matcher.group("MEMORYPATTERN") != null ? "memory"
                    : matcher.group("PAREN") != null ? "paren"
                    : matcher.group("BRACE") != null ? "brace"
                    : matcher.group("BRACKET") != null ? "bracket"
                    : matcher.group("STRING") != null ? "string"
                    : matcher.group("COMMENT") != null ? "comment"
                    : null;
            /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private void autocomplete(String text, int start, int end) {
        text = text.replaceAll("[\\t\\n\\r\\f\\v]", "");
//        System.out.println("in text = " + text);
        if ( text.length() == 0 || this.entries.contains(text) ) {
//            System.out.println("Hide Autocomplete!");
            this.entriesPopup.hide();
        } else {
//            System.out.println("Show Autocomplete!");
            LinkedList<String> searchResult = new LinkedList<>();
            searchResult.addAll(this.entries.subSet(text, text + Character.MAX_VALUE));
//            System.out.println("text = " + text);
//            System.out.println("searchResult = " + searchResult.size());
            if ( searchResult.size() > 0 ) {
//                System.out.println("POPULATE");
                this.populatePopupItems(searchResult, start, end);
                if ( !entriesPopup.isShowing() ) {
//                    System.out.println("SHOW POPUP");
                    this.entriesPopup.show(MainApp.primaryStage);
                }
            } else {
//                System.out.println("HIDE DUMDUM!");
                this.entriesPopup.hide();
            }
        }
    }

    private void populatePopupItems(List<String> searchResult, int start, int end) {
        List<CustomMenuItem> menuItems = new LinkedList<>();

        // If you'd like more entries, modify this line.
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        for ( int i = 0; i < count; i++ ) {
            final String result = searchResult.get(i);
            Label entryLabel = new Label(result);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            item.setOnAction((event) -> {
                // TODO: Replace the word being completed in the codeArea
                this.codeArea.replaceText(start, end, result.toUpperCase());
                this.entriesPopup.hide();
            });
            menuItems.add(item);
        }
        this.entriesPopup.getItems().clear();
        this.entriesPopup.getItems().addAll(menuItems);
    }

    private void setCodeAreaAutocomplete() {
        this.entries = new TreeSet<>();
        this.entriesPopup = new ContextMenu();

        this.entries.addAll(Arrays.asList(this.sysCon.getKeywordBuilder().getInstructionKeywords()));
        this.entries.addAll(Arrays.asList(this.sysCon.getKeywordBuilder().getRegisterKeywords()));

        this.codeArea.setPopupWindow(entriesPopup);
        this.codeArea.setPopupAlignment(PopupAlignment.SELECTION_BOTTOM_CENTER);
        this.codeArea.setPopupAnchorOffset(new Point2D(4, 4));
    }

    public void setCodeAreaText(String text) {
        this.codeArea.replaceText(text);
    }

    public Node getCodeArea() {
        return codeArea;
    }

    public String getCodeAreaText() {
        return codeArea.getText();
    }

    public UndoManager getUndoManager() {
        return codeArea.getUndoManager();
    }

    public void undo() {
        codeArea.getUndoManager().undo();
    }

    public void redo() {
        codeArea.getUndoManager().redo();
    }

    public void requestFocus() {
        Platform.runLater(() -> codeArea.requestFocus());
    }

    @Override
    public void update(CalvisFormattedInstruction currentInstruction, int lineNumber) {
//        System.out.println("Update TextEditorPane!");
        String currentLine = currentInstruction.toString();
        if ( currentLine != null ) {
            String codeAreaText = this.codeArea.getText();
            Matcher matcher = this.lineByLinePattern.matcher(codeAreaText);
            HashMap<Integer, int[]> findHighlightRanges = new HashMap<>();
            // c represents matched
            int c = 0;

            while ( matcher.find() ) {
                if ( !matcher.toMatchResult().group().contains(";") ) {
                    int[] arrRange = new int[2];
                    arrRange[0] = matcher.start();
                    arrRange[1] = matcher.end();
                    findHighlightRanges.put(c, arrRange);
                    c++;
                }
            }
            int[] range = findHighlightRanges.get(lineNumber);
            if ( range != null ) {
                // System.out.println(range[0] + " to " + range[1]);
                this.codeArea.selectRange(range[0], range[1]);
                this.codeArea.redo();
            }
        } else {
//            System.out.println("null highlight currentLine");
            this.codeArea.selectRange(0, 0);
            this.redo();
        }
    }

    @Override
    public void refresh() {
        this.codeArea.selectRange(0, 0);
        this.redo();
    }

    @Override
    public void build() {
        this.setCodeEnvironment();

        String[] arr = this.sysCon.getKeywordBuilder().getInstructionKeywords();
        String expression = String.join("|", arr);
        expression = "((.*)\\b(" + expression + ")\\b(.*)(?=;))|((.*)\\b(" + expression + ")\\b(.*))";
        this.lineByLinePattern = Pattern.compile("(?<FIND>" + expression + ")");

        this.setCodeAreaAutocomplete();
    }
}
