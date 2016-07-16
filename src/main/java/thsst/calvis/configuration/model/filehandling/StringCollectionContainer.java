package thsst.calvis.configuration.model.filehandling;

import java.util.ArrayList;

/**
 * Created by Ivan on 1/29/2016.
 */
public class StringCollectionContainer {

    public ArrayList<String> strArray;

    public StringCollectionContainer(String... variables) {
        strArray = new ArrayList<String>();
        for (int x = 0; x < variables.length; x++) {
            strArray.add(variables[x]);
        }
    }

    public ArrayList<String> getStrArray() {
        return strArray;
    }
}
