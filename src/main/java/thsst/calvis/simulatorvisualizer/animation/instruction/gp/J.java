package thsst.calvis.simulatorvisualizer.animation.instruction.gp;

import thsst.calvis.configuration.model.engine.Calculator;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;
import thsst.calvis.simulatorvisualizer.model.JumperFunction;
import thsst.calvis.simulatorvisualizer.model.TimeLineFunction;

import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 9 Jul 2016.
 */
public class J extends CalvisAnimation {

    @Override
    public void animate(ScrollPane tab) {
        this.root.getChildren().clear();
        tab.setContent(root);
        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();
        TimeLineFunction timeFunc = new TimeLineFunction(timeline, root, registers, memory);
        Calculator c = new Calculator(registers, memory);
        Token[] tokens = currentInstruction.getParameterTokens();
        Token des = tokens[1];
        Token cc = tokens[0];
        String labelName = timeFunc.getValue(des, 0);
        String jumpMode = "J" + cc.getValue();
        System.out.println(labelName  + " fuck " + jumpMode);
        ObservableList<Node> parent = this.root.getChildren();

        String conditionalTextResult = "";
        String proceedOrNot = "";

        JumperFunction jc = new JumperFunction(registers, memory);
        ArrayList<String> getFlags = jc.getFlags(jumpMode);
        String[] flagValues = new String[getFlags.size()];
        flagValues = getFlags.toArray(flagValues);
        boolean isPassable = jc.computeJump(jumpMode, flagValues);

        if(isPassable){
                conditionalTextResult = jc.computeJumpText(isPassable, jumpMode);
                proceedOrNot = "Proceed To";
        }
        else{
                conditionalTextResult = jc.computeJumpText(isPassable, jumpMode);
                proceedOrNot = "Don't Proceed To";
        }

        System.out.println(conditionalTextResult);
        System.out.println(proceedOrNot);

        Rectangle fake = timeFunc.createRectangle(0, 0, Color.WHITE);
        Rectangle fromRec = timeFunc.createRectangle(140, 130, Color.web("#002e63"));
        Text conditionLabel = timeFunc.generateText(new Text("Condition"), 15, "#fffaf0", FontWeight.EXTRA_BOLD, "Georgia");
        Text conditionVal = timeFunc.generateText(new Text(conditionalTextResult), 14, "#fffaf0", FontWeight.NORMAL, "Georgia");
        Text acceptanceVal = timeFunc.generateText(new Text(proceedOrNot), 20, "#0bda51", FontWeight.EXTRA_BOLD, "Georgia");
        parent.addAll(fake, fromRec, conditionLabel, conditionVal, acceptanceVal);

        //label functions
        Rectangle labelRec = timeFunc.createRectangle(140, 130, Color.web("#c04000"));
        Text conditionLabelLabel = timeFunc.generateText(new Text("Label Name"), 15, "#fffaf0", FontWeight.EXTRA_BOLD, "Georgia");
        Text conditioLabelnVal = timeFunc.generateText(new Text(labelName), 14, "#fffaf0", FontWeight.NORMAL, "Georgia");

        fromRec.setX(30);
        fromRec.setY(30);

        conditionLabel.setX( 30 + fromRec.getLayoutBounds().getWidth()/2 - conditionLabel.getLayoutBounds().getWidth() / 2 );
        conditionLabel.setY( 60);

        conditionVal.setX(30 + fromRec.getLayoutBounds().getWidth()/2 - conditionVal.getLayoutBounds().getWidth()/4 - 10 );
        conditionVal.setWrappingWidth(fromRec.getLayoutBounds().getWidth() - 10);
        conditionVal.setY( 60 + 40);

        labelRec.setX(30 + 350);
        labelRec.setY(30);

        conditionLabelLabel.setX( 30 + labelRec.getLayoutBounds().getWidth()/2 - conditionLabelLabel.getLayoutBounds().getWidth() / 2 + 350);
        conditionLabelLabel.setY( 60);

        conditioLabelnVal.setX(30 + labelRec.getLayoutBounds().getWidth()/2 - conditioLabelnVal.getLayoutBounds().getWidth()/4 - 10 + 350);
        conditioLabelnVal.setWrappingWidth(labelRec.getLayoutBounds().getWidth() - 10);
        conditioLabelnVal.setY( 60 + 40);
        parent.addAll(labelRec, conditionLabelLabel, conditioLabelnVal);

        acceptanceVal.setX(labelRec.getX()/2 - fromRec.getX()/2);
        acceptanceVal.setY(95);

        FadeTransition textFade = new FadeTransition(Duration.millis(1000), acceptanceVal);
        timeFunc.fadeText(textFade, acceptanceVal, 1.0, 0.1);
        textFade.play();
        if(isPassable){
            acceptanceVal.setFill(Color.web("#0bda51"));
        }
        else{
            acceptanceVal.setFill(Color.web("#e62020"));
        }
        timeline.setDelay(Duration.millis(2000));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
