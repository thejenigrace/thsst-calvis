package MainEditor.controller;

import EnvironmentConfiguration.model.CALVISInstruction;
import EnvironmentConfiguration.model.CALVISParser;
import SimulatorVisualizer.controller.SimulationEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class SystemController {

    private CALVISParser parser;
    private SimulationEngine sim;

    public SystemController(CALVISParser parser, SimulationEngine sim){
        this.parser = parser;
        this.sim = sim;
    }

    public void play(String code) {
        System.out.println("Parsing: \n" + code);
        HashMap<String, CALVISInstruction> mappedInstruction = parser.parse(code);
        sim.beginSimulation(mappedInstruction);
    }

    public String[] getKeywords(){
        List<String> keywordsList = new ArrayList<>();

        Iterator<String> registerIterator = sim.getRegisterKeys();
       // Iterator<String> instructionIterator = parser.getInstructionKeys();

        populateKeywords(keywordsList, registerIterator);
       // populateKeywords(keywordsList, instructionIterator);

        String[] keywordsArray = new String[keywordsList.size()];
        keywordsArray = keywordsList.toArray(keywordsArray);
        return keywordsArray;
    }

    private void populateKeywords(List keywordsList, Iterator<String> iterator){
        while (iterator.hasNext()){
            String key = iterator.next();
            keywordsList.add(key.toUpperCase());
            keywordsList.add(key.toLowerCase());
        }
    }



}
