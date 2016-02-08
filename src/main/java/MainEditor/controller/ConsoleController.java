package MainEditor.controller;

import EnvironmentConfiguration.model.engine.Register;
import MainEditor.model.AssemblyComponent;
import MainEditor.model.FlagUI;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.cell.PropertyValueFactory;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import org.fxmisc.richtext.StyledTextArea;

import java.net.URL;
import java.util.*;

/**
 * Created by Jennica on 07/02/2016.
 */
public class ConsoleController extends AssemblyComponent implements Initializable {

    private StyledTextArea textArea;
    private Tab tab;

    public ConsoleController() {
        this.textArea = new StyleClassedTextArea(false);
        this.textArea.setWrapText(true);
        this.textArea.getStyleClass().add("console");
        this.textArea.getStylesheets().add("/css/console.css");
        this.textArea.richChanges().subscribe( change -> textArea.setStyleSpans(0, changeTextColor(textArea.getText())) );
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
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void build(){

    }

    @Override
    public void update(String currentLine, int lineNumber) {

    }

    @Override
    public void refresh() {

    }

    /*
     * print
     */
    public void printf(String text) {
        textArea.appendText(text);

    }

    /*
     * scan
     */
    public String scanf(int start, int end) {
        return textArea.getText(start, end);

    }

    /*
     * clear screen
     */
    public void clear() {
        textArea.clear();

    }

    /*
     * get char
     */
    public String getch(int start, int end, int index) {
        return textArea.getText(start, end).charAt(index) + "";

    }
}
