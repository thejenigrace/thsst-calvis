package thsst.calvis.editor.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import thsst.calvis.configuration.model.engine.Converter;

import java.net.URL;
import java.util.ResourceBundle;
/**
 * Created by Jennica on 19/07/2016.
 */
public class ConverterController implements Initializable {

    private static final int WORD = 0;
    private static final int DWORD = 1;
    private static final int QWORD = 2;

    @FXML
    private TextField textFieldInput;
    @FXML
    private TextField textFieldBinaryUpper;
    @FXML
    private TextField textFieldBinaryLower;
    @FXML
    private TextField textFieldSigned;
    @FXML
    private TextField textFieldUnsigned;
    @FXML
    private TextField textField32;
    @FXML
    private TextField textField64;
    @FXML
    private Button btnSize;
    @FXML
    private Button btnClear;

    private WorkspaceController workspaceController;
    private Stage dialogStage;

    private String[] sizes = {"WORD", "DWORD", "QWORD"};
    private int[] bitSizes = {4, 8, 16};
    private int currentSizeMode = 0;
    private SimpleStringProperty sizeMode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sizeMode = new SimpleStringProperty(sizes[currentSizeMode]);

        buildBinaryTextField(textFieldBinaryUpper);
        buildBinaryTextField(textFieldBinaryLower);
        buildDecimalTextField(textFieldSigned);
        buildDecimalTextField(textFieldUnsigned);

        // fired by every text property change
        textFieldInput.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    newValue = newValue.replaceAll("\\s", "");
                    if ( newValue.matches("[0-9a-fA-F]{0," + bitSizes[currentSizeMode] + "}") ) {
                        StringBuilder str = new StringBuilder(newValue.toUpperCase());
                        int idx = str.length() - 4;
                        while (idx > 0) {
                            str.insert(idx, " ");
                            idx -= 4;
                        }
                        Platform.runLater(() -> ((StringProperty) observable).setValue(str.toString()));
                        Platform.runLater(() -> textFieldInput.positionCaret(str.length()));
                        if ( newValue.equals("") ) {
                            convert("0");
                        } else {
                            convert(newValue);
                        }
                    } else {
                        ((StringProperty) observable).setValue(oldValue);
                    }
                }
        );

        btnSize.getStyleClass().add("converter");
        btnSize.getStylesheets().add("/css/converter.css");
        btnSize.textProperty().bind(sizeMode);

        btnClear.getStyleClass().add("converter");
        btnClear.getStylesheets().add("/css/converter.css");

        build();
    }

    private void buildBinaryTextField(TextField textField) {
        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    newValue = newValue.replaceAll("\\s", "");
                    StringBuilder str = new StringBuilder(newValue);
                    int idx = str.length() - 4;
                    while (idx > 0) {
                        str.insert(idx, " ");
                        idx -= 4;
                    }
                    ((StringProperty) observable).setValue(str.toString());
                }
        );
    }

    private void buildDecimalTextField(TextField textField) {
        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    newValue = newValue.replaceAll("\\s", "");
                    StringBuilder str = new StringBuilder(newValue);
                    int idx = str.length() - 3;
                    while (idx > 0) {
                        str.insert(idx, " ");
                        idx -= 3;
                    }
                    ((StringProperty) observable).setValue(str.toString());
                }
        );
    }

    private void build() {
        Platform.runLater(() -> {
            textFieldBinaryLower.setDisable(true);
            textField32.setDisable(true);
            textField64.setDisable(true);
            textFieldInput.requestFocus();
        });
    }

    private void convert(String hex) {
        if ( hex.length() > 0 ) {
            Converter converter = new Converter(hex);
            switch ( currentSizeMode ) {
                case WORD:
                    textFieldBinaryUpper.setText(converter.toBinaryString());
                    textFieldSigned.setText(converter.to16BitSignedInteger() + "");
                    textFieldUnsigned.setText(converter.to16BitUnsignedInteger() + "");
                    break;
                case DWORD:
                    textFieldBinaryUpper.setText(converter.toBinaryString());
                    textFieldSigned.setText(converter.to32BitSignedInteger() + "");
                    textFieldUnsigned.setText(converter.to32BitUnsignedInteger() + "");
                    if ( hex.length() == 8 ) {
                        textField32.setText(converter.toSinglePrecision() + "");
                    } else {
                        textField32.setText("0");
                    }
                    break;
                case QWORD:
                    String binaryString = converter.toBinaryString();
                    if ( binaryString.length() <= 32 ){
                        textFieldBinaryUpper.setText(binaryString);
                        textFieldBinaryLower.setText("0");
                    } else {
                        textFieldBinaryUpper.setText(binaryString.substring(0, 32));
                        textFieldBinaryLower.setText(binaryString.substring(32));
                    }
                    textFieldSigned.setText(converter.to64BitSignedInteger() + "");
                    textFieldUnsigned.setText(converter.to64BitUnsignedInteger());
                    if ( hex.length() == 16 ) {
                        textField64.setText(converter.toDoublePrecision() + "");
                    } else {
                        textField64.setText("0");
                    }
                    break;
            }
        }
    }


    public void setWorkspaceController(WorkspaceController workspaceController) {
        this.workspaceController = workspaceController;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void changeSize(ActionEvent event){
        currentSizeMode = (currentSizeMode + 1) % sizes.length;
        sizeMode.set(sizes[currentSizeMode]);
        String currentText = textFieldInput.getText();
        currentText = currentText.replaceAll(" ", "");
        switch ( currentSizeMode ) {
            case WORD:
                textFieldBinaryLower.setText("0");
                textFieldBinaryLower.setDisable(true);
                textField32.setText("0");
                textField32.setDisable(true);
                textField64.setText("0");
                textField64.setDisable(true);

                int length = currentText.length();
                if ( length > 4 ) {
                    textFieldInput.setText(currentText.substring(length - 4, length));
                }
                break;
            case DWORD:
                textFieldBinaryLower.setDisable(true);
                textField32.setDisable(false);
                textField64.setText("0");
                textField64.setDisable(true);
                convert(currentText);
                break;
            case QWORD:
                textFieldBinaryLower.setDisable(false);
                textField32.setText("0");
                textField32.setDisable(true);
                textField64.setDisable(false);
                convert(currentText);
                break;
        }
    }

    @FXML
    public void clear(ActionEvent event) {
        textFieldInput.clear();
    }

}
