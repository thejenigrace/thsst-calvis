package thsst.calvis.simulatorvisualizer.model.instructionanimation;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import thsst.calvis.configuration.model.engine.Calculator;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.configuration.model.exceptions.MemoryReadException;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;
import thsst.calvis.simulatorvisualizer.model.RotateModel;
import thsst.calvis.simulatorvisualizer.model.TimeLineFunction;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Marielle Ong on 8 Jul 2016.
 */
public class Psllw extends CalvisAnimation {

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

        String w4 = des.substring(0, 4);
        String w3 = des.substring(4, 8);
        String w2 = des.substring(8, 12);
        String w1 = des.substring(12);

        BigInteger count = new BigInteger("0", 16);

        if(tokens[1].isRegister()){
            count = new BigInteger(registers.get(tokens[1]), 16);
        }
        else if(tokens[1].isHex()){
            count = new BigInteger(tokens[1].getValue(), 16);
        }


        int counter = count.intValue();
        int operandSize = 16;
        ObservableList<Node> parent = this.root.getChildren();

        ArrayList<RotateModel> rotateModels1 = new ArrayList<>();
        ArrayList<RotateModel> rotateModels2 = new ArrayList<>();
        ArrayList<RotateModel> rotateModels3 = new ArrayList<>();
        ArrayList<RotateModel> rotateModels4 = new ArrayList<>();
        BigInteger biW4 = new BigInteger(w4, 16);
        BigInteger biW3 = new BigInteger(w3, 16);
        BigInteger biW2 = new BigInteger(w2, 16);
        BigInteger biW1 = new BigInteger(w1, 16);

//        BigInteger biDes = new BigInteger(des, 16);
//        BigInteger biResult = biDes;

        String carryFlagValue = "";
        for (int x = 1; x <= counter; x++) {
            biW4 = biW4.shiftLeft(1);
            biW4 = biW4.clearBit(0);

            if (operandSize < c.binaryZeroExtend(biW4.toString(2), operandSize).length()) {
                int cut = c.binaryZeroExtend(biW4.toString(2), operandSize).length() - operandSize;
                String t = c.binaryZeroExtend(biW4.toString(2), operandSize).substring(cut);
                rotateModels4.add(new RotateModel(t, carryFlagValue));
            }
            else
                rotateModels4.add(new RotateModel(c.binaryZeroExtend(biW4.toString(2), operandSize), carryFlagValue));
        }

        for (int x = 1; x <= counter; x++) {
            biW3 = biW3.shiftLeft(1);
            biW3 = biW3.clearBit(0);

            if (operandSize < c.binaryZeroExtend(biW3.toString(2), operandSize).length()) {
                int cut = c.binaryZeroExtend(biW3.toString(2), operandSize).length() - operandSize;
                String t = c.binaryZeroExtend(biW3.toString(2), operandSize).substring(cut);
                rotateModels3.add(new RotateModel(t, carryFlagValue));
            }
            else
                rotateModels3.add(new RotateModel(c.binaryZeroExtend(biW3.toString(2), operandSize), carryFlagValue));
        }

        for (int x = 1; x <= counter; x++) {
            biW2 = biW2.shiftLeft(1);
            biW2 = biW2.clearBit(0);

            if (operandSize < c.binaryZeroExtend(biW2.toString(2), operandSize).length()) {
                int cut = c.binaryZeroExtend(biW2.toString(2), operandSize).length() - operandSize;
                String t = c.binaryZeroExtend(biW2.toString(2), operandSize).substring(cut);
                rotateModels2.add(new RotateModel(t, carryFlagValue));
            }
            else
                rotateModels2.add(new RotateModel(c.binaryZeroExtend(biW2.toString(2), operandSize), carryFlagValue));
        }

        for (int x = 1; x <= counter; x++) {
            biW1 = biW1.shiftLeft(1);
            biW1 = biW1.clearBit(0);

            if (operandSize < c.binaryZeroExtend(biW1.toString(2), operandSize).length()) {
                int cut = c.binaryZeroExtend(biW1.toString(2), operandSize).length() - operandSize;
                String t = c.binaryZeroExtend(biW1.toString(2), operandSize).substring(cut);
                rotateModels1.add(new RotateModel(t, carryFlagValue));
            }
            else
                rotateModels1.add(new RotateModel(c.binaryZeroExtend(biW1.toString(2), operandSize), carryFlagValue));
        }

