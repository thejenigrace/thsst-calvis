package MainEditor.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Jennica on 10/4/15.
 */
public class Register {

    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleStringProperty value = new SimpleStringProperty("");

    public Register() {
        this("", "");
    }

    public Register(String name, String value) {
        setName(name);
        setValue(value);
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
}
