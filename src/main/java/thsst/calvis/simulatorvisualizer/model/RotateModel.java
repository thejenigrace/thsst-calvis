package thsst.calvis.simulatorvisualizer.model;

/**
 * Created by Ivan on 7/10/2016.
 */
public class RotateModel{
	private String result;
	private String carryFlag;

	public RotateModel(String result, String carryFlag){
		this.result = result;
		this.carryFlag = carryFlag;
	}

	public String getResult(){
		return result;
	}

	public String getFlag(){
		return carryFlag;
	}
}
