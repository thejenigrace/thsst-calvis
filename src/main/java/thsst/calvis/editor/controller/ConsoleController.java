package thsst.calvis.editor.controller;

import thsst.calvis.configuration.model.engine.CalvisFormattedInstruction;
import thsst.calvis.editor.view.AssemblyComponent;
import thsst.calvis.editor.view.ConsoleTextArea;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;

/**
 * Created by Jennica on 07/02/2016.
 */
public class ConsoleController extends AssemblyComponent {

    private ConsoleTextArea textArea;
    private Tab tab;
    private int lineBefore;
    private CalvisFormattedInstruction currentInstruction;

    public ConsoleController() {
        this.lineBefore = 0;
        this.textArea = new ConsoleTextArea(false);
        this.tab = new Tab();
        this.tab.setText("Console");
        this.tab.setContent(textArea);
    }

    public Tab getTab() {
        return tab;
    }

    @Override
    public void update(CalvisFormattedInstruction currentInstruction, int lineNumber) {
        if ( currentInstruction != null) {
            String name = currentInstruction.getName();
            if ( name.matches("PRINTF|SCANF|CLS") ) {
                attachCalvisInstruction(currentInstruction);
                currentInstruction.setConsole(this);
                currentInstruction.getInstruction().consoleExecute(sysCon.getRegisterState(),
                        sysCon.getMemoryState(), this);
            }
        }
    }

    @Override
    public void refresh() {
        cls();
    }

    @Override
    public void build() {
        this.textArea.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER && textArea.getState() )  {
                if ( currentInstruction != null ) {
                    try {
                        currentInstruction.executeScan();
                    } catch ( Exception e ) {
                        sysCon.reportError(e);
                    }
                }
            }
        });
    }

    public void printf(String text){
        this.textArea.setState(true);
        this.textArea.appendText(text);
        this.textArea.setState(false);
    }

    public void scanf(){
        this.sysCon.pauseFromConsole();
        this.textArea.setState(true);
        lineBefore = textArea.getText().length();
    }

    public String retrieveScanned() {
        String text = textArea.getText();
        text = textArea.getText(lineBefore, text.length());
        this.textArea.setState(false);
        this.sysCon.resumeFromConsole();
        return text;
    }

    public void cls(){
        this.textArea.setState(true);
        this.textArea.clear();
        this.textArea.setState(false);
        this.lineBefore = 0;
    }

    public void stopConsole() {
        this.textArea.setState(false);
    }

    public void attachCalvisInstruction(CalvisFormattedInstruction CalvisInstruction) {
        this.currentInstruction = CalvisInstruction;
    }

}
