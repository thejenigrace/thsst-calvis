package EnvironmentConfiguration.controller;

import EnvironmentConfiguration.model.CALVISParser;
import EnvironmentConfiguration.model.InstructionList;
import EnvironmentConfiguration.model.Memory;
import EnvironmentConfiguration.model.RegisterList;
import MainEditor.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class EnvironmentConfigurator {

    private CALVISParser p;
    private Memory memory;
    private RegisterList registers;

    private MainApp mainApp;
    private Parent root;

    public EnvironmentConfigurator() throws IOException {
//        // 1. Setup the environment
//        this.memory = new Memory(16, filepaths.get(0));
//        this.registers = new RegisterList(filepaths.get(1));
//        InstructionList instructions = new InstructionList(filepaths.get(2));
//
//        // 2. Create the EnvironmentConfiguration.model.CALVISParser with Dynamic Grammar from
//        // the list of instructions and list of registers
//        // include the memory to bind instruction actions
//
//        this.p = new CALVISParser(instructions, registers, memory);
    }

    private void createEnvironmentBuilder() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/EnvironmentConfiguration/environmentBuilder.fxml"));
        root = (BorderPane) loader.load();

        EnvironmentBuilder eb = loader.getController();
        eb.setMainApp(mainApp);
    }

    public void begin() throws IOException {
        createEnvironmentBuilder();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public CALVISParser getParser(){
        return this.p;
    }

    public Memory getMemory() {
        return this.memory;
    }

    public RegisterList getRegisters() {
        return this.registers;
    }

    public Parent getParent() {
        return this.root;
    }
}
