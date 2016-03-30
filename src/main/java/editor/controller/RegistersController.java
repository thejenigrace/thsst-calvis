package editor.controller;

import configuration.model.engine.Register;
import editor.model.AssemblyComponent;
import editor.model.Flag;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Jennica Alcalde on 10/3/2015.
 */
public class RegistersController extends AssemblyComponent implements Initializable {

    @FXML
    private GridPane gridPaneRegister;
    @FXML
    private TreeTableView<Register> treeTableViewRegister;
    @FXML
    private TreeTableColumn<Register, String> colRegisterName;
    @FXML
    private TreeTableColumn<Register, String> colRegisterValue;


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
                p.getValue().getValue().getName()));
        colRegisterValue.setCellValueFactory((TreeTableColumn.CellDataFeatures<Register, String> p) -> new ReadOnlyStringWrapper(
                p.getValue().getValue().getValue().toString()));

        flagsName1.setCellValueFactory(new PropertyValueFactory<Flag, String>("name"));
        flagsValue1.setCellValueFactory(new PropertyValueFactory<Flag, String>("flagValue"));

        flagsName2.setCellValueFactory(new PropertyValueFactory<Flag, String>("name"));
        flagsValue2.setCellValueFactory(new PropertyValueFactory<Flag, String>("flagValue"));
    }

    @Override
    public void build() {
        try {
            this.sysCon.getRegisterState().setRegisterContent();

            Map map = this.sysCon.getRegisterState().getRegisterMap();

            System.out.println(map);

            ObservableList<Register> registers = FXCollections.observableArrayList(map.values());
            TreeItem<Register> dummyRoot = new TreeItem<>();

            for (Register rMother : registers) {
                TreeItem<Register> motherRegister = new TreeItem<>(rMother);
                Map childMap = this.sysCon.getRegisterState().getChildRegisterMap(rMother.getName());

                if (childMap != null) {
                    ObservableList<Register> childRegisters = FXCollections.observableArrayList(childMap.values());
                    for (Register rChild : childRegisters) {
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

//            createTreeTableView(map);
            flagList1 = FXCollections.observableArrayList(this.sysCon.getRegisterState().getEFlags().getFlagList());
            tableViewFlags1.setItems(flagList1);

            flagList2 = FXCollections.observableArrayList(this.sysCon.getRegisterState().getMxscr().getFlagList());
            tableViewFlags2.setItems(flagList2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String currentLine, int lineNumber) {
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

    private void createTreeTableView(Map map) {
        TreeItem<Register> dummyRoot = createNode(map);

        for (int i = 0; i < dummyRoot.getChildren().size(); i++) {
            System.out.println(dummyRoot.getChildren().get(i).getValue().getName());
        }

        this.treeTableViewRegister.setRoot(dummyRoot);
        this.treeTableViewRegister.setShowRoot(false);
    }

    private TreeItem<Register> createNode(Map map) {

        return new TreeItem<Register>() {
            private boolean isLeaf;
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;

            @Override
            public ObservableList<TreeItem<Register>> getChildren() {
                if (isFirstTimeChildren) {
                    isFirstTimeChildren = false;
                    super.getChildren().setAll(buildChildren(map));
                }
                return super.getChildren();
            }

            @Override
            public boolean isLeaf() {
                if (isFirstTimeLeaf) {
                    isFirstTimeLeaf = false;
//                    Register r = getValue();
                    isLeaf = true;
                }

                return isLeaf;
            }

            private ObservableList<TreeItem<Register>> buildChildren(Map map) {
//                File f = TreeItem.getValue();
//                if (f != null && f.isDirectory()) {
//                    File[] files = f.listFiles();
//                    if (files != null) {
                ObservableList<Register> registers = FXCollections.observableArrayList(map.values());
                ArrayList<TreeItem<Register>> collection = new ArrayList<>();

                for (Register r : registers) {
                    collection.add(new TreeItem<>(r));
                }

                return FXCollections.observableArrayList(collection);
//                    }
//                }
//
//                return FXCollections.emptyObservableList();
            }
        };
    }
}
