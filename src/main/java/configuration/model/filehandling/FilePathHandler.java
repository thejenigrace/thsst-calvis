package configuration.model.filehandling;

import java.util.ArrayList;

/**
 * Created by Ivan on 1/27/2016.
 */
public class FilePathHandler{

	private ArrayList<ArrayList<String>> filePaths;
	private ArrayList<Integer> selectedIndexes;

	public FilePathHandler(ArrayList<ArrayList<String>> filePaths, ArrayList<Integer> selectedIndexes){
		this.filePaths = filePaths;
		this.selectedIndexes = selectedIndexes;
	}

	public ArrayList<ArrayList<String>> getFilePaths(){
		return filePaths;
	}

	public void setFilePaths(ArrayList<ArrayList<String>> filePaths){
		this.filePaths = filePaths;
	}

	public ArrayList<Integer> getSelectedIndexes(){
		return selectedIndexes;
	}

	public void setSelectedIndexes(ArrayList<Integer> selectedIndexes){
		this.selectedIndexes = selectedIndexes;
	}
}
