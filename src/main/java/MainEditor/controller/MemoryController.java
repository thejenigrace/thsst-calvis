package MainEditor.controller;

import EnvironmentConfiguration.model.Register;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import MainEditor.model.Memory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
//        memoryAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
//        memoryRepresentation.setCellValueFactory(new PropertyValueFactory<>("representation"));
//        memoryInstruction.setCellValueFactory(new PropertyValueFactory<>("instruction"));
//
//        tableViewMemory.getItems().setAll(parseMemoryList());

        memoryAddress.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getKey()));
        memoryRepresentation.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getValue()));
    }


    @Override
    public void update() {
        tableViewMemory.refresh();
    }

    @Override
    public void build() {
        Map map = this.sysCon.getMemoryState().getMemoryMap();
        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(map.entrySet());
        tableViewMemory.setItems(items);
    }
}
