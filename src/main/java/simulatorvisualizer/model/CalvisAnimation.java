package simulatorvisualizer.model;

import configuration.model.engine.CalvisFormattedInstruction;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.Tab;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public abstract class CalvisAnimation {

    protected Timeline timeline;
    protected Group root;
    protected CalvisFormattedInstruction currentInstruction;
    protected EnvironmentBag oldEnvironment;
    protected EnvironmentBagFinder finder;

    public CalvisAnimation() {
        this.timeline = new Timeline();
        this.root = new Group();
        this.finder = new EnvironmentBagFinder();
    }

    public void setCurrentInstruction(CalvisFormattedInstruction currentInstruction) {
        this.currentInstruction = currentInstruction;
    }

    public void setOldEnvironment(EnvironmentBag environment) {
        this.oldEnvironment = environment;
        this.finder.setEnvironmentBag(environment);
        this.finder.setLookup(currentInstruction.getMemory().getLookupTable());
    }

    public abstract void animate(Tab tab);
}
