package simulatorvisualizer.model;

import java.util.ArrayList;

/**
 * Created by Ivan on 7/10/2016.
 */
public class RotateModel{
	private String result;
	private String zeroFlag;

	public RotateModel(String result, String zeroFlag){
		this.result = result;
		this.zeroFlag = zeroFlag;
	}

	public String getResult(){
		return result;
	}

	public String getFlag(){
		return zeroFlag;
	}
}
