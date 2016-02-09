package MainEditor.controller;

import EnvironmentConfiguration.model.engine.Register;
import MainEditor.model.AssemblyComponent;
import MainEditor.model.FlagUI;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

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

    private ObservableList<FlagUI> flagList1;
    private ObservableList<FlagUI> flagList2;

    @FXML
    private TableView<FlagUI> tableViewFlags1;
    @FXML
    private TableColumn flagsName1;
    @FXML
    private TableColumn flagsValue1;

    @FXML
    private TableView<FlagUI> tableViewFlags2;
    @FXML
    private TableColumn<FlagUI, String> flagsName2;
    @FXML
    private TableColumn<FlagUI, String> flagsValue2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerName.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getKey()));
        registerValue.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().getValue().toString()));

        flagsName1.setCellValueFactory(new PropertyValueFactory<FlagUI, String>("name"));
        flagsValue1.setCellValueFactory(new PropertyValueFactory<FlagUI, String>("flagValue"));

        SplitPane.setResizableWithParent(tableViewRegister, Boolean.TRUE);
        SplitPane.setResizableWithParent(tableViewFlags1, Boolean.TRUE);
    }

    @Override
    public void build(){
        TreeMap map = (TreeMap) this.sysCon.getRegisterState().getRegisterMap();
        ObservableList<Map.Entry<String, Register>> items = FXCollections.observableArrayList(map.entrySet());
        tableViewRegister.setItems(items);
        registerName.setComparator(map.comparator());

        flagList1 = FXCollections.observableArrayList(this.sysCon.getRegisterState().getEFlags().getFlagUIList());
        tableViewFlags1.setItems(flagList1);
    }

    @Override
    public void update(String currentLine, int lineNumber) {
        tableViewRegister.refresh();

        flagList1 = FXCollections.observableArrayList(this.sysCon.getRegisterState().getEFlags().getFlagUIList());
        tableViewFlags1.setItems(flagList1);
        tableViewFlags1.refresh();
    }

    @Override
    public void refresh() {
        tableViewRegister.refresh();

        flagList1 = FXCollections.observableArrayList(this.sysCon.getRegisterState().getEFlags().getFlagUIList());
        tableViewFlags1.setItems(flagList1);
        tableViewFlags1.refresh();
    }
}
