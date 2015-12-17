package MainEditor.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import MainEditor.model.Memory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Jennica Alcalde on 10/4/2015.
 */
public class MemoryController implements Initializable {

    @FXML
    private TableView<Memory> tableViewMemory;
    @FXML
    private TableColumn<Memory, String> memoryAddress;
    @FXML
    private TableColumn<Memory, String> memoryRepresentation;
    @FXML
    private TableColumn<Memory, String> memoryInstruction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        memoryAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        memoryRepresentation.setCellValueFactory(new PropertyValueFactory<>("representation"));
        memoryInstruction.setCellValueFactory(new PropertyValueFactory<>("instruction"));

        tableViewMemory.getItems().setAll(parseMemoryList());
    }

    private List<Memory> parseMemoryList() {
        List<Memory> memory = new ArrayList<>();
        memory.add(new Memory("0000", "00" ,""));
        memory.add(new Memory("0001", "00" ,""));
        memory.add(new Memory("0002", "00" ,""));
        memory.add(new Memory("0003", "00" ,""));
        memory.add(new Memory("0004", "00" ,""));
        memory.add(new Memory("0005", "00" ,""));
        memory.add(new Memory("0006", "00" ,""));
        memory.add(new Memory("0007", "00" ,""));
        memory.add(new Memory("0008", "00" ,""));
        memory.add(new Memory("0009", "00" ,""));
        memory.add(new Memory("000A", "00" ,""));
        memory.add(new Memory("000B", "00" ,""));
        memory.add(new Memory("000C", "00" ,""));
        memory.add(new Memory("000D", "00" ,""));
        memory.add(new Memory("000E", "00" ,""));
        memory.add(new Memory("000F", "00" ,""));
        return memory;
    }
}
