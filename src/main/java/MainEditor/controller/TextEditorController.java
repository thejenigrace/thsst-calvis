package MainEditor.controller;

import MainEditor.model.AssemblyComponent;
import org.fxmisc.richtext.CodeArea;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Goodwin Chua on 1/28/2016.
 */
public class TextEditorController extends AssemblyComponent {

    private CodeArea codeArea;

    public TextEditorController(CodeArea codeArea){
        this.codeArea = codeArea;
    }

    @Override
    public void update(String currentLine, int lineNumber) {

        if (currentLine != null) {
            String find_pattern = "(?m)^.*$";
            Pattern pattern = Pattern.compile("(?<FIND>" + find_pattern + ")");
            Matcher matcher = pattern.matcher(this.codeArea.getText());
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
            if (range != null) {
                codeArea.selectRange(range[0], range[1]);
                codeArea.redo();
            }
        }
        else {
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

    }
}
