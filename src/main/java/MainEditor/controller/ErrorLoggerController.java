package MainEditor.controller;

import MainEditor.model.AssemblyComponent;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Jennica Alcalde on 10/4/2015.
 */
public class ErrorLoggerController extends AssemblyComponent implements Initializable {
    @FXML
    private TableView<Map.Entry<String,String>> tableViewErrorLogger;
    @FXML
    private TableColumn<Map.Entry<String,String>, String> errorType;
    @FXML
    private TableColumn<Map.Entry<String,String>, String> errorMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorType.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getKey()));
        errorMessage.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getValue()));
    }

    @Override
    public void update(String currentLine, int lineNumber) {
        tableViewErrorLogger.refresh();
    }

    @Override
    public void refresh() {
        tableViewErrorLogger.refresh();
    }

    @Override
    public void build() {
//        Map map = this.sysCon.getMemoryState().getMemoryMap();
//        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(map.entrySet());
//        tableViewErrorLogger.setItems(items);
//        tableViewErrorLogger.scrollTo(tableViewMemory.getItems().size());
    }
}
