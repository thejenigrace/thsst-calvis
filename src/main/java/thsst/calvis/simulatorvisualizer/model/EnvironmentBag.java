package thsst.calvis.simulatorvisualizer.model;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class EnvironmentBag {

    private String[][] registerStringArray;
    private String flagsValue;
    private String mxscrValue;
    private String[][] memoryArray;

    public EnvironmentBag(String[][] registerStringArray, String flagsValue,
                          String mxscrValue, String[][] memoryArray) {
        this.registerStringArray = registerStringArray;
        this.flagsValue = flagsValue;
        this.mxscrValue = mxscrValue;
        this.memoryArray = memoryArray;
    }

    public String[][] getRegisterStringArray() {
        return registerStringArray;
    }

    public String getFlagsValue() {
        return flagsValue;
    }

    public String getMxscrValue() {
        return mxscrValue;
    }

    public String[][] getMemoryArray() {
        return memoryArray;
    }
}
