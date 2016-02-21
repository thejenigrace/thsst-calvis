package MainEditor.controller;

import MainEditor.model.AssemblyComponent;
import MainEditor.model.ErrorLog;
import com.github.pfmiles.dropincc.DropinccException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.LineNumberReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Jennica Alcalde on 10/4/2015.
 */
public class ErrorLoggerController extends AssemblyComponent implements Initializable {
    @FXML
    private TableView<ErrorLog> tableViewErrorLogger;
    @FXML
    private TableColumn<ErrorLog, String> errorType;
	@FXML
	private TableColumn<ErrorLog, String> errorCause;
    @FXML
    private TableColumn<ErrorLog, String> errorMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorType.setCellValueFactory(
                p -> p.getValue().errorTypeProperty());
	    errorCause.setCellValueFactory(
			    p -> p.getValue().errorCauseProperty());
        errorMessage.setCellValueFactory(
                p -> p.getValue().errorMessageProperty());

    }

    @Override
    public void update(String currentLine, int lineNumber) {
        tableViewErrorLogger.refresh();
    }

    @Override
    public void refresh() {
        tableViewErrorLogger.setItems(null);
    }

    @Override
    public void build() {
		// DO NOT USE
    }

    public void build(Exception e){
	    if ( e != null ){
			ObservableList<ErrorLog> items = FXCollections.observableArrayList();
		    String type = e.getClass().getSimpleName();
		    String cause = "N/A";
		    if ( e.getCause() != null ){
			    cause = e.getCause().getLocalizedMessage();
		    }
		    String message = "N/A";
		    if ( e.getMessage() != null ){
			    message = e.getMessage();
		    }
		    if ( e instanceof DropinccException ){
			    type = "SyntaxError";
			    cause = message.substring(message.indexOf("position:"));
			    cause = cause.replaceAll(",.*", "");
			    cause = cause.replaceAll(".*: ", "");
			    String parsedCode = this.sysCon.getParsedCode();
			    String[] lines = parsedCode.split("\n");
			    int causePosition = Integer.parseInt(cause);
			    int linesRead = 0;
			    System.out.println("Total lines: " + lines.length);
			    for (int i = 0; i < lines.length; i++) {
				    if ( lines[i].length() + linesRead < causePosition ) {
					    linesRead += lines[i].length();
				    } else {
					    if ( parsedCode.charAt(causePosition - 1) == '\n' ) {
						    cause = "Line number: " + i;
					    } else {
						    cause = "Line number: " + (i + 1);
					    }
					    break;
				    }
			    }
			    if ( !cause.contains("number") ) {
				    cause = "position: " + cause;
			    }
			    if ( message.indexOf("upcoming sequence:") != -1 ) {
				    message = "Unknown " + message.substring(message.indexOf("upcoming sequence:"));
			    }
		    }
		    items.add(new ErrorLog(type, cause, message));
		    tableViewErrorLogger.setItems(items);
	    }
    }
}
