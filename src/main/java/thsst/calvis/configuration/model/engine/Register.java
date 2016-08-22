package thsst.calvis.configuration.model.engine;

public class Register {

    // size = number of bits
    protected String value;
    protected String name;
    protected int size;

    public Register() {

    }

    public Register(String name, int size) {
        this.name = name;
        this.size = size;
        initializeValue();
    }

    /*	populates register value with HEX value
        of 0s depending on size
     */
    public void initializeValue() {
        String regInitialValue = "";

        while ( regInitialValue.length() < this.size / 4 ) {
            regInitialValue = "0" + regInitialValue;
        }
        this.value = regInitialValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
