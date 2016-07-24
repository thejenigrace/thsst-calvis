package thsst.calvis.simulatorvisualizer.animation.instruction.x87;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import thsst.calvis.configuration.model.engine.Calculator;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;
import thsst.calvis.simulatorvisualizer.model.TimeLineFunction;

import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Fdivrp extends CalvisAnimation {

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
        scrollPane.setContent(root);
        RegisterList registers = currentInstruction.getRegisters();
        ObservableList<Node> parent = this.root.getChildren();
        Memory memory = currentInstruction.getMemory();
        TimeLineFunction timeFunc = new TimeLineFunction(timeline, root, registers, memory, finder);
        Calculator c = new Calculator(registers, memory);
        Token[] tokens = currentInstruction.getParameterTokens();
        String notesStr = "Additional Notes: \n" +
                "\nC0, C2, C3 are undefined." +
                "\nC1 is set to 0 if stack underflow." +
                "\nC1 is set to 1 if result was rounded up; Else set to 0.";
        Text poppedValue = timeFunc.generateText(new Text(notesStr), 15, "000000");
        Token des;
        Token src;
        String sourceStr = "";
        String desStr = "";
        int bitSize = 0;
        Text srcLabel = new Text();
        Text desLabel = new Text();
        Text resLabel = new Text();
        Text srcValue = new Text();
        Text desValue = new Text();
        Text resValue = new Text();
        Rectangle desRec = new Rectangle();
        Rectangle srcRec = new Rectangle();
        Rectangle resRec = new Rectangle();
        Text sign = timeFunc.generateText(new Text("/"), 30, "000000");
        Text equal = timeFunc.generateText(new Text("="), 30, "000000");
        ArrayList<Text> notes = new ArrayList<>();
        switch(tokens.length){
            case 0:
                bitSize = registers.getBitSize("ST0");
                sourceStr = finder.getRegister("ST1");
                desStr = finder.getRegister("ST0");
                desLabel = this.createLabelText("ST0");
                srcLabel = this.createLabelText("ST1");
                resLabel = this.createLabelText("ST0");
                desRec = this.createRectangle("ST0", 200, 60);
                srcRec = this.createRectangle("ST1", 200, 60);
                resRec = this.createRectangle("ST0", 200, 60);
                resValue = new Text(registers.get("ST0"));
                break;
            case 1:

                src = tokens[0];
                bitSize = timeFunc.getBitSize(src);
                sourceStr = timeFunc.getPreviousValue(src, bitSize);
                sourceStr = (Long.parseLong(sourceStr, 16) + 0.0 )+ "";
                desStr = finder.getRegister("ST0");
                desLabel = this.createLabelText("ST0");
                resLabel = this.createLabelText("ST0");
                srcLabel = this.createLabelText(src);
                desRec = this.createRectangle("ST0", 200, 60);
                srcRec = this.createRectangle(src, 200, 60);
                resRec = this.createRectangle("ST0", 200, 60);
                resValue = new Text(registers.get("ST0"));
                break;
            case 2:
                des = tokens[0];
                src = tokens[1];
                bitSize = timeFunc.getBitSize(src);
                sourceStr = timeFunc.getPreviousValue(src, bitSize);
                desStr = timeFunc.getPreviousValue(des, bitSize);
                desLabel = this.createLabelText(des);
                resLabel = this.createLabelText(des);
                srcLabel = this.createLabelText(src);
                desRec = this.createRectangle(des, 160, 60);
                srcRec = this.createRectangle(src, 160, 60);
                resRec = this.createRectangle(des, 160, 60);
                resValue = new Text(registers.get(des));
                break;
        }
        srcValue = new Text(sourceStr);
        desValue = new Text(desStr);
        System.out.println(desValue.getText() + " :(");
        Rectangle fake = new Rectangle(0,0);
        parent.addAll(fake, desRec, resRec, srcRec, srcValue, desValue, resValue, desLabel, resLabel, srcLabel, sign, equal);
        fake.setX(0);
        fake.setY(0);

        double posX = 30;
        double posY = 20;
        double space = 60;

        desRec.setX(posX);
        desRec.setY(posY);

        srcRec.setX(posX + space + desRec.getWidth());
        srcRec.setY(posY);

        resRec.setX(posX + space + desRec.getWidth() + srcRec.getWidth() + space);
        resRec.setY(posY);

        desLabel.setX(posX + (desRec.getWidth() / 2 - desLabel.getLayoutBounds().getWidth() / 2) );
        desLabel.setY(posY + 20);

        desValue.setX(posX + (desRec.getWidth() / 2 - desValue.getLayoutBounds().getWidth() / 2));
        desValue.setY(posY + 45);

        srcLabel.setX(posX + (srcRec.getWidth() / 2 - srcLabel.getLayoutBounds().getWidth() / 2) + desRec.getWidth() + space);
        srcLabel.setY(posY + 20);

        srcValue.setX(posX + (srcRec.getWidth() / 2 - srcValue.getLayoutBounds().getWidth() / 2) + desRec.getWidth() + space);
        srcValue.setY(posY + 45);

        resLabel.setX(posX + (resRec.getWidth() / 2 - resLabel.getLayoutBounds().getWidth() / 2) + (desRec.getWidth() + space) * 2);
        resLabel.setY(posY + 20);

        resValue.setX(posX + (resRec.getWidth() / 2 - resValue.getLayoutBounds().getWidth() / 2) + (desRec.getWidth() + space) * 2);
        resValue.setY(posY + 45);

        sign.setX(posX + space / 4 + desRec.getWidth());
        sign.setY(posY + 45);

        equal.setX(posX + space / 4 + (desRec.getWidth()) * 2 + space);
        equal.setY(posY + 45);

        poppedValue.setX(posX + 5);
        poppedValue.setY(posY + desRec.getHeight() + 30);

    }
}

