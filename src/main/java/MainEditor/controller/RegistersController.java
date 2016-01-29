package MainEditor.controller;

import EnvironmentConfiguration.model.Register;
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
 * Created by Jennica Alcalde on 10/3/2015.
 */
public class RegistersController extends AssemblyComponent implements Initializable {
    @FXML
    private TableView<Map.Entry<String,Register>> tableViewRegister;
    @FXML
    private TableColumn<Map.Entry<String, Register>, String> registerName;
    @FXML
    private TableColumn<Map.Entry<String, Register>, String> registerValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerName.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getKey()));
        registerValue.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getValue().toString()));

    }

    @Override
    public void build(){
        Map map = this.sysCon.getRegisterState().getRegisterMap();
        ObservableList<Map.Entry<String, Register>> items = FXCollections.observableArrayList(map.entrySet());
        tableViewRegister.setItems(items);
    }

    @Override
    public void update(String currentLine, int lineNumber) {
        tableViewRegister.refresh();
    }

    @Override
    public void refresh() {
        tableViewRegister.refresh();
    }
}
