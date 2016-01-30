package EnvironmentConfiguration.model.error_logging;

import java.util.ArrayList;

/**
 * Created by Ivan on 1/27/2016.
 */
public class ErrorLogger {
    private ArrayList<ErrorMessageList> errorMessageListArrayList;

    public ErrorLogger(ArrayList<ErrorMessageList> errorMessageLists){
        errorMessageListArrayList = errorMessageLists;
    }

    public void add(ErrorMessageList errorMessageList){
        errorMessageListArrayList.add(errorMessageList);
    }

    public int size(){
        return errorMessageListArrayList.size();
    }

    public ArrayList<ErrorMessageList> getAll(){
        return errorMessageListArrayList;
    }

    public ErrorMessageList get(int index){
        return errorMessageListArrayList.get(index);
    }

    public void addAll(ArrayList<ErrorMessageList> errorMessageListArrayList){
        this.errorMessageListArrayList.addAll(errorMessageListArrayList);
    }

    public void combineErrorLogger(ErrorLogger errorLogger){
        this.errorMessageListArrayList.addAll(errorLogger.getAll());
    }

    public void clearContents(){
        errorMessageListArrayList.clear();
    }
}
