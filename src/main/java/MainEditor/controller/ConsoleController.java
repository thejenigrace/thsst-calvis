package MainEditor.controller;

import javafx.scene.control.Tab;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import org.fxmisc.richtext.StyledTextArea;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Jennica on 07/02/2016.
 */
public class ConsoleController {

    private StyledTextArea textArea;
    private Tab tab;

    public ConsoleController() {
        this.textArea = new StyleClassedTextArea(false);
        this.textArea.setWrapText(true);
        this.textArea.getStyleClass().add("console");
        this.textArea.getStylesheets().add("/css/console.css");
        this.textArea.richChanges().subscribe(change -> textArea.setStyleSpans(0, changeTextColor(textArea.getText())));
        this.tab = new Tab();
        this.tab.setText("Console");
        this.tab.setContent(textArea);
    }

    public Tab getTab() {
        return tab;
    }

    private StyleSpans<Collection<String>> changeTextColor(String text) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        spansBuilder.add(Collections.singleton("console-font-color"), text.length());

        return spansBuilder.create();
    }
}
