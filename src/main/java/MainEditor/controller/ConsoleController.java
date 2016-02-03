package MainEditor.controller;

import MainEditor.model.AssemblyComponent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.io.Console;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class ConsoleController extends AssemblyComponent implements Initializable {
    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL url, ResourceBundle resources) {

    }

    @Override
    public void build() {

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