        ArrayList<Text> rotateResults = new ArrayList<>();
//        ArrayList<Text> rotateResults3 = new ArrayList<>();
//        ArrayList<Text> rotateResults2 = new ArrayList<>();
//        ArrayList<Text> rotateResults1 = new ArrayList<>();
        ArrayList<Text> zeroFlags = new ArrayList<>();
        ArrayList<Text> resultLabel = new ArrayList<>();
        ArrayList<Text> cfLabel = new ArrayList<>();
        ArrayList<Text> numberLabel = new ArrayList<>();
        Text resultText = timeFunc.generateText(new Text("Final Result"), 13, "#3d2b1f", FontWeight.NORMAL, "Elephant");
        Rectangle fake = timeFunc.createRectangle(0, 0, Color.WHITE);
        parent.add(fake);

        for(int x = 0; x < counter; x++){
            rotateResults.add(timeFunc.generateText(new Text(rotateModels4.get(x).getResult()), 13, "#5d8aa8", FontWeight.NORMAL, "Elephant"));
            numberLabel.add(timeFunc.generateText(new Text(Integer.toString(x + 1)), 13, "#a4c639", FontWeight.NORMAL, "Elephant"));
            zeroFlags.add(timeFunc.generateText(new Text(rotateModels4.get(x).getFlag()), 13, "#5d8aa8", FontWeight.NORMAL, "Elephant"));
            resultLabel.add(timeFunc.generateText(new Text(tokens[0].getValue() ), 13, "#3d2b1f", FontWeight.NORMAL, "Elephant"));
            cfLabel.add(timeFunc.generateText(new Text("word 4"), 13, "#3d2b1f", FontWeight.NORMAL, "Elephant"));
            parent.addAll(rotateResults.get(x), zeroFlags.get(x), cfLabel.get(x), resultLabel.get(x), numberLabel.get(x));
        }

        for(int x = 0; x < counter; x++){
            rotateResults.add(timeFunc.generateText(new Text(rotateModels3.get(x).getResult()), 13, "#5d8aa8", FontWeight.NORMAL, "Elephant"));
            numberLabel.add(timeFunc.generateText(new Text(Integer.toString(x + 1)), 13, "#a4c639", FontWeight.NORMAL, "Elephant"));
            zeroFlags.add(timeFunc.generateText(new Text(rotateModels3.get(x).getFlag()), 13, "#5d8aa8", FontWeight.NORMAL, "Elephant"));
            resultLabel.add(timeFunc.generateText(new Text(tokens[0].getValue() + "(word 3)"), 13, "#3d2b1f", FontWeight.NORMAL, "Elephant"));
            cfLabel.add(timeFunc.generateText(new Text("word 3"), 13, "#3d2b1f", FontWeight.NORMAL, "Elephant"));
            parent.addAll(rotateResults.get(x), zeroFlags.get(x), cfLabel.get(x), resultLabel.get(x), numberLabel.get(x));
        }

        for(int x = 0; x < counter; x++){
            rotateResults.add(timeFunc.generateText(new Text(rotateModels2.get(x).getResult()), 13, "#5d8aa8", FontWeight.NORMAL, "Elephant"));
            numberLabel.add(timeFunc.generateText(new Text(Integer.toString(x + 1)), 13, "#a4c639", FontWeight.NORMAL, "Elephant"));
            zeroFlags.add(timeFunc.generateText(new Text(rotateModels2.get(x).getFlag()), 13, "#5d8aa8", FontWeight.NORMAL, "Elephant"));
            resultLabel.add(timeFunc.generateText(new Text(tokens[0].getValue() + "(word 2)"), 13, "#3d2b1f", FontWeight.NORMAL, "Elephant"));
            cfLabel.add(timeFunc.generateText(new Text("word 2"), 13, "#3d2b1f", FontWeight.NORMAL, "Elephant"));
            parent.addAll(rotateResults.get(x), zeroFlags.get(x), cfLabel.get(x), resultLabel.get(x), numberLabel.get(x));
        }

        for(int x = 0; x < counter; x++){
            rotateResults.add(timeFunc.generateText(new Text(rotateModels2.get(x).getResult()), 13, "#5d8aa8", FontWeight.NORMAL, "Elephant"));
            numberLabel.add(timeFunc.generateText(new Text(Integer.toString(x + 1)), 13, "#a4c639", FontWeight.NORMAL, "Elephant"));
            zeroFlags.add(timeFunc.generateText(new Text(rotateModels2.get(x).getFlag()), 13, "#5d8aa8", FontWeight.NORMAL, "Elephant"));
            resultLabel.add(timeFunc.generateText(new Text(tokens[0].getValue() + "(word 1)"), 13, "#3d2b1f", FontWeight.NORMAL, "Elephant"));
            cfLabel.add(timeFunc.generateText(new Text("word 1"), 13, "#3d2b1f", FontWeight.NORMAL, "Elephant"));
            parent.addAll(rotateResults.get(x), zeroFlags.get(x), cfLabel.get(x), resultLabel.get(x), numberLabel.get(x));
        }

        for(int x = 0; x < counter * 4; x++){
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