package thsst.calvis.simulatorvisualizer.animation.instruction.x87;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
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
public class Fsave extends CalvisAnimation {

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
                "\nC0, C1, C2, C3 are saved then cleared .";
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
        Text sign = timeFunc.generateText(new Text("Transfer To ->"), 18, "000000");
        Text equal = timeFunc.generateText(new Text("="), 18, "000000");
        ArrayList<Text> notes = new ArrayList<>();

        switch(tokens.length){
            case 0:
                bitSize = registers.getBitSize("ST0");
                int top = (registers.x87().status().getTop() + 1) % 8;
                desStr = "FPU TAG REGISTER: " + registers.x87().tag().getValue() + "\n"
                          + "FPU CONTROL REGISTER: " + registers.x87().control().getValue() + "\n"
                          + "FPU STATUS REGISTER: " + registers.x87().status().getValue() + "\n"
                          + "FPU STACK POINTER: " + "ST" + registers.x87().status().getTop();
                desLabel = this.createLabelText("");
//                srcLabel = this.createLabelText("RESULTING CLEAR:");
//                resLabel = this.createLabelText("ST0");
                desRec = this.createRectangle("ST0", 350, 400);
//                srcRec = this.createRectangle("ST0", 180, 80);
//                resRec = this.createRectangle("ST0", 200, 60);
//                resValue = new Text(registers.get("ST0"));

                break;
            case 1:

                src = tokens[0];
                bitSize = timeFunc.getBitSize(src);
                sourceStr = timeFunc.getPreviousValue(src, bitSize);
                sourceStr = timeFunc.getValue(src, bitSize) + "";
                desStr = "D[FPUControlWord] ← FPUControlWord\n" +
                        "D[FPUStatusWord] ← FPUStatusWord\n" +
                        "D[FPUTagWord] ← FPUTagWord\n" +
                        "D[FPUDataPointer] ← FPUDataPointer\n" +
                        "D[FPUInstructionPointer] ← FPUInstructionPointer\n" +
                        "D[FPULastInstructionOpcode] ← FPULastInstructionOpcode\n" +
                        "D[ST(0)] ← ST(0)\n" +
                        "D[ST(1)] ← ST(1)\n" +
                        "D[ST(2)] ← ST(2)\n" +
                        "D[ST(3)] ← ST(3)\n" +
                        "D[ST(4)]← ST(4)\n" +
                        "D[ST(5)] ← ST(5)\n" +
                        "D[ST(6)] ← ST(6)\n" +
                        "D[ST(7)] ← ST(7)\n\n" +
                        "SET THE FF:\n" +
                        "FPUControlWord ← 037FH\n" +
                        "FPUStatusWord ← 0\n" +
                        "FPUTagWord ← FFFFH\n" +
                        "FPUDataPointer ← 0\n" +
                        "FPUInstructionPointer ← 0\n" +
                        "FPULastInstructionOpcode ← 0";
                desLabel = this.createLabelText("Set " + src.getValue() + ":");
//                resLabel = this.createLabelText("ST0");
//                srcLabel = this.createLabelText(src);
                desRec = new Rectangle(380, 400, Color.web("#79CFCE", 1.0));
//                srcRec = this.createRectangle(src, 200, 60);
//                resRec = this.createRectangle("ST0", 200, 60);
//                resValue = new Text(registers.get("ST0"));
                break;
            case 2:
//                des = tokens[0];
//                src = tokens[1];
//                bitSize = timeFunc.getBitSize(src);
//                sourceStr = timeFunc.getPreviousValue(src, bitSize);
//                desStr = timeFunc.getPreviousValue(des, bitSize);
//                desLabel = this.createLabelText(des);
//                resLabel = this.createLabelText(des);
//                srcLabel = this.createLabelText(src);
//                desRec = this.createRectangle(des, 160, 60);
//                srcRec = this.createRectangle(src, 160, 60);
//                resRec = this.createRectangle(des, 160, 60);
//                resValue = new Text(registers.get(des));
                break;
        }
        srcValue = new Text(sourceStr);
        desValue = new Text(desStr);
//        desValue.setWrappingWidth(desRec.getLayoutBounds().getWidth() - 40);
        System.out.println(desValue.getText() + " :(");
        Rectangle fake = new Rectangle(0,0);
        parent.addAll(fake, desRec, desValue, desLabel, poppedValue);
        fake.setX(0);
        fake.setY(0);

        double posX = 30;
        double posY = 20;
        double space = 160;

        desRec.setX(posX);
        desRec.setY(posY);

        srcRec.setX(posX + space + desRec.getWidth());
        srcRec.setY(posY);

        resRec.setX(posX + space + desRec.getWidth() + srcRec.getWidth() + space);
        resRec.setY(posY);

        desLabel.setX(posX + (desRec.getWidth() / 2 - desLabel.getLayoutBounds().getWidth() / 2) );
        desLabel.setY(posY + 20);

        desValue.setX(posX + 40);
        desValue.setY(posY + 45);

        srcLabel.setX(posX + (srcRec.getWidth() / 2 - srcLabel.getLayoutBounds().getWidth() / 2) + desRec.getWidth() + space);
        srcLabel.setY(posY + 20);

        srcValue.setX(posX + 40);
        srcValue.setY(posY + 45);

        resLabel.setX(posX + (resRec.getWidth() / 2 - resLabel.getLayoutBounds().getWidth() / 2) + (desRec.getWidth() + space) * 2);
        resLabel.setY(posY + 20);

        resValue.setX(posX + (resRec.getWidth() / 2 - resValue.getLayoutBounds().getWidth() / 2) + (desRec.getWidth() + space) * 2);
        resValue.setY(posY + 45);

        sign.setX(posX + 20 + desRec.getWidth());
        sign.setY(posY + 45);

        equal.setX(posX + space / 4 + (desRec.getWidth()) * 2 + space);
        equal.setY(posY + 45);

        poppedValue.setX(posX + 5);
        poppedValue.setY(posY + desRec.getHeight() + 75);

    }
}

