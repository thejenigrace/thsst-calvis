package thsst.calvis.configuration.model.errorlogging;

import thsst.calvis.configuration.model.filehandling.FilePath;
import thsst.calvis.configuration.model.filehandling.FilePathList;

import java.util.ArrayList;

/**
 * Created by Ivan on 1/27/2016.
 */
public class FilePathLogger{

	private ArrayList<FilePathList> filePathListArrayList;

	public FilePathLogger(ArrayList<FilePathList> filePathListArrayList){
		this.filePathListArrayList = filePathListArrayList;
	}

	public void add(FilePathList filePathList){
		this.filePathListArrayList.add(filePathList);
	}

	public void add(String category, ArrayList<FilePath> filePathArrayList){
		this.filePathListArrayList.add(new FilePathList(category, filePathArrayList));
	}

	public void addAll(ArrayList<FilePathList> arrayPathList){
		this.filePathListArrayList.addAll(arrayPathList);
	}

	public ArrayList<FilePathList> getAll(){
		return this.filePathListArrayList;
	}

	public FilePathList get(int index){
		return filePathListArrayList.get(index);
	}
}
