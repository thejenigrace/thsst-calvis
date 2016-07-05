package simulatorvisualizer.model;

import simulatorvisualizer.model.instructionanimation.Mov;

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
    }

}
