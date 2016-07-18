package simulatorvisualizer.model.instructionanimation;

import configuration.model.engine.Calculator;
import configuration.model.engine.Memory;
import configuration.model.engine.RegisterList;
import configuration.model.engine.Token;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import simulatorvisualizer.model.CalvisAnimation;
import simulatorvisualizer.model.TimeLineFunction;

/**
 * Created by Goodwin Chua on 9 Jul 2016.
 */
public class Je extends CalvisAnimation {

    @Override
    public void animate(ScrollPane tab) {
        this.root.getChildren().clear();
        tab.setContent(root);
        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();
        TimeLineFunction timeFunc = new TimeLineFunction(timeline, root, registers, memory, finder);
        Calculator c = new Calculator(registers, memory);
        Token[] tokens = currentInstruction.getParameterTokens();
        Token des = tokens[0];
        String labelName = timeFunc.getValue(des, 0);
        ObservableList<Node> parent = this.root.getChildren();

        String conditionalTextResult = "";
        String proceedOrNot = "";
        if( registers.getEFlags().getZeroFlag().equals("1") ){
            conditionalTextResult = "ZF == 0";
            proceedOrNot = "Proceed To";
        }
        else{
            conditionalTextResult = "ZF != 0";
            proceedOrNot = "Don't Proceed To";
        }

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

        labelRec.setX(30 + 320);
        labelRec.setY(30);

        conditionLabelLabel.setX( 30 + labelRec.getLayoutBounds().getWidth()/2 - conditionLabelLabel.getLayoutBounds().getWidth() / 2 + 320);
        conditionLabelLabel.setY( 60);

        conditioLabelnVal.setX(30 + labelRec.getLayoutBounds().getWidth()/2 - conditioLabelnVal.getLayoutBounds().getWidth()/4 - 10 + 320);
        conditioLabelnVal.setWrappingWidth(labelRec.getLayoutBounds().getWidth() - 10);
        conditioLabelnVal.setY( 60 + 40);
        parent.addAll(labelRec, conditionLabelLabel, conditioLabelnVal);

        acceptanceVal.setX(200);
        acceptanceVal.setY(95);

        FadeTransition textFade = new FadeTransition(Duration.millis(1000), acceptanceVal);
        timeFunc.fadeText(textFade, acceptanceVal, 1.0, 0.1);
        textFade.play();

        timeline.setDelay(Duration.millis(2000));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
