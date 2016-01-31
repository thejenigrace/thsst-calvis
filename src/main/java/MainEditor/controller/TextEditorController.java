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
	private Pattern pattern;

    public TextEditorController(CodeArea codeArea){
        this.codeArea = codeArea;
    }

    @Override
    public void update(String currentLine, int lineNumber) {
        if (currentLine != null) {
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
	    String[] arr = this.sysCon.getInstructionKeywords();
	    String expression =  String.join("|", arr) ;
	    expression = "(" + expression +")(.*)";
	    pattern = Pattern.compile("(?<FIND>" + expression + ")");
    }
}
