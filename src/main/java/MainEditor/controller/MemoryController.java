package MainEditor.controller;

import EnvironmentConfiguration.model.engine.Memory;
import MainEditor.model.AssemblyComponent;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Jennica Alcalde on 10/4/2015.
 */
public class MemoryController extends AssemblyComponent implements Initializable {

    @FXML
    private AnchorPane paneMemoryFilter;
    @FXML
    private AnchorPane paneMemoryTableView;

    @FXML
    private TableView<Map.Entry<String, String>> tableViewMemory;
    @FXML
    private TableColumn<Map.Entry<String, String>, String> memoryLabel;
    @FXML
    private TableColumn<Map.Entry<String, String>, String> memoryAddress;
    @FXML
    private TableColumn<Map.Entry<String, String>, String> memoryRepresentation;

    private Memory memory;

    private TextField textFieldFilter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        memoryLabel.setCellValueFactory(
                p -> new SimpleStringProperty(this.memory.getCorrespondingLabel(p.getValue().getKey())));
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
        this.memory = this.sysCon.getMemoryState();
        Map map = this.memory.getMemoryMap();
        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(map.entrySet());
        tableViewMemory.setItems(items);
        tableViewMemory.scrollTo(tableViewMemory.getItems().size());

        textFieldFilter = TextFields.createClearableTextField();
        textFieldFilter.setPromptText("Memory Filter");
        textFieldFilter.textProperty().addListener((o) -> {
            if(textFieldFilter.textProperty().get().isEmpty()) {
                tableViewMemory.setItems(items);
                return;
            }

            ObservableList<Map.Entry<String, String>> tableItems = FXCollections.observableArrayList();
            ObservableList<TableColumn<Map.Entry<String, String>, ?>> cols = tableViewMemory.getColumns();
            for(int i=0; i<items.size(); i++) {
                for(int j=0; j<cols.size(); j++) {
                    TableColumn col = cols.get(j);
                    String cellValue = col.getCellData(items.get(i)).toString();
                    cellValue = cellValue.toLowerCase();
                    if(cellValue.contains(textFieldFilter.textProperty().get().toLowerCase())) {
                        tableItems.add(items.get(i));
                        break;
                    }
                }

            }
            tableViewMemory.setItems(tableItems);
        });
        AnchorPane.setTopAnchor(textFieldFilter, 0.0);
        AnchorPane.setBottomAnchor(textFieldFilter, 0.0);
        AnchorPane.setLeftAnchor(textFieldFilter, 0.0);
        AnchorPane.setRightAnchor(textFieldFilter, 0.0);
        paneMemoryFilter.getChildren().add(textFieldFilter);
    }
}
