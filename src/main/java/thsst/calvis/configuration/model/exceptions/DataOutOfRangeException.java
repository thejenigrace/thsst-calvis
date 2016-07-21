package thsst.calvis.configuration.model.exceptions;

/**
 * Created by Ivan on 7/21/2016.
 */
public class DataOutOfRangeException extends Exception {

	public DataOutOfRangeException(String first) {
		super("Values can only be up to 0xFFFFFFFFFFFFFFFF " + first);
	}

}