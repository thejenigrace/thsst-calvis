package thsst.calvis.configuration.model.engine;

/**
 * Created by Goodwin Chua on 13 Jul 2016.
 */
public class FpuRegister extends Register {

    public FpuRegister(String name, int size) {
        super(name, size);
    }
    @Override
    public void initializeValue() {
        this.value = "0.0";
    }
}
