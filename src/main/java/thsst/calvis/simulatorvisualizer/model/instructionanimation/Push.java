package thsst.calvis.simulatorvisualizer.model.instructionanimation;

import thsst.calvis.configuration.model.engine.Calculator;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;
import thsst.calvis.simulatorvisualizer.model.TimeLineFunction;

/**
 * Created by Goodwin Chua on 9 Jul 2016.
 */
public class Push extends CalvisAnimation {

    @Override
    public void animate(ScrollPane tab) {
        this.root.getChildren().clear();
        tab.setContent(root);
        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();
        TimeLineFunction timeFunc = new TimeLineFunction(timeline, root, registers, memory);
        Calculator c = new Calculator(registers, memory);
        Token[] tokens = currentInstruction.getParameterTokens();
        Token des = tokens[0];
        int operandSize = timeFunc.getBitSize(des);
        String desVal = timeFunc.getValue(des, operandSize);
        ObservableList<Node> parent = this.root.getChildren();
        Rectangle fake = timeFunc.createRectangle(0, 0, Color.WHITE);
        Rectangle desRec = timeFunc.createRectangle(140, 90, Color.web("#e9d66b"));
        Text desLabel = timeFunc.generateText(new Text(des.getValue()), 15, "#3d2b1f", FontWeight.NORMAL, "Elephant");
        Text desValue = timeFunc.generateText(new Text(desVal), 13, "#3d2b1f", FontWeight.NORMAL, "Elephant");
        Text desValueMoving = timeFunc.generateText(new Text(desVal), 13, "#3d2b1f", FontWeight.NORMAL, "Elephant");

        Rectangle stackRec = timeFunc.createRectangle(140, 90, Color.web("#6e7f80"));
        Text stackLabel = timeFunc.generateText(new Text("STACK"), 18, "#3d2b1f", FontWeight.EXTRA_BOLD, "Elephant");
        Text espLabel = timeFunc.generateText(new Text("ESP"), 15, "#b31b1b", FontWeight.BOLD, "Elephant");
        Text pushLabel = timeFunc.generateText(new Text("PUSHED VALUE"), 14, "#b31b1b", FontWeight.BOLD, "Elephant");
        Text espVal = timeFunc.generateText(new Text(registers.get("ESP")), 13, "#3d2b1f", FontWeight.NORMAL, "Elephant");
        Text insLabel = timeFunc.generateText(new Text("Value will be pushed to =>"), 15, "#b31b1b", FontWeight.BOLD, "Elephant");
        parent.addAll(insLabel, fake, desRec, desValue, desLabel, stackRec, espVal, stackLabel, desValueMoving, espLabel, pushLabel);
        //Timeline animations
        desValue.setX(desRec.getLayoutBounds().getWidth()/2 - desValue.getLayoutBounds().getWidth()/2 + 20);
        desValue.setY(80);

        desValueMoving.setVisible(false);

        timeFunc.addTimeline(desRec.getLayoutBounds().getWidth()/2 - desValueMoving.getLayoutBounds().getWidth()/2 + 20 , 80, 0, desValueMoving);
        timeFunc.addTimeline(stackRec.getLayoutBounds().getWidth()/2 - desValueMoving.getLayoutBounds().getWidth()/2 + 400 , 100, 3000, desValueMoving);

        desLabel.setX(desRec.getLayoutBounds().getWidth()/2 - desLabel.getLayoutBounds().getWidth()/2 + 20);
        desLabel.setY(50);

        stackRec.setX(400);
        stackRec.setY(20);

        espVal.setX(stackRec.getLayoutBounds().getWidth()/2 - espVal.getLayoutBounds().getWidth()/2 + 400);
        espVal.setY(55);

        stackLabel.setX(stackRec.getLayoutBounds().getWidth()/2 - stackLabel.getLayoutBounds().getWidth()/2 + 400);
        stackLabel.setY(15);

        desRec.setX(20);
        desRec.setY(20);

        espLabel.setX(stackRec.getLayoutBounds().getWidth()/2 - espLabel.getLayoutBounds().getWidth()/2 + 400);
        espLabel.setY(35);

        pushLabel.setX(stackRec.getLayoutBounds().getWidth()/2 - pushLabel.getLayoutBounds().getWidth()/2 + 400);
        pushLabel.setY(80);

        insLabel.setX(stackRec.getLayoutBounds().getWidth()/2 + 100 + 5);
        insLabel.setY(70);

        timeFunc.hideshowElement(desValueMoving, 1000 / 5, true);
        timeline.setDelay(Duration.millis(2000));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
