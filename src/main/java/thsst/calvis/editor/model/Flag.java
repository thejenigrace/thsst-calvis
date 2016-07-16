package thsst.calvis.editor.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Goodwin Chua on 1/30/2016.
 */
public class Flag {

    private SimpleStringProperty name;
    private SimpleStringProperty value;

    public Flag(String name, String value) {
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleStringProperty(value);
    }

    public String getName() {
        return this.name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getValue() {
        return this.value.get();
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    @Override
    public String toString() {
        return (name.get() + " : " + value.get());
    }

}
