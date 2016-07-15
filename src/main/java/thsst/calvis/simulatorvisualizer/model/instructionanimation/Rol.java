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
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;
import thsst.calvis.simulatorvisualizer.model.RotateModel;
import thsst.calvis.simulatorvisualizer.model.TimeLineFunction;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 9 Jul 2016.
 */
public class Rol extends CalvisAnimation {

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
        Token count = tokens[1];
        int counter = timeFunc.parseHex(count);
        int operandSize = timeFunc.getBitSize(des);
        ObservableList<Node> parent = this.root.getChildren();

        ArrayList<RotateModel> rotateModels = new ArrayList<>();
        String destination = timeFunc.getValue(des, operandSize);
        BigInteger biDes = new BigInteger(destination, 16);
        BigInteger biResult = biDes;
        boolean bitSet = false;

        String carryFlagValue = "";
        for (int x = 0; x < counter; x++) {
            bitSet = biResult.testBit(operandSize - 1);
            biResult = biResult.shiftLeft(1);
            if (bitSet) {
                biResult = biResult.setBit(0);
                carryFlagValue = "1";
            } else {
                biResult = biResult.clearBit(0);
                carryFlagValue = "0";
            }
            if (operandSize < c.binaryZeroExtend(biResult.toString(2), operandSize).length()) {
                int cut = c.binaryZeroExtend(biResult.toString(2), operandSize).length() - operandSize;
                String t = c.binaryZeroExtend(biResult.toString(2), operandSize).substring(cut);
                rotateModels.add(new RotateModel(t, carryFlagValue));
            }
            else
                rotateModels.add(new RotateModel(c.binaryZeroExtend(biResult.toString(2), operandSize), carryFlagValue));
        }

        ArrayList<Text> rotateResults = new ArrayList<>();
        ArrayList<Text> zeroFlags = new ArrayList<>();
        ArrayList<Text> resultLabel = new ArrayList<>();
        ArrayList<Text> cfLabel = new ArrayList<>();
        ArrayList<Text> numberLabel = new ArrayList<>();
        Text resultText = timeFunc.generateText(new Text("Final Result"), 20, "#3d2b1f", FontWeight.EXTRA_BOLD, "Elephant");
        Rectangle fake = timeFunc.createRectangle(0, 0, Color.WHITE);
        parent.add(fake);
        for(int x = 0; x < counter; x++){
            numberLabel.add(timeFunc.generateText(new Text(Integer.toString(x + 1)), 20, "#a4c639", FontWeight.EXTRA_BOLD, "Elephant"));
            rotateResults.add(timeFunc.generateText(new Text(rotateModels.get(x).getResult()), 20, "#5d8aa8", FontWeight.EXTRA_BOLD, "Elephant"));
            zeroFlags.add(timeFunc.generateText(new Text(rotateModels.get(x).getFlag()), 20, "#5d8aa8", FontWeight.EXTRA_BOLD, "Elephant"));
            resultLabel.add(timeFunc.generateText(new Text(des.getValue()), 15, "#3d2b1f", FontWeight.EXTRA_BOLD, "Elephant"));
            cfLabel.add(timeFunc.generateText(new Text("CF"), 15, "#3d2b1f", FontWeight.EXTRA_BOLD, "Elephant"));
            parent.addAll(rotateResults.get(x), zeroFlags.get(x), cfLabel.get(x), resultLabel.get(x), numberLabel.get(x));
        }

        for(int x = 0; x < counter; x++){
            timeFunc.setTimelinePosition(40 + numberLabel.get(x).getLayoutBounds().getWidth() + 30, 20 + 50 * x, zeroFlags.get(x));
            timeFunc.setTimelinePosition(40 + numberLabel.get(x).getLayoutBounds().getWidth() + 30
                    + zeroFlags.get(x).getLayoutBounds().getWidth() + 30, 20 + 50 * x, rotateResults.get(x));
            timeFunc.setTimelinePosition(40 + numberLabel.get(x).getLayoutBounds().getWidth() + 30 , 40 + 50 * x, cfLabel.get(x));
            timeFunc.setTimelinePosition(40 + numberLabel.get(x).getLayoutBounds().getWidth() + 30
                    + zeroFlags.get(x).getLayoutBounds().getWidth() + 30 , 40 + 50 * x, resultLabel.get(x));
            timeFunc.setTimelinePosition(10 , 20 + 50 * x, numberLabel.get(x));
        }
        parent.add(resultText);
        timeFunc.setTimelinePosition(20 + 60 + zeroFlags.get(0).getLayoutBounds().getWidth() + 80 + resultLabel.get(0).getLayoutBounds().getWidth(), 40 + 50 * (counter  - 1), resultText);


        timeline.play();
    }
}
