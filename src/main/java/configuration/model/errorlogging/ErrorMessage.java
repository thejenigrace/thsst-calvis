package configuration.model.errorlogging;

import java.util.ArrayList;

/**
 * Created by Ivan on 12/29/2015.
 */
public class ErrorMessage{

	private Types type;
	private ArrayList<String> variables;
	private String errorLine;

	public ErrorMessage(Types type, ArrayList<String> variables, String errorLine){
		this.type = type;
		this.variables = variables;
		this.errorLine = errorLine;
	}

	public Types getType(){
		return type;
	}

	public void setType(Types type){
		this.type = type;
	}

	public ArrayList<String> getVariables(){
		return variables;
	}


	public String getErrorLine(){
		return errorLine;
	}

	public void setErrorLine(String ErrorLine){
		this.errorLine = ErrorLine;
	}

}
