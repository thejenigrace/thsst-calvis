package thsst.calvis.editor.model;

import thsst.calvis.configuration.model.engine.InstructionList;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Goodwin Chua on 7 Jul 2016.
 */
public class KeywordBuilder {

    private RegisterList registerList;
    private Memory memory;
    private InstructionList instructionList;

    public KeywordBuilder(RegisterList registerList, Memory memory, InstructionList instructionList) {
        this.registerList = registerList;
        this.memory = memory;
        this.instructionList = instructionList;
    }

    public String[] getRegisterKeywords() {
        List<String> keywordsList = new ArrayList<>();
        Iterator<String> registerIterator = registerList.getRegisterKeys();
        populateKeywords(keywordsList, registerIterator);
        String[] keywordsArray = new String[keywordsList.size()];
        keywordsArray = keywordsList.toArray(keywordsArray);
        return keywordsArray;
    }

    public String[] getMemoryKeywords() {
        List<String> keywordsList = new ArrayList<>();
        Iterator<String> registerIterator = memory.getMemoryKeys();
        populateKeywords(keywordsList, registerIterator);
        String[] keywordsArray = new String[keywordsList.size()];
        keywordsArray = keywordsList.toArray(keywordsArray);
        return keywordsArray;
    }

    public String[] getInstructionKeywords() {
        List<String> keywordsList = new ArrayList<>();
        Iterator<String> instructionIterator = instructionList.getInstructionKeys();
        populateKeywords(keywordsList, instructionIterator);
        String[] instructionKeywords = new String[keywordsList.size()];
        instructionKeywords = keywordsList.toArray(instructionKeywords);
        return instructionKeywords;
    }

    /**
     * Method used to populate @param keywordsList with both upper case and
     * lower case keywords from @param iterator
     */
    private void populateKeywords(List keywordsList, Iterator<String> iterator) {
        while ( iterator.hasNext() ) {
            String key = iterator.next();
            keywordsList.add(key.toUpperCase());
            keywordsList.add(key.toLowerCase());
        }
    }
}
