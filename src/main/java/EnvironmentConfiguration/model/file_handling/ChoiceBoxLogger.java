package EnvironmentConfiguration.model.file_handling;

import java.util.ArrayList;

/**
 * Created by Ivan on 1/27/2016.
 */
public class ChoiceBoxLogger {

    private ArrayList<ArrayList<String>> choiceBoxLists;

    public ChoiceBoxLogger(ArrayList<ArrayList<String>> choiceBoxLists) {
        this.choiceBoxLists = choiceBoxLists;
    }

    public ArrayList<String> get(int index) {
        return choiceBoxLists.get(index);
    }

    public void add(ArrayList<String> list) {
        choiceBoxLists.add(list);
    }

    public int size() {
        return choiceBoxLists.size();
    }
}
