package thsst.calvis.editor.controller;

import com.github.pfmiles.dropincc.DropinccException;
import thsst.calvis.configuration.model.engine.CalvisFormattedInstruction;
import thsst.calvis.configuration.model.exceptions.IncorrectParameterException;
import thsst.calvis.editor.view.AssemblyComponent;
import thsst.calvis.editor.model.ErrorLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jennica Alcalde on 10/4/2015.
 */
public class ErrorLoggerController extends AssemblyComponent implements Initializable {

    @FXML
    private TableView<ErrorLog> tableViewErrorLogger;
    @FXML
    private TableColumn<ErrorLog, String> colErrorType;
    @FXML
    private TableColumn<ErrorLog, String> colErrorCause;
    @FXML
    private TableColumn<ErrorLog, String> colErrorMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.colErrorType.setCellValueFactory(
                p -> p.getValue().errorTypeProperty());
        this.colErrorCause.setCellValueFactory(
                p -> p.getValue().errorCauseProperty());
        this.colErrorMessage.setCellValueFactory(
                p -> p.getValue().errorMessageProperty());
    }

    @Override
    public void update(CalvisFormattedInstruction currentInstruction, int lineNumber) {
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

    public void build(Exception e) {
        if ( e != null ) {
            ObservableList<ErrorLog> items = FXCollections.observableArrayList();
            String type = e.getClass().getSimpleName();
            String cause = "N/A";
            String message = "N/A";

            if ( e instanceof IncorrectParameterException ) {
                cause = "Line: " + ((IncorrectParameterException) e).getLineNumber();
            }
            if ( e.getCause() != null ) {
                cause = e.getCause().getClass().getSimpleName();
                message = e.getCause().getLocalizedMessage();
            }
            if ( e.getMessage() != null ) {
                message = e.getMessage();
            }
            if ( e instanceof DropinccException ) {
                type = "SyntaxError";
                if ( message.contains("position") ) {
                    cause = message.substring(message.indexOf("position:"));
                    cause = cause.replaceAll(",.*", "");
                    cause = cause.replaceAll(".*: ", "");
                    String parsedCode = this.sysCon.getParsedCode();
                    String[] lines = parsedCode.split("\n");
                    int causePosition = Integer.parseInt(cause);
                    int linesRead = 0;
                    for ( int i = 0; i < lines.length; i++ ) {
                        if ( lines[i].length() + linesRead < causePosition ) {
                            linesRead += lines[i].length();
                        } else {
                            if ( causePosition - 1 >= 0 && parsedCode.charAt(causePosition - 1) == '\n' ) {
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
                        if ( message.contains("'['") && message.contains("']'") ) {
                            message += " ; Missing size directive ";
                        }
                    }
                }
            }
            items.add(new ErrorLog(type, cause, message));
            this.tableViewErrorLogger.setItems(items);
        }
    }
}
