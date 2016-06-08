package editor.model;

import editor.MainApp;
import editor.controller.WorkspaceController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.fxmisc.richtext.*;
import org.fxmisc.undo.UndoManager;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jennica on 04/02/2016.
 */
public class TextEditor extends AssemblyComponent {

    private WorkspaceController workspaceController;
    private CodeArea codeArea;
    private Tab tab;
    private Pattern lineByLinePattern;

    // 'path' property
    private final ObjectProperty<Path> path = new SimpleObjectProperty<>();
    // 'modified' property
    private final ReadOnlyBooleanWrapper modified = new ReadOnlyBooleanWrapper();

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

    /**
     * The existing autocomplete entries.
     */
    private SortedSet<String> entries;
    /**
     * The popup used to select an entry.
     */
    private ContextMenu entriesPopup;

//    private void findWord(caret)

    public TextEditor(WorkspaceController workspaceController) {
        System.out.println("Initialize");
        this.workspaceController = workspaceController;
        this.codeArea = new CodeArea();
        codeArea.setStyle("-fx-highlight-fill: lightgray; -fx-font-size: 14px;");
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.richChanges().subscribe(change -> {
//            this.tab.setGraphic(new Text("*"));
//            this.workspaceController.disableSaveMode(false);
//            System.out.println(codeArea.getCaretPosition());
            int caret = codeArea.getCaretPosition();
            System.out.println("caret = " + caret);
            String text = codeArea.getText();

            int count = 0;
            int start = 0;
            for ( int i = caret-1; i > 0; i-- ) {
                System.out.println("try = " + codeArea.getText(i, caret));
                start = i;
                if ( codeArea.getText(i, caret-count).equals(" "))
                    break;

                count++;
            }

            System.out.println("count = " + count);

            if ( caret > 1 && codeArea.getText(caret-count-1, caret-count).equals(" ")) {
                System.out.println("space");
                System.out.println(codeArea.getText(caret-count, caret));
                this.autocomplete(codeArea.getText(caret-count, caret), caret-count, caret);
            } else {
                this.autocomplete(codeArea.getText(), 0, caret);
            }

            codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
        });

        this.tab = new Tab();
//        this.tab.setText("Untitled");
        this.tab.setUserData(this);
        this.tab.setContent(codeArea);

        this.path.addListener((observable, oldPath, newPath) -> updateTab());
        this.modified.addListener((observable, oldPath, newPath) -> updateTab());
        updateTab();

        tab.setOnSelectionChanged(e -> {
            if ( tab.isSelected() ) {
                Platform.runLater(() -> activated());
            }
        });
//       activated();
    }

    public Tab getTab() {
        return tab;
    }

    public CodeArea getCodeArea() {
        return codeArea;
    }

    public Path getPath() {
        return path.get();
    }

    public void setPath(Path path) {
        this.path.set(path);
    }

    public ObjectProperty<Path> pathProperty() {
        return path;
    }

    public boolean isModified() {
        return modified.get();
    }

    public ReadOnlyBooleanProperty modifiedProperty() {
        return modified.getReadOnlyProperty();
    }

    private void updateTab() {
        Path path = this.path.get();
        tab.setText((path != null) ? path.getFileName().toString() : "Untitled");
        tab.setTooltip((path != null) ? new Tooltip(path.toString()) : null);
        tab.setGraphic(isModified() ? new Text("*") : null);

        if ( isModified() ) {
            workspaceController.disableSaveMode(false);
        } else {
            workspaceController.disableSaveMode(true);
        }
    }

    private void activated() {
        System.out.println("Tab Pane Activated");
//        if( tab.getTabPane() == null || !tab.isSelected())
//            return; // tab is already closed or no longer active
//
//        if (tab.getContent() != null) {
//            codeArea.requestFocus();
//            return;
//        }

        // bind the editor undo manager to the properties
        UndoManager undoManager = codeArea.getUndoManager();
        modified.bind(Bindings.not(undoManager.atMarkedPositionProperty()));

        codeArea.requestFocus();

        if ( isModified() ) {
            workspaceController.disableSaveMode(false);
        } else {
            workspaceController.disableSaveMode(true);
        }
    }

    /**
     * Method for configuring the highlighted keywords within the text editor
     */
    private void setCodeEnvironment() {
        INSTRUCTION_KEYWORDS = sysCon.getInstructionKeywords();
        INSTRUCTION_PATTERN = "\\b(" + String.join("|", INSTRUCTION_KEYWORDS) + ")\\b";
        REGISTER_KEYWORDS = sysCon.getRegisterKeywords();
        REGISTER_PATTERN = "\\b(" + String.join("|", REGISTER_KEYWORDS) + ")\\b";
        MEMORY_KEYWORDS = sysCon.getMemoryKeywords();
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

    @Override
    public void update(String currentLine, int lineNumber) {
        if ( currentLine != null ) {
            String codeAreaText = this.codeArea.getText();
            Matcher matcher = lineByLinePattern.matcher(codeAreaText);
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
                codeArea.selectRange(range[0], range[1]);
                codeArea.redo();
            }
        } else {
            System.out.println("null highlight currentLine");
            codeArea.selectRange(0, 0);
            codeArea.redo();
        }
    }

    @Override
    public void refresh() {
        codeArea.selectRange(0, 0);
        codeArea.redo();
    }

    private void autocomplete(String text, int start, int end) {
        if ( text.length() == 0 ) {
            System.out.println("HIDE!");
            entriesPopup.hide();
        } else {
            System.out.println("SHOW");
            LinkedList<String> searchResult = new LinkedList<>();
            searchResult.addAll(entries.subSet(text, text + Character.MAX_VALUE));
            if ( entries.size() > 0 ) {
                populatePopupItems(searchResult, start, end);
                if ( !entriesPopup.isShowing() ) {
                    entriesPopup.show(MainApp.primaryStage);
                }
            }
        }
    }

    private void populatePopupItems(List<String> searchResult, int start, int end) {
        List<CustomMenuItem> menuItems = new LinkedList<>();

        // If you'd life more entries, modify this line.
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        for ( int i = 0; i < count; i++ ) {
            final String result = searchResult.get(i);
            Label entryLabel = new Label(result);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
//            item.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    // TODO: Replace the word being completed in the codeArea
//                    codeArea.replaceText(start, end, result);
//                    entriesPopup.hide();
//                }
//            });
            item.setOnAction((event) -> {
                // TODO: Replace the word being completed in the codeArea
                codeArea.replaceText(start, end, result);
                entriesPopup.hide();
            });
            menuItems.add(item);
        }
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }

    private void setCodeAreaAutocomplete() {
        System.out.println("setCodeAreaAutoComplete");
        this.entries = new TreeSet<>();
        this.entriesPopup = new ContextMenu();

        this.entries.addAll(Arrays.asList(sysCon.getInstructionKeywords()));

        codeArea.setPopupWindow(entriesPopup);
        codeArea.setPopupAlignment(PopupAlignment.SELECTION_BOTTOM_CENTER);
        codeArea.setPopupAnchorOffset(new Point2D(4, 4));
    }

    @Override
    public void build() {
        this.setCodeEnvironment();
        String[] arr = this.sysCon.getInstructionKeywords();
        String expression = String.join("|", arr);
        expression = "((.*)\\b(" + expression + ")\\b(.*)(?=;))|((.*)\\b(" + expression + ")\\b(.*))";
//        expression = "[^;]\\b(" + expression + ")\\b(.*)"; //?)(?=;)|\\b(" + expression + ")\\b(.*)";
        lineByLinePattern = Pattern.compile("(?<FIND>" + expression + ")");

        this.setCodeAreaAutocomplete();
    }
}
