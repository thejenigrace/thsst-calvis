package thsst.calvis.simulatorvisualizer.model.instructionanimation;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import thsst.calvis.configuration.model.engine.Calculator;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.configuration.model.exceptions.MemoryReadException;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;
import thsst.calvis.simulatorvisualizer.model.RotateModel;
import thsst.calvis.simulatorvisualizer.model.TimeLineFunction;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Marielle Ong on 8 Jul 2016.
 */
public class Sal extends CalvisAnimation {

    @Override
    public void animate(ScrollPane tab) {
        this.root.getChildren().clear();
        tab.setContent(root);
        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();
        TimeLineFunction timeFunc = new TimeLineFunction(timeline, root, registers, memory);
        Calculator c = new Calculator(registers, memory);
        Token[] tokens = currentInstruction.getParameterTokens();
        String des = "";
        if ( tokens[0].getType() == Token.REG ) {
            des = finder.getRegister(tokens[0].getValue());
        }
        else if ( tokens[0].getType() == Token.MEM ) {
            try {
                des = finder.read(tokens[0], registers.getBitSize(tokens[0]));
            } catch (MemoryReadException e) {
                e.printStackTrace();
            }
        }
        Token count = tokens[1];
        int counter = timeFunc.parseHex(count);
        int operandSize = timeFunc.getBitSize(tokens[0]);
        ObservableList<Node> parent = this.root.getChildren();

        ArrayList<RotateModel> rotateModels = new ArrayList<>();
        BigInteger biDes = new BigInteger(des, 16);
        BigInteger biResult = biDes;

        String carryFlagValue = "";
        for (int x = 1; x <= counter; x++) {
            biResult = biResult.shiftLeft(1);
            biResult = biResult.clearBit(0);

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
            rotateResults.add(timeFunc.generateText(new Text(rotateModels.get(x).getResult()), 20, "#5d8aa8", FontWeight.EXTRA_BOLD, "Elephant"));
            numberLabel.add(timeFunc.generateText(new Text(Integer.toString(x + 1)), 20, "#a4c639", FontWeight.EXTRA_BOLD, "Elephant"));
            zeroFlags.add(timeFunc.generateText(new Text(rotateModels.get(x).getFlag()), 20, "#5d8aa8", FontWeight.EXTRA_BOLD, "Elephant"));
            resultLabel.add(timeFunc.generateText(new Text(tokens[0].getValue()), 15, "#3d2b1f", FontWeight.EXTRA_BOLD, "Elephant"));
            cfLabel.add(timeFunc.generateText(new Text(""), 15, "#3d2b1f", FontWeight.EXTRA_BOLD, "Elephant"));
            parent.addAll(rotateResults.get(x), zeroFlags.get(x), cfLabel.get(x), resultLabel.get(x), numberLabel.get(x));
        }

        for(int x = 0; x < counter; x++){
            timeFunc.setTimelinePosition(60 + rotateResults.get(x).getLayoutBounds().getWidth(), 20 + 50 * x, zeroFlags.get(x));
            timeFunc.setTimelinePosition(20 + 30, 20 + 50 * x, rotateResults.get(x));
            timeFunc.setTimelinePosition(60 + rotateResults.get(x).getLayoutBounds().getWidth() , 40 + 50 * x, cfLabel.get(x));
            timeFunc.setTimelinePosition(20 + 30 , 40 + 50 * x, resultLabel.get(x));
            timeFunc.setTimelinePosition(10 , 20 + 50 * x, numberLabel.get(x));
        }
        parent.add(resultText);
        timeFunc.setTimelinePosition(20 + 60 + resultLabel.get(0).getLayoutBounds().getWidth(), 40 + 50 * (counter  - 1), resultText);
        timeline.play();
    }
}