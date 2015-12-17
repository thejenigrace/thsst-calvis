package MainEditor.model;

/**
 * Created by Jennica on 10/4/15.
 */
public class Flag {

    private String name;
    private String value;

    public Flag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
