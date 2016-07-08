package simulatorvisualizer.model;

import simulatorvisualizer.model.instructionanimation.*;

import java.util.HashMap;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class AnimationMap extends HashMap<String, CalvisAnimation> {

    public AnimationMap(){
        super();
        generateGP();
    }

    private void generateGP() {
        generateGPDataTransfer();
    }

    private void generateGPDataTransfer(){
        this.put("MOV", new Mov());
        this.put("STC", new Stc());
        this.put("CLC", new Clc());
        this.put("CMC", new Cmc());
        this.put("CLD", new Cld());
        this.put("STD", new Std());
        this.put("STI", new Sti());
        this.put("CLI", new Cli());
        this.put("SAHF", new Sahf());
        this.put("LAHF", new Lahf());
        this.put("PUSHF", new Pushf());
        this.put("PUSHFD", new Pushfd());
        this.put("POPF", new Popf());
        this.put("POPFD", new Popfd());
        this.put("AND", new And());
        this.put("OR", new Or());
        this.put("XOR", new Xor());
        this.put("NOT", new Not());
        this.put("TEST", new Test());
    }

}
