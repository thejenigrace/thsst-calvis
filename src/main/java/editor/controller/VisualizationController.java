package editor.controller;

import configuration.model.engine.CalvisFormattedInstruction;
import editor.model.AssemblyComponent;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import simulatorvisualizer.model.CalvisAnimation;
import simulatorvisualizer.model.instructionanimation.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jennica on 07/02/2016.
 */
public class VisualizationController extends AssemblyComponent {

    private static final double animationX = 500;
    private static final double animationY = 130;

    private Tab tab;
    private CalvisFormattedInstruction currentInstruction;
    private HashMap<String, CalvisAnimation> animationMap;


    public VisualizationController() {
        this.tab = new Tab();
        this.tab.setText("Visualization");
    }

    public Tab getTab() {
        return tab;
    }

    @Override
    public void update(CalvisFormattedInstruction currentInstruction, int lineNumber) {
        attachCalvisInstruction(currentInstruction);
        Platform.runLater(
                new Thread() {
                    public void run() {
                        tab.setContent(null);
                        animate();
                    }
                }
        );

    }

    public void animate() {
        // traverse through the list
        // find the appropriate animation for currentInstruction
        String name = this.currentInstruction.getName();

        CalvisAnimation animation = this.animationMap.get(name);

        if ( animation == null ) {
            // no animation
        } else {
            animation.setCurrentInstruction(this.currentInstruction);
            animation.animate(tab);
        }
    }

    @Override
    public void refresh() {
//        this.tab.setContent(root);
    }

    @Override
    public void build() {
        // instantiate animation classes
        buildAnimations();
    }

    private void buildAnimations() {
        // LIST
        this.animationMap = new HashMap<>();
        this.animationMap.put("MOV", new Mov());
    }

    public void attachCalvisInstruction(CalvisFormattedInstruction CalvisInstruction) {
        this.currentInstruction = CalvisInstruction;
    }

}
