package simulatorvisualizer.model.instructionanimation;

import configuration.model.engine.Memory;
import configuration.model.engine.RegisterList;
import configuration.model.engine.Token;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import simulatorvisualizer.model.CalvisAnimation;
import simulatorvisualizer.model.TimeLineFunction;

/**
 * Created by Goodwin Chua on 9 Jul 2016.
 */
public class Cmpxchg extends CalvisAnimation {

    @Override
    public void animate(ScrollPane tab) {
        this.root.getChildren().clear();
        tab.setContent(root);

        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();
        TimeLineFunction timeFunc = new TimeLineFunction(timeline, root);

        Token[] tokens = currentInstruction.getParameterTokens();

        int desSize = 0;
        if ( tokens[0].getType() == Token.REG )
            desSize = registers.getBitSize(tokens[0]);
        else if ( tokens[0].getType() == Token.MEM && tokens[1].getType() == Token.REG )
            desSize = registers.getBitSize(tokens[1]);
        else
            desSize = memory.getBitSize(tokens[0]);

        // ANIMATION ASSETS
        Rectangle srcRectangle = this.createRectangle(tokens[0], 90, 110);
        Rectangle fake = new Rectangle(1, 1, Color.WHITE);
        Rectangle eaxRectangle = new Rectangle(90, 110, Color.web("#e32636"));
        Rectangle desRectangle = this.createRectangle(tokens[1], 90, 110);
        Text plusCircle = new Text("Compare");
        plusCircle.setFont(Font.font(24));
        plusCircle.setFill(Color.web("#3d2b1f"));

        Circle equalCircle = new Circle(30, Color.web("#ffe135"));
        Text srcLabel = this.createLabelText(tokens[0]);
        srcLabel.setFont(Font.font(15));
        srcLabel.setFill(Color.web("#3d2b1f"));
        Text desLabel = this.createLabelText(tokens[1]);
        desLabel.setFont(Font.font(15));
        desLabel.setFill(Color.web("#3d2b1f"));

        Text aLabel = new Text("");
        switch(desSize){
            case 8:
                aLabel = new Text("AL");
                break;
            case 16:
                aLabel = new Text("AX");
                break;
            case 32:
                aLabel = new Text("EAX");
                break;
        }

        // Add them to root ( this is your canvas)
        root.getChildren().add(srcRectangle);
        root.getChildren().add(fake);
        root.getChildren().add(eaxRectangle);
        root.getChildren().add(plusCircle);
        root.getChildren().add(equalCircle);
        root.getChildren().add(desRectangle);
        root.getChildren().add(srcLabel);
        root.getChildren().add(desLabel);
        root.getChildren().add(aLabel);

        Text equalSign = new Text("=");
        timeFunc.editText(equalSign, 30, "#3d2b1f");

        Text srcValue = this.createValueText(tokens[0], registers, memory, desSize);
        timeFunc.editText(srcValue, 12, "#3d2b1f");

        Text desValue = this.createValueText(tokens[1], registers, memory, desSize);
        timeFunc.editText(desValue, 12, "#3d2b1f");

        Text eaxValue = new Text(registers.get(aLabel.getText()));
        timeFunc.editText(eaxValue, 12, "#3d2b1f");

        aLabel.setFont(Font.font(14));
        aLabel.setFill(Color.web("#3d2b1f"));



        // Basic timeline
        timeFunc.addTimeline(0, 0, Duration.ZERO, fake);
        timeFunc.addTimeline(0, 0, 100, fake);
        timeFunc.addTimeline(200, 50, Duration.ZERO, eaxRectangle);
        timeFunc.addTimeline(200, 50, 100, eaxRectangle);
        timeFunc.addTimeline(0, 50, Duration.ZERO, srcRectangle);
        timeFunc.addTimeline(0, 50, 100, srcRectangle);
        timeFunc.addTimeline(100, 110, Duration.ZERO, plusCircle);
        timeFunc.addTimeline(100, 110, 100, plusCircle);
        timeFunc.addTimeline(345, 100, Duration.ZERO, equalCircle);
        timeFunc.addTimeline(345, 100, 100, equalCircle);
        timeFunc.addTimeline(340, 105, Duration.ZERO, equalSign);
        timeFunc.addTimeline(340, 105, 100, equalSign);
        timeFunc.addTimeline(400, 50, Duration.ZERO, desRectangle);
        timeFunc.addTimeline(400, 50, 100, desRectangle);
        timeFunc.addTimeline(30, 80, Duration.ZERO, srcLabel);
        timeFunc.addTimeline(30, 80, 100, srcLabel);
        timeFunc.addTimeline(230, 80, Duration.ZERO, aLabel);
        timeFunc.addTimeline(230, 80, 100, aLabel);
        timeFunc.addTimeline(430, 80, Duration.ZERO, desLabel);
        timeFunc.addTimeline(430, 80, 100, desLabel);
        timeFunc.addTimeline(desSize / 16 * 1 + (32 - desSize / 16 ) * .75, 110, Duration.ZERO, srcValue);
        timeFunc.addTimeline(desSize / 16 * 1 + (32 - desSize / 16) * .75, 110, 100, srcValue);
        timeFunc.addTimeline(desSize / 16 * 1 + (32 - desSize / 16 ) * .75 + 200, 110, Duration.ZERO, eaxValue);
        timeFunc.addTimeline(desSize / 16 * 1 + (32 - desSize / 16) * .75 + 200, 110, 100, eaxValue);
        timeFunc.addTimeline(desSize / 16 * 1 + (32 - desSize / 16 ) * .75 + 400, 110, Duration.ZERO, desValue);
        timeFunc.addTimeline(desSize / 16 * 1 + (32 - desSize / 16) * .75 + 400, 110, 100, desValue);
        timeline.play();
    }
}
