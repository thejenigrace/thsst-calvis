package simulatorvisualizer.model.instructionanimation;

import configuration.model.engine.Calculator;
import configuration.model.engine.Memory;
import configuration.model.engine.RegisterList;
import configuration.model.engine.Token;
import configuration.model.exceptions.MemoryReadException;
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
        TimeLineFunction timeFunc = new TimeLineFunction(timeline, root, registers, memory, finder);
        Calculator c = new Calculator(registers, memory);
        Token[] tokens = currentInstruction.getParameterTokens();

        int desSize = 0;
        if ( tokens[0].getType() == Token.REG )
            desSize = registers.getBitSize(tokens[0]);
        else if ( tokens[0].getType() == Token.MEM && tokens[1].getType() == Token.REG )
            desSize = registers.getBitSize(tokens[1]);
        else
            desSize = memory.getBitSize(tokens[0]);

        // ANIMATION ASSETS
        Rectangle srcRectangle = this.createRectangle(tokens[0], 100, 110);
        Rectangle fake = new Rectangle(1, 1, Color.WHITE);
        Rectangle eaxRectangle = new Rectangle(100, 110, Color.web("#e32636"));
        Rectangle desRectangle = this.createRectangle(tokens[1], 100, 110);
        Text plusCircle = new Text("Compare");

        Circle equalCircle = new Circle(30, Color.web("#ffe135"));
        Text srcLabel = this.createLabelText(tokens[0]);
        srcLabel.setFont(Font.font(15));
        srcLabel.setFill(Color.web("#3d2b1f"));
        Text desLabel = this.createLabelText(tokens[1]);
        desLabel.setFont(Font.font(15));
        desLabel.setFill(Color.web("#3d2b1f"));
        Text zeroValue = new Text("Zero Flag Value is set to: " + registers.getEFlags().getZeroFlag());
        timeFunc.editText(zeroValue, 24, "#3d2b1f");


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
//        root.getChildren().add(plusCircle);
        root.getChildren().add(equalCircle);
        root.getChildren().add(desRectangle);
        root.getChildren().add(srcLabel);
        root.getChildren().add(desLabel);
        root.getChildren().add(aLabel);

        Text equalSign = new Text("=");
        timeFunc.editText(equalSign, 30, "#3d2b1f");

        Text srcValue = new Text("0x" + finder.getRegister(tokens[1].getValue()));
        timeFunc.editText(srcValue, 12, "#3d2b1f");

        String desVal = "";
        if(tokens[0].isMemory()){
            try{
                desVal = finder.read(tokens[0], desSize);
            } catch(MemoryReadException e){
                e.printStackTrace();
            }
        }
        else{
            desVal = finder.getRegister(tokens[0].getValue());
        }

        Text desValue = new Text("0x" + desVal);
        timeFunc.editText(desValue, 12, "#3d2b1f");
        Text eaxValue = new Text("0x" + finder.getRegister(aLabel.getText()));
        timeFunc.editText(eaxValue, 12, "#3d2b1f");

        aLabel.setFont(Font.font(14));
        aLabel.setFill(Color.web("#3d2b1f"));

        if(eaxValue.getText().equals(desValue.getText())){
            timeFunc.editText(plusCircle, 22, "#3d2b1f", "Is Equal");
            srcValue.setText("0x0" + registers.get(tokens[1].getValue()));
//            desLabel.setText(desLabel.getText());
//            srcLabel.setText(desLabel.getText());
            desLabel.setText(srcLabel.getText());
            desRectangle.setFill(srcRectangle.getFill());

        }
        else{
            timeFunc.editText(plusCircle, 22, "#3d2b1f", "Not Equal");
            srcValue.setText(desValue.getText());
            desRectangle.setFill(eaxRectangle.getFill());
            desLabel.setText(aLabel.getText());
        }


        // Basic timeline
        timeFunc.addTimeline(0, 0, Duration.ZERO, fake);
        timeFunc.addTimeline(0, 0, 100, fake);
        timeFunc.addTimeline(200, 50, Duration.ZERO, eaxRectangle);
        timeFunc.addTimeline(200, 50, 100, eaxRectangle);
        timeFunc.addTimeline(50, 220, Duration.ZERO, zeroValue);
        timeFunc.addTimeline(50, 220, 100, zeroValue);
        timeFunc.addTimeline(0, 50, Duration.ZERO, srcRectangle);
        timeFunc.addTimeline(0, 50, 100, srcRectangle);
        timeFunc.addTimeline(100, 110, Duration.ZERO, plusCircle);
        timeFunc.addTimeline(100, 110, 100, plusCircle);
        timeFunc.addTimeline(345, 100, Duration.ZERO, equalCircle);
        timeFunc.addTimeline(345, 100, 100, equalCircle);
        timeFunc.addTimeline(335, 110, Duration.ZERO, equalSign);
        timeFunc.addTimeline(335, 110, 100, equalSign);
        timeFunc.addTimeline(400, 50, Duration.ZERO, desRectangle);
        timeFunc.addTimeline(400, 50, 100, desRectangle);
        timeFunc.addTimeline(20, 80, Duration.ZERO, srcLabel);
        timeFunc.addTimeline(20, 80, 100, srcLabel);
        timeFunc.addTimeline(220, 80, Duration.ZERO, aLabel);
        timeFunc.addTimeline(220, 80, 100, aLabel);
        timeFunc.addTimeline(420, 80, Duration.ZERO, desLabel);
        timeFunc.addTimeline(420, 80, 100, desLabel);
        timeFunc.addTimeline(desSize / 16 * 1 + (32 - desSize / 16 ) * .75, 110, Duration.ZERO, desValue);
        timeFunc.addTimeline(desSize / 16 * 1 + (32 - desSize / 16) * .75, 110, 100, desValue);
        timeFunc.addTimeline(desSize / 16 * 1 + (32 - desSize / 16 ) * .75 + 200, 110, Duration.ZERO, eaxValue);
        timeFunc.addTimeline(desSize / 16 * 1 + (32 - desSize / 16) * .75 + 200, 110, 100, eaxValue);
        timeFunc.addTimeline(desSize / 16 * 1 + (32 - desSize / 16 ) * .75 + 400, 110, Duration.ZERO, srcValue);
        timeFunc.addTimeline(desSize / 16 * 1 + (32 - desSize / 16) * .75 + 400, 110, 100, srcValue);
        System.out.println(srcLabel.getText() + " putangina");
        timeline.play();


    }
}
