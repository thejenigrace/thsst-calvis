package editor.controller;

import configuration.model.engine.CalvisFormattedInstruction;
import configuration.model.engine.Register;
import editor.model.AssemblyComponent;
import editor.model.Flag;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Arrays;
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
    private TableView<Flag> tableViewFlags1;
    @FXML
    private TableColumn flagsName1;
    @FXML
    private TableColumn flagsValue1;
    @FXML
    private TableView<Flag> tableViewFlags2;
    @FXML
    private TableColumn flagsName2;
    @FXML
    private TableColumn flagsValue2;

    private ObservableList<Flag> flagList1;
    private ObservableList<Flag> flagList2;

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

        flagsName1.setCellValueFactory(new PropertyValueFactory<Flag, String>("name"));
        flagsValue1.setCellValueFactory(new PropertyValueFactory<Flag, String>("flagValue"));

        flagsName2.setCellValueFactory(new PropertyValueFactory<Flag, String>("name"));
        flagsValue2.setCellValueFactory(new PropertyValueFactory<Flag, String>("flagValue"));
    }


    public String convert(String registerName, String hexValue) throws NumberFormatException {
//        System.out.println(registerName + ": " + hexValue);

        String[] gpRegisters = new String[]{"EAX", "EBX", "ECX", "EDX", "ESI", "EDI", "ESP", "EBP", "EIP", "CS", "SS", "DS", "ES", "FS", "GS"};
        String[] mmxRegisters = new String[]{"MM0", "MM1", "MM2", "MM3", "MM4", "MM5", "MM6", "MM7"};

        if ( Arrays.asList(gpRegisters).contains(registerName) || Arrays.asList(mmxRegisters).contains(registerName) ) {
            Long longValue = Long.parseLong(hexValue, 16);
            return longValue.toString();
        }

        return "";
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

//            for (int i = 0; i < dummyRoot.getChildren().size(); i++) {
//                System.out.println("INSIDE DUMMY: " + dummyRoot.getChildren().get(i).getValue().getName());
//            }

            this.treeTableViewRegister.setRoot(dummyRoot);
            this.treeTableViewRegister.setShowRoot(false);

            flagList1 = FXCollections.observableArrayList(this.sysCon.getRegisterState().getEFlags().getFlagList());
            tableViewFlags1.setItems(flagList1);

            flagList2 = FXCollections.observableArrayList(this.sysCon.getRegisterState().getMxscr().getFlagList());
            tableViewFlags2.setItems(flagList2);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(CalvisFormattedInstruction currentInstruction, int lineNumber) {
        treeTableViewRegister.refresh();
        tableViewFlags1.refresh();
        tableViewFlags2.refresh();
    }

    @Override
    public void refresh() {
        treeTableViewRegister.refresh();
        tableViewFlags1.refresh();
        tableViewFlags2.refresh();
    }

}
