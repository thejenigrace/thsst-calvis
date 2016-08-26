package thsst.calvis.simulatorvisualizer.animation.instruction.gp;

import thsst.calvis.configuration.model.engine.Calculator;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.configuration.model.exceptions.MemoryReadException;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;
import thsst.calvis.simulatorvisualizer.model.TimeLineFunction;

/**
 * Created by Goodwin Chua on 9 Jul 2016.
 */
public class Cmpxchgs8 extends CalvisAnimation {

    @Override
    public void animate(ScrollPane tab) {
        this.root.getChildren().clear();
        tab.setContent(root);
        
        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();
        TimeLineFunction timeFunc = new TimeLineFunction(timeline, root, registers, memory, finder);
        Calculator c = new Calculator(registers, memory);
        Token[] tokens = currentInstruction.getParameterTokens();
        Token src = tokens[0];
        int operandSize = memory.getBitSize(src);
        ObservableList<Node> parent = this.root.getChildren();

        // ANIMATION ASSETS
        Rectangle edxRectangle = timeFunc.createRectangle(110, 80, Color.web("#fe6f5e"));
        Rectangle eaxRectangle = timeFunc.createRectangle(110, 80, Color.web("#318ce7"));
        Rectangle memRectangle = timeFunc.createRectangle(220, 80, Color.web("#00ffff"));
        Rectangle fake = timeFunc.createRectangle(0, 0, Color.WHITE);
        parent.addAll(edxRectangle, eaxRectangle, fake);

        Text edxLabel = timeFunc.generateText(new Text("EDX"), 12, "#3d2b1f");
        Text eaxLabel = timeFunc.generateText(new Text("EAX"), 12, "#3d2b1f");
        Text compare = timeFunc.generateText(new Text("=="), 20, "#3d2b1f");
        Text arrow = timeFunc.generateText(new Text("=>"), 20, "#3d2b1f");
        Text zeroFlag = timeFunc.generateText(new Text("Zero Flag Value Result: " + registers.getEFlags().getZeroFlag()), 20, "#3d2b1f");

        compare.setFont(Font.font("Elephant", FontWeight.EXTRA_BOLD, 20));
        Text srcLabel = timeFunc.generateText(this.createLabelText(src), 12, "#3d2b1f");
        arrow.setFont(Font.font("Elephant", FontWeight.EXTRA_BOLD, 20));
        parent.addAll(compare, edxLabel, eaxLabel, memRectangle, srcLabel, arrow);

        //Values
        Text edxValue = timeFunc.generateText(new Text(finder.getRegister("EDX")), 12, "#3d2b1f");
        Text eaxValue = timeFunc.generateText(new Text(finder.getRegister("EAX")), 12, "#3d2b1f");
        Text srcValue = new Text();

        try{
            srcValue = timeFunc.generateText(new Text(finder.read(src, operandSize)), 12, "#3d2b1f");
        } catch(MemoryReadException e){}



        parent.addAll(edxValue, eaxValue, srcValue, zeroFlag);

        //Timeline
        timeFunc.setTimelinePosition(0, 0, fake);
        timeFunc.setTimelinePosition(250, 65, compare);
        timeFunc.setTimelinePosition(20, 20, edxRectangle);
        timeFunc.setTimelinePosition(20 + eaxRectangle.getWidth(), 20, eaxRectangle);
        timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2, 40, edxLabel);
        timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                (edxRectangle.getX() + edxRectangle.getWidth()), 40, eaxLabel);
        timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                (edxRectangle.getX() + edxRectangle.getWidth() + 130), 20, memRectangle);
        timeFunc.setTimelinePosition(350, 40, srcLabel);
        timeFunc.setTimelinePosition(350, 60, srcValue);
        timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2, 60, edxValue);
        timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                (edxRectangle.getX() + edxRectangle.getWidth()), 60, eaxValue);
        timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                (edxRectangle.getX() + edxRectangle.getWidth() + 365), 65, arrow);

        timeFunc.setTimelinePosition(20, 150, zeroFlag);

        if((edxValue.getText() + eaxValue.getText()).equals(srcValue.getText())){
            compare.setText("==");
            Text resultLabel = timeFunc.generateText(new Text(srcLabel.getText()), 12, "#3d2b1f");
            Text ecxValue = timeFunc.generateText(new Text(finder.getRegister("ECX")), 12, "#3d2b1f");
            Text ebxValue = timeFunc.generateText(new Text(finder.getRegister("EBX")), 12, "#3d2b1f");
            Text ecxLabel = timeFunc.generateText(new Text("ECX"), 12, "#3d2b1f");
            Text ebxLabel = timeFunc.generateText(new Text("EBX"), 12, "#3d2b1f");

            Rectangle resultRectangle = timeFunc.createRectangle(220, 80, Color.web("#00ffff"));
            parent.addAll(resultRectangle, resultLabel, ecxValue, ebxValue, ecxLabel, ebxLabel);
            timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                    (edxRectangle.getX() + edxRectangle.getWidth() + 400), 20, resultRectangle);
            timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                    (edxRectangle.getX() + edxRectangle.getWidth() + 450), 40, resultLabel);
            timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                    (edxRectangle.getX() + edxRectangle.getWidth() + 450), 80, ecxValue);
            timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                    (edxRectangle.getX() + edxRectangle.getWidth() + 450 + 54), 80, ebxValue);

            timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                    (edxRectangle.getX() + edxRectangle.getWidth() + 450), 60, ecxLabel);

            timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                    (edxRectangle.getX() + edxRectangle.getWidth() + 450 + 54), 60, ebxLabel);
        }
        else{
            compare.setText("!=");
            Text edxResultVal = timeFunc.generateText(new Text(srcValue.getText().substring(0, 8)), 12, "#3d2b1f");
            Text eaxResultVal = timeFunc.generateText(new Text(srcValue.getText().substring(8, 16)), 12, "#3d2b1f");
            Text edxResultLabel = timeFunc.generateText(new Text("EDX"), 12, "#3d2b1f");
            Text eaxResultLabel = timeFunc.generateText(new Text("EAX"), 12, "#3d2b1f");
            Rectangle edxRectangleRes = timeFunc.createRectangle(110, 80, Color.web("#fe6f5e"));
            Rectangle eaxRectangleRes = timeFunc.createRectangle(110, 80, Color.web("#318ce7"));
            parent.add(edxRectangleRes);
            parent.add(eaxRectangleRes);
            parent.add(eaxResultLabel);
            parent.add(edxResultLabel);
            parent.add(eaxResultVal);
            parent.add(edxResultVal);

            timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                    (edxRectangle.getX() + edxRectangle.getWidth() + 400), 20, edxRectangleRes);

            timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                    (edxRectangle.getX() + edxRectangle.getWidth() + 400 + edxRectangle.getWidth()), 20, eaxRectangleRes);

            timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                    (edxRectangle.getX() + edxRectangle.getWidth() + 430), 60, edxResultVal);

            timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                    (edxRectangle.getX() + edxRectangle.getWidth() + 430), 40, edxResultLabel);

            timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                    (edxRectangle.getX() + edxRectangle.getWidth() + 425 + 120), 60, eaxResultVal);

            timeFunc.setTimelinePosition(edxRectangle.getX() + edxRectangle.getWidth() / 2 +
                    (edxRectangle.getX() + edxRectangle.getWidth() + 425 + 120), 40, eaxResultLabel);
        }

        timeline.play();
    }
}
