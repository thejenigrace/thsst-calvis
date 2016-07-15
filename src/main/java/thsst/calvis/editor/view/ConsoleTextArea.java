package thsst.calvis.editor.view;

import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Goodwin Chua on 7 Jul 2016.
 */
public class ConsoleTextArea extends StyleClassedTextArea {

    private boolean textState;

    public ConsoleTextArea(boolean preserveStyle) {
        super(preserveStyle);
        setWrapText(true);
        getStyleClass().add("console");
        getStylesheets().add("/css/console.css");
        richChanges().subscribe(change -> setStyleSpans(0, changeTextColor(getText())));

        this.textState = false;
    }

    @Override
    public void replaceText(int start, int end, String text) {
        String current = getText();
        // only insert if no new lines after insert position:
        if (!current.substring(start).contains("\n") && this.textState) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        String current = getText();
        int selectionStart = getSelection().getStart();
        if (!current.substring(selectionStart).contains("\n") && this.textState) {
            super.replaceSelection(text);
        }
    }

    @Override
    public void clear() {
        super.replaceText(0, super.getLength(), "");
    }

    private StyleSpans<Collection<String>> changeTextColor(String text) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        spansBuilder.add(Collections.singleton("console-font-color"), text.length());
        return spansBuilder.create();
    }

    public boolean getState() {
        return this.textState;
    }

    public void setState(boolean flag) {
        this.textState = flag;
    }

}
