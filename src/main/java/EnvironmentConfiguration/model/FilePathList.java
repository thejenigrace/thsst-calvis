package EnvironmentConfiguration.model;

import java.util.ArrayList;

/**
 * Created by Ivan on 1/27/2016.
 */
public class FilePathList {

    private ArrayList<FilePath> filePathArrayList;
    private String category;

    public FilePathList(String category,ArrayList<FilePath> filePathArrayList){
        this.category = category;
        this.filePathArrayList = filePathArrayList;
    }

    public void add(FilePath filePathArrayList){
        this.filePathArrayList.add(filePathArrayList);
    }

    public void addAll(ArrayList<FilePath> filePathArrayList){
        this.filePathArrayList.addAll(filePathArrayList);
    }

    public ArrayList<FilePath> getAll(){
        return filePathArrayList;
    }

    public FilePath get(int index){
        return filePathArrayList.get(index);
    }

    public String getCategory(){
        return category;
    }

    public int size(){
        return filePathArrayList.size();
    }
}
