package thsst.calvis.editor.controller;

import javafx.scene.control.SelectionMode;
import thsst.calvis.configuration.model.engine.CalvisFormattedInstruction;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.editor.view.AssemblyComponent;
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
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Jennica Alcalde on 10/4/2015.
 */
public class MemoryController extends AssemblyComponent implements Initializable {

    @FXML
    private AnchorPane paneMemoryFilter;

    @FXML
    private TableView<Map.Entry<String, String>> tableViewMemory;
    @FXML
    private TableColumn<Map.Entry<String, String>, String> colMemoryLabel;
    @FXML
    private TableColumn<Map.Entry<String, String>, String> colMemoryAddress;
    @FXML
    private TableColumn<Map.Entry<String, String>, String> colMemoryRepresentation;

    private Memory memory;

    private TextField textFieldFilter;

    private  ObservableList<Map.Entry<String, String>> items;

    private ArrayList<String> memoryAddressList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tableViewMemory.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.colMemoryLabel.setCellValueFactory(
                p -> new SimpleStringProperty(this.memory.getCorrespondingLabel(p.getValue().getKey())));
        this.colMemoryAddress.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getKey()));
        this.colMemoryRepresentation.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getValue()));
    }

    @Override
    public void update(CalvisFormattedInstruction currentInstruction, int lineNumber) {
        this.tableViewMemory.refresh();
        this.tableViewMemory.getSelectionModel().clearSelection();

        Token[] tokens = currentInstruction.getParameterTokens();
        for ( Token token : tokens ) {
            if ( token.getType().equals(Token.MEM) ) {
                String baseAddress = this.memory.removeSizeDirectives(token.getValue());
                System.out.println("mem baseAddress: " + baseAddress);
                int memIndex = this.memoryAddressList.indexOf(baseAddress);
                this.tableViewMemory.scrollTo(memIndex);
                this.tableViewMemory.getSelectionModel().select(memIndex);
            }
        }
    }

    @Override
    public void refresh() {
        this.tableViewMemory.refresh();
        this.tableViewMemory.getSelectionModel().clearSelection();
        this.tableViewMemory.scrollTo(this.items.size() - 1);
    }

    @Override
    public void build() {
        this.memory = this.sysCon.getMemoryState();
        Map map = this.memory.getMemoryMap();
        this.items = FXCollections.observableArrayList(map.entrySet());

        items.forEach(i -> {
            this.memoryAddressList.add(i.getKey());
        });

        this.tableViewMemory.setItems(items);
        this.tableViewMemory.scrollTo(this.tableViewMemory.getItems().size());

        this.textFieldFilter = TextFields.createClearableTextField();
        this.textFieldFilter.setPromptText("Memory Filter");
        this.textFieldFilter.textProperty().addListener((o) -> {
            if ( textFieldFilter.textProperty().get().isEmpty() ) {
                tableViewMemory.setItems(items);
                return;
            }

            ObservableList<Map.Entry<String, String>> tableItems = FXCollections.observableArrayList();
            ObservableList<TableColumn<Map.Entry<String, String>, ?>> cols = this.tableViewMemory.getColumns();
            for ( int i = 0; i < items.size(); i++ ) {
                for ( int j = 0; j < cols.size(); j++ ) {
                    TableColumn col = cols.get(j);
                    String cellValue = col.getCellData(items.get(i)).toString();
                    cellValue = cellValue.toLowerCase();
                    if ( cellValue.contains(textFieldFilter.textProperty().get().toLowerCase()) ) {
                        tableItems.add(items.get(i));
                        break;
                    }
                }

            }
            this.tableViewMemory.setItems(tableItems);
        });
        AnchorPane.setTopAnchor(textFieldFilter, 0.0);
        AnchorPane.setBottomAnchor(textFieldFilter, 0.0);
        AnchorPane.setLeftAnchor(textFieldFilter, 0.0);
        AnchorPane.setRightAnchor(textFieldFilter, 0.0);
        this.paneMemoryFilter.getChildren().add(this.textFieldFilter);
    }
}
