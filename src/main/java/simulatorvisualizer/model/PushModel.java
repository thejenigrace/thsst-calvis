package simulatorvisualizer.model;

/**
 * Created by Ivan on 7/10/2016.
 */
public class PushModel{
	private String register;
	private String spValue;

	public PushModel(String result, String zeroFlag){
		this.register = result;
		this.spValue = zeroFlag;
	}

	public String getResult(){
		return register;
	}

	public String getSP(){
		return spValue;
	}
}
