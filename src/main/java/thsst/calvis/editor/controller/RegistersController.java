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
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.editor.model.Flag;
import thsst.calvis.editor.view.AssemblyComponent;

import java.net.URL;
import java.util.ArrayList;
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

    ArrayList<String> motherRegNameList = new ArrayList<>();
    ArrayList<String> childRegNameList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.treeTableViewRegister.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.colRegisterName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Register, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue().getName()));

        this.colRegisterHexValue.setCellValueFactory((TreeTableColumn.CellDataFeatures<Register, String> p) -> {
            String registerValue = p.getValue().getValue().getValue().toString();

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

        this.colMxcsrFlagsName.setCellValueFactory(new PropertyValueFactory<Flag, String>("name"));
        this.colMxcsrFlagsValue.setCellValueFactory(new PropertyValueFactory<Flag, String>("value"));

        this.colEFlagsName.setCellValueFactory(new PropertyValueFactory<Flag, String>("name"));
        this.colEFlagsValue.setCellValueFactory(new PropertyValueFactory<Flag, String>("value"));
    }

    @Override
    public void update(CalvisFormattedInstruction currentInstruction, int lineNumber) {
        this.motherRegNameList.forEach(s -> {
            System.out.println("ito: " + s);
        });

        this.treeTableViewRegister.refresh();
        this.tableViewMxcsrFlags.refresh();
        this.tableViewEFlags.refresh();

        Token[] tokens = currentInstruction.getParameterTokens();
        this.treeTableViewRegister.getSelectionModel().clearSelection();
        for ( Token token : tokens ) {
            System.out.println("chose: " + token);

            int regIndex;
            if ( this.childRegNameList.contains(token.getValue() ))
                regIndex = this.motherRegNameList.indexOf(this.getMotherRegister(token.getValue()));
            else
                regIndex = this.motherRegNameList.indexOf(token.getValue());

            this.treeTableViewRegister.getSelectionModel().select(regIndex);
        }
    }

    private String getMotherRegister(String childRegister) {
        switch ( childRegister ) {
            case "AX":
            case "AH":
            case "AL" : return "EAX";
            case "BX":
            case "BH":
            case "BL": return "EBX";
            case "CX":
            case "CH":
            case "CL" : return "ECX";
            case "DX":
            case "DH":
            case "DL": return "EDX";
            case "SI": return "ESI";
            case "DI": return "EDI";
            case "BP": return "ESP";
            case "SP": return "EBP";
            default: return "";
        }
    }

    @Override
    public void refresh() {
        this.treeTableViewRegister.refresh();
        this.treeTableViewRegister.getSelectionModel().clearSelection();
        this.tableViewMxcsrFlags.refresh();
        this.tableViewEFlags.refresh();
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
                        // Add child register name for table row selection / highlight
                        this.childRegNameList.add(rChild.getName());
                    }
                }

                dummyRoot.getChildren().add(motherRegister);
                // Add mother register name for table row selection / highlight
                this.motherRegNameList.add(motherRegister.getValue().getName());
            }

            this.treeTableViewRegister.setRoot(dummyRoot);
            this.treeTableViewRegister.setShowRoot(false);

            this.mxcsrFlagList = FXCollections.observableArrayList(this.sysCon.getRegisterState().getMxscr().getFlagList());
            this.tableViewMxcsrFlags.setItems(this.mxcsrFlagList);

            this.eFlagsList = FXCollections.observableArrayList(this.sysCon.getRegisterState().getEFlags().getFlagList());
            this.tableViewEFlags.setItems(this.eFlagsList);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
