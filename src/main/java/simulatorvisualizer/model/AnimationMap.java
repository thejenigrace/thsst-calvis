package simulatorvisualizer.model;

import simulatorvisualizer.model.instructionanimation.*;

import java.util.HashMap;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class AnimationMap extends HashMap<String, CalvisAnimation> {

    public AnimationMap() {
        super();
        this.generateGP();
    }

    private void generateGP() {
        this.generateGPDataTransfer();
        this.generateGPBinaryArithmetic();
    }

    private void generateGPDataTransfer() {
        this.put("MOV", new Mov());
        this.put("LEA", new Lea());
        this.put("XLAT", new Xlat());
        this.put("CMOV", new Cmov());
    }

    private void generateGPBinaryArithmetic() {
        this.put("ADD", new Add());
    }
}
