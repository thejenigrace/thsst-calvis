package thsst.calvis.editor.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import thsst.calvis.configuration.model.engine.CalvisFormattedInstruction;
import thsst.calvis.configuration.model.engine.Register;
import thsst.calvis.editor.model.Flag;
import thsst.calvis.editor.view.AssemblyComponent;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Jennica Alcalde on 10/3/2015.
 */
public class RegistersController extends AssemblyComponent implements Initializable {

    @FXML
    private TreeTableView<Register> treeTableViewRegister;
    @FXML
    private TreeTableColumn<Register, String> colRegisterName;
    @FXML
    private TreeTableColumn<Register, String> colRegisterHexValue;

    @FXML
    private TableView<Flag> tableViewMxcsrFlags;
    @FXML
    private TableColumn colMxcsrFlagsName;
    @FXML
    private TableColumn colMxcsrFlagsValue;
    @FXML
    private TableView<Flag> tableViewEFlags;
    @FXML
    private TableColumn colEFlagsName;
    @FXML
    private TableColumn colEFlagsValue;

    private ObservableList<Flag> mxcsrFlagList;
    private ObservableList<Flag> eFlagsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colRegisterName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Register, String> p) -> new ReadOnlyStringWrapper(
                p.getValue().getValue().getName()
        ));
        colRegisterHexValue.setCellValueFactory((TreeTableColumn.CellDataFeatures<Register, String> p) -> {
            String registerValue = p.getValue().getValue().getValue().toString();
            String registerName = p.getValue().getValue().getName();

            Register someRegister = p.getValue().getValue();

            if ( someRegister.getSize() == 128 && registerValue.length() == 32 ) {
                registerValue = registerValue.substring(0, 8) + " "
                        + registerValue.substring(8, 16) + " "
                        + registerValue.substring(16, 24) + " "
                        + registerValue.substring(24);
            } else if ( someRegister.getSize() == 64 && registerValue.length() == 16 ) {
                registerValue = registerValue.substring(0, 8) + " "
                        + registerValue.substring(8);
            }
            ReadOnlyStringWrapper stringWrapper = new ReadOnlyStringWrapper(registerValue);
            return stringWrapper;
        });

        colMxcsrFlagsName.setCellValueFactory(new PropertyValueFactory<Flag, String>("name"));
        colMxcsrFlagsValue.setCellValueFactory(new PropertyValueFactory<Flag, String>("value"));

        colEFlagsName.setCellValueFactory(new PropertyValueFactory<Flag, String>("name"));
        colEFlagsValue.setCellValueFactory(new PropertyValueFactory<Flag, String>("value"));
    }

    @Override
    public void update(CalvisFormattedInstruction currentInstruction, int lineNumber) {
        treeTableViewRegister.refresh();
        tableViewMxcsrFlags.refresh();
        tableViewEFlags.refresh();
    }

    @Override
    public void refresh() {
        treeTableViewRegister.refresh();
        tableViewMxcsrFlags.refresh();
        tableViewEFlags.refresh();
    }

    @Override
    public void build() {
        try {
            Map map = this.sysCon.getRegisterState().getRegisterMap();

            ObservableList<Register> registers = FXCollections.observableArrayList(map.values());
            TreeItem<Register> dummyRoot = new TreeItem<>();

            for ( Register rMother : registers ) {
                TreeItem<Register> motherRegister = new TreeItem<>(rMother);
                Map childMap = this.sysCon.getRegisterState().getChildRegisterMap(rMother.getName());

                if ( childMap != null ) {
                    ObservableList<Register> childRegisters = FXCollections.observableArrayList(childMap.values());
                    for ( Register rChild : childRegisters ) {
                        motherRegister.getChildren().add(new TreeItem<>(rChild));
                    }
                }

                dummyRoot.getChildren().add(motherRegister);
            }

            this.treeTableViewRegister.setRoot(dummyRoot);
            this.treeTableViewRegister.setShowRoot(false);

            this.mxcsrFlagList = FXCollections.observableArrayList(this.sysCon.getRegisterState().getMxscr().getFlagList());
            tableViewMxcsrFlags.setItems(this.mxcsrFlagList);

            this.eFlagsList = FXCollections.observableArrayList(this.sysCon.getRegisterState().getEFlags().getFlagList());
            this.tableViewEFlags.setItems(this.eFlagsList);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
