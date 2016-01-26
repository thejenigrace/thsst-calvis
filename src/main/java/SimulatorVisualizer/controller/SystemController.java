package SimulatorVisualizer.controller;

import EnvironmentConfiguration.controller.EnvironmentConfigurator;
import EnvironmentConfiguration.model.*;
import MainEditor.model.AssemblyComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Goodwin Chua on 12/11/2015.
 */
public class SystemController {

    private EnvironmentConfigurator environment;
    private RegisterList registerList;
    private InstructionList instructionList;
    private Memory memory;
    private CALVISParser parser;
    private List<AssemblyComponent> observerList;

    public SystemController(EnvironmentConfigurator ec){
        this.environment = ec;
        this.parser = environment.getParser();
        this.registerList = environment.getRegisters();
        this.instructionList = environment.getInstructions();
        this.memory = environment.getMemory();
        this.observerList = new ArrayList<>();
    }

    public void attach(AssemblyComponent observer){
        observer.setSysCon(this);
        observerList.add(observer);
    }

    public void notifyAllObservers(){
        observerList.forEach(AssemblyComponent::update);
    }

    public RegisterList getRegisterState(){
        return this.registerList;
    }

    public Memory getMemoryState(){
        return this.memory;
    }

    public void play(String code) {
        System.out.println("Parsing: \n" + code);
        HashMap<String, CALVISInstruction> mappedInstruction = parser.parse(code);
        this.registerList.clear();
        this.memory.clear();
        beginSimulation(mappedInstruction);
    }

    private void beginSimulation(HashMap<String, CALVISInstruction> m){
        HashMap<String, CALVISInstruction> map = m;
        Iterator<String> keys = map.keySet().iterator();

        while (map.containsKey(environment.getRegisters().get("EIP"))){
            String currentLine = environment.getRegisters().get("EIP");
            System.out.println("EIP: "+ currentLine);
            map.get(currentLine).execute(); // EXECUTE THE CALVIS INSTRUCTION
            int value = Integer.parseInt(currentLine, 16);
            value++;
            environment.getRegisters().set("EIP", Integer.toHexString(value));

            notifyAllObservers();
        }
    }

    public String[] getKeywords(){
        List<String> keywordsList = new ArrayList<>();

        Iterator<String> registerIterator = registerList.getRegisterKeys();
        Iterator<String> instructionIterator = instructionList.getInstructionKeys();

        populateKeywords(keywordsList, registerIterator);
        populateKeywords(keywordsList, instructionIterator);

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
