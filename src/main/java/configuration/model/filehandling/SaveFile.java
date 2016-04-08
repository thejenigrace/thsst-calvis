package configuration.model.filehandling;

import java.util.ArrayList;

/**
 * Created by Ivan on 1/26/2016.
 */
public class SaveFile{

	private String name;
	private ArrayList<String> location;

	public SaveFile(String name, ArrayList<String> location){
		this.name = name;
		this.location = location;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public ArrayList<String> getLocation(){
		return location;
	}

	public void setLocation(ArrayList<String> location){
		this.location = location;
	}
}
