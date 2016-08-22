package thsst.calvis.configuration.model.engine;

/**
 * Created by Goodwin Chua on 22 Aug 2016.
 */
public class StackPointerRegister extends Register {

    public StackPointerRegister(String name, int size) {
        super(name, size);
    }

    @Override
    public void initializeValue() {
        String regInitialValue = "FFFE";
        while ( regInitialValue.length() < this.size / 4 ) {
            regInitialValue = "0" + regInitialValue;
        }
        this.value = regInitialValue;
    }

}
