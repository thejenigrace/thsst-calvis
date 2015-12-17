package MainEditor.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Jennica on 10/4/15.
 */
public class Memory {

    private SimpleStringProperty address = new SimpleStringProperty("");
    private SimpleStringProperty representation = new SimpleStringProperty("");
    private SimpleStringProperty instruction = new SimpleStringProperty("");

    public Memory() {
        this("", "", "");
    }

    public Memory(String address, String representation, String instruction) {
        setAddress(address);
        setRepresentation(representation);
        setInstruction(instruction);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getRepresentation() {
        return representation.get();
    }

    public void setRepresentation(String representation) {
        this.representation.set(representation);
    }

    public String getInstruction() {
        return instruction.get();
    }

    public void setInstruction(String instruction) {
        this.instruction.set(instruction);
    }
}
