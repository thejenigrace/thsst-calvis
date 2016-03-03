package MainEditor.controller;

import EnvironmentConfiguration.model.engine.CALVISInstruction;
import MainEditor.model.AssemblyComponent;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import org.fxmisc.richtext.StyledTextArea;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Jennica on 07/02/2016.
 */
public class ConsoleController extends AssemblyComponent {

    private StyledTextArea textArea;
    private Tab tab;
    private boolean state;
    private int lineBefore;
    private CALVISInstruction currentInstruction;

    public ConsoleController() {
        this.state = false;
        this.lineBefore = 0;
        this.textArea = new StyleClassedTextArea(false){
            @Override
            public void replaceText(int start, int end, String text) {
                String current = getText();
                // only insert if no new lines after insert position:
                if (!current.substring(start).contains("\n") && state) {
                    super.replaceText(start, end, text);
                }
            }

            @Override
            public void replaceSelection(String text) {
                String current = getText();
                int selectionStart = getSelection().getStart();
                if (!current.substring(selectionStart).contains("\n") && state) {
                    super.replaceSelection(text);
                }
            }

        };
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

    @Override
    public void update(String currentLine, int lineNumber) {
//        Platform.runLater(
//                new Thread() {
//                    public void run() {
//                        if (lineNumber == 0 ) {
//                            printf("hello");
//                            scanf();
//                        }
//                    }
//                }
//        );
    }

    @Override
    public void refresh() {
        cls();
    }

    @Override
    public void build() {
        this.textArea.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER && state)  {
//                String text = retrieveScanned();
                if ( currentInstruction != null ) {
                    currentInstruction.executeScan();
                }
            }
        });
    }

    public void printf(String text){
        this.state = true;
        this.textArea.appendText(text);
        this.state = false;
    }

    public void scanf(){
        this.state = true;
        this.sysCon.pauseFromConsole();
        lineBefore = textArea.getText().length();
    }

    public String retrieveScanned() {
        String text = textArea.getText();
        text = textArea.getText(lineBefore, text.length());
//        System.out.println(text);
        this.state = false;
        this.sysCon.resumeFromConsole();
        return text;
    }

    public void cls(){
        this.state = true;
        this.textArea.clear();
        this.state = false;
        this.lineBefore = 0;
    }

    public void attachCALVISInstruction(CALVISInstruction calvisInstruction) {
        this.currentInstruction = calvisInstruction;
    }

}
