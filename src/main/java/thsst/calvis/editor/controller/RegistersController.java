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
//    @FXML
//    private TreeTableColumn<Register, String> colRegisterInfo;

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
        colRegisterHexValue.setCellValueFactory((TreeTableColumn.CellDataFeatures<Register, String> p) -> new ReadOnlyStringWrapper(
                p.getValue().getValue().getValue().toString()
        ));
//        colRegisterInfo.setCellValueFactory((TreeTableColumn.CellDataFeatures<Register, String> p) -> new ReadOnlyStringWrapper(
//                convert(p.getValue().getValue().getName(), p.getValue().getValue().toString())
//        ));

        colMxcsrFlagsName.setCellValueFactory(new PropertyValueFactory<Flag, String>("name"));
        colMxcsrFlagsValue.setCellValueFactory(new PropertyValueFactory<Flag, String>("value"));

        colEFlagsName.setCellValueFactory(new PropertyValueFactory<Flag, String>("name"));
        colEFlagsValue.setCellValueFactory(new PropertyValueFactory<Flag, String>("value"));
    }


//    public String convert(String registerName, String hexValue) throws NumberFormatException {
////        System.out.println(registerName + ": " + hexValue);
//
//        String[] gpRegisters = new String[]{"EAX", "EBX", "ECX", "EDX", "ESI", "EDI", "ESP", "EBP", "EIP", "CS", "SS", "DS", "ES", "FS", "GS"};
//        String[] mmxRegisters = new String[]{"MM0", "MM1", "MM2", "MM3", "MM4", "MM5", "MM6", "MM7"};
//
//        if ( Arrays.asList(gpRegisters).contains(registerName) || Arrays.asList(mmxRegisters).contains(registerName) ) {
//            Long longValue = Long.parseLong(hexValue, 16);
//            return longValue.toString();
//        }
//
//        return "";
//    }

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
