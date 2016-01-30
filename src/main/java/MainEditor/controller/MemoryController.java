package MainEditor.controller;

import MainEditor.model.AssemblyComponent;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class MemoryController extends AssemblyComponent implements Initializable {

    @FXML
    private TableView<Map.Entry<String,String>> tableViewMemory;
    @FXML
    private TableColumn<Map.Entry<String,String>, String> memoryAddress;
    @FXML
    private TableColumn<Map.Entry<String,String>, String> memoryRepresentation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        memoryAddress.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getKey()));
        memoryRepresentation.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getValue()));
    }


    @Override
    public void update(String currentLine, int lineNumber) {
        tableViewMemory.refresh();
    }

    @Override
    public void refresh() {
        tableViewMemory.refresh();
    }

    @Override
    public void build() {
        Map map = this.sysCon.getMemoryState().getMemoryMap();
        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(map.entrySet());
        tableViewMemory.setItems(items);

    }
}
