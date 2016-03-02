package editor.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Goodwin Chua on 1/30/2016.
 */
public class FlagUI {

    private SimpleStringProperty name;
    private SimpleStringProperty flagValue;

    public FlagUI(String name, String flagValue) {
        this.name = new SimpleStringProperty(name);
        this.flagValue = new SimpleStringProperty(flagValue);
    }

    public String getFlagValue() {
        return this.flagValue.get();
    }

    public void setFlagValue(String val) {
        this.flagValue.set(val);
    }

    public String getName() {
        return this.name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public String toString() {
        return (name.get() + " : " + flagValue.get());
    }

}
