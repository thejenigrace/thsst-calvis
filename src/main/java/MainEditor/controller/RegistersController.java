package MainEditor.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import MainEditor.model.Register;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Jennica Alcalde on 10/3/2015.
 */
public class RegistersController implements Initializable {

    @FXML
    private TitledPane titledPaneRegisters;
    @FXML
    private TableView<Register> tableViewRegister;
    @FXML
    private TableColumn<Register, String> registerName;
    @FXML
    private TableColumn<Register, String> registerValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        registerValue.setCellValueFactory(new PropertyValueFactory<>("value"));

        tableViewRegister.getItems().setAll(parseRegisterList());
        titledPaneRegisters.setExpanded(true);
    }

    private List<Register> parseRegisterList() {
        List<Register> registers = new ArrayList<>();
        registers.add(new Register("EAX", "0x00000000"));
        registers.add(new Register("EBX", "0x00000000"));
        registers.add(new Register("ECX", "0x00000000"));
        registers.add(new Register("EDX", "0x00000000"));
        registers.add(new Register("ESI", "0x00000000"));
        registers.add(new Register("EDI", "0x00000000"));
        registers.add(new Register("EBP", "0x00000000"));
        registers.add(new Register("EDP", "0x00000000"));
        registers.add(new Register("EIP", "0x00000000"));

        return registers;
    }
}
