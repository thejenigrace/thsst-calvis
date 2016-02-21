package EnvironmentConfiguration.model.engine;

/**
 * Created by Goodwin Chua on 2/21/2016.
 */
public class MissingSizeDirectiveException extends Exception {
	public MissingSizeDirectiveException(String address, int line) {
		super("Missing size directive for memory token: [" + address + "] at line number: " + line);
	}
}
