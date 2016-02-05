package MainEditor.model;

import MainEditor.controller.WorkspaceController;
import javafx.scene.control.Tab;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jennica on 04/02/2016.
 */
public class TextEditor extends AssemblyComponent {
    private WorkspaceController workspaceController;
    private CodeArea codeArea;
    private final Tab tab = new Tab();
    private Pattern lineByLinePattern;

    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static String[] KEYWORDS;
    private static String KEYWORD_PATTERN;
    private static Pattern PATTERN;

    public TextEditor(WorkspaceController workspaceController) {
        this.workspaceController = workspaceController;
        this.codeArea = new CodeArea();

        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.richChanges().subscribe(change -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));

        this.tab.setText("Untitled");
        this.tab.setContent(codeArea);
    }

    public Tab getTab() {
        return tab;
    }

    public CodeArea getCodeArea() {
        return codeArea;
    }

    /**
     * Method for configuring the highlighted
     *  keywords within the text editor
     */
    private void setCodeEnvironment(){
        this.KEYWORDS = sysCon.getKeywords();
        this.KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
        this.PATTERN = Pattern.compile(
                "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                        + "|(?<PAREN>" + PAREN_PATTERN + ")"
                        + "|(?<BRACE>" + BRACE_PATTERN + ")"
                        + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                        + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                        + "|(?<STRING>" + STRING_PATTERN + ")"
                        + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
        );
    }

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

    @Override
    public void update(String currentLine, int lineNumber) {
        if (currentLine != null) {
            Matcher matcher = lineByLinePattern.matcher(this.codeArea.getText());
            HashMap<Integer, int[]> findHighlightRanges = new HashMap<>();
            // c represents matched
            int c = 0;

            while (matcher.find()) {
                int[] arrRange = new int[2];
                arrRange[0] = matcher.start();
                arrRange[1] = matcher.end();
                findHighlightRanges.put(c, arrRange);
                c++;
            }
            int[] range = findHighlightRanges.get(lineNumber);
            if ( range != null ) {
                // System.out.println(range[0] + " to " + range[1]);
                codeArea.selectRange(range[0], range[1]);
                codeArea.redo();
            }
        }
        else {
            System.out.println("null currentLine");
            codeArea.selectRange(0,0);
            codeArea.redo();
        }
    }

    @Override
    public void refresh() {
        codeArea.selectRange(0,0);
        codeArea.redo();
    }

    @Override
    public void build() {
        this.setCodeEnvironment();
        String[] arr = this.sysCon.getInstructionKeywords();
        String expression =  String.join("|", arr) ;
        expression = "(" + expression +")(.*)";
        lineByLinePattern = Pattern.compile("(?<FIND>" + expression + ")");
    }
}
