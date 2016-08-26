package thsst.calvis.simulatorvisualizer.animation.instruction.sse2;

import javafx.collections.ObservableList;
import javafx.scene.Group;
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

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 9 Jul 2016.
 */
public class Punpcklqdq extends CalvisAnimation {

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
        Token src = tokens[1];
        int size = timeFunc.getBitSize(des);
        ObservableList<Node> parent = this.root.getChildren();

        String result = timeFunc.getValue(des, size);
        String source = timeFunc.getPreviousValue(src, size);
        String destination = timeFunc.getPreviousValue(des, timeFunc.getBitSize(des));
        BigInteger biDes = new BigInteger(destination, 16);
        BigInteger biResult = biDes;
        boolean bitSet = false;

//        Line line= timeFunc.drawLine(0,0, 200,200);


        Rectangle desRec = this.createRectangle(des, 400, 80);
        Rectangle srcRec = this.createRectangle(src, 400, 80);
        Rectangle resRec = this.createRectangle(des, 400, 80);
        Rectangle fake = new Rectangle(0,0);

        parent.add(fake);
        parent.addAll(srcRec, desRec, resRec);

        ArrayList<String> srcStrings = new ArrayList<>();
        ArrayList<String> desStrings = new ArrayList<>();
        ArrayList<String> resStrings = new ArrayList<>();

        for(int x = 0; x < (size / 4) / 4; x++){
            srcStrings.add(source.substring(x * 4, x * 4 + 4));
            desStrings.add(destination.substring(x * 4, x * 4 + 4));
            resStrings.add(result.substring(x * 4, x * 4 + 4));
        }

        double posX = 120;
        double posY = 20;
        double posLabelY = 25;
        double posValueY = 10;
        double distanceWord = 50;
        srcRec.setX(posX);
        srcRec.setY(posY);

        desRec.setX(posX + desRec.getLayoutBounds().getWidth() + 150);
        desRec.setY(posY);

        resRec.setX(desRec.getX() / 2 - srcRec.getX() / 2 + posX);
        resRec.setY(posY + 140);

        Text srcLabel = new Text(src.getValue() + "(SOURCE)");
        srcLabel.setX(posX + srcRec.getWidth() / 2 - srcLabel.getLayoutBounds().getWidth() / 2);
        srcLabel.setY(posY + posLabelY);

        Text desLabel = new Text(des.getValue() + "(DESTINATION)");
        desLabel.setX(posX + srcRec.getWidth() + 150 + ( desRec.getLayoutBounds().getWidth() / 2 - desLabel.getLayoutBounds().getWidth() / 2));
        desLabel.setY(posY + posLabelY);

        Text resLabel = new Text(des.getValue() + "(RESULT)");
        resLabel.setX(posX + desRec.getX() / 2 - srcRec.getX() / 2 + ( resRec.getLayoutBounds().getWidth() / 2 - resLabel.getLayoutBounds().getWidth() / 2));
        resLabel.setY(posY + 140 + posLabelY);

        ArrayList<Text> txtSrc = new ArrayList<>();
        ArrayList<Text> txtDes = new ArrayList<>();
        ArrayList<Text> txtRes = new ArrayList<>();

        for(int x = 0; x < 8; x++){
            txtSrc.add(new Text(srcStrings.get(x)));
            txtDes.add(new Text(desStrings.get(x)));
            txtRes.add(new Text(resStrings.get(x)));
            if(x > 3){
                txtSrc.get(x).setFill(Color.web("#4B5320"));
                txtDes.get(x).setFill(Color.web("#CC0000"));
            }
            if(x % 2 != 0){
                txtRes.get(x).setFill(Color.web("#4B5320"));
            }
            else{
                txtRes.get(x).setFill(Color.web("#CC0000"));
            }

        }

        parent.addAll(srcLabel, desLabel, resLabel);
        timeline.play();

        Group srcGroup = new Group();
        Group desGroup = new Group();
        Group resGroup = new Group();
        for(int x = 0; x < 8; x++){
            txtSrc.get(x).setX(x * distanceWord);
            txtSrc.get(x).setY(20);
            txtRes.get(x).setX(x * distanceWord);
            txtRes.get(x).setY(20);
            txtDes.get(x).setX(x * distanceWord);
            txtDes.get(x).setY(20);
            srcGroup.getChildren().add(txtSrc.get(x));
            desGroup.getChildren().add(txtDes.get(x));
            resGroup.getChildren().add(txtRes.get(x));
        }

        parent.addAll(srcGroup, desGroup, resGroup);

        srcGroup.setLayoutX(posX + srcRec.getWidth() / 2 - srcGroup.getLayoutBounds().getWidth() / 2);
        srcGroup.setLayoutY(posY + posLabelY + posValueY);

        desGroup.setLayoutX(posX + srcRec.getWidth() + 150 + ( desRec.getLayoutBounds().getWidth() / 2 - desGroup.getLayoutBounds().getWidth() / 2));
        desGroup.setLayoutY(posY + posLabelY + posValueY);

        resGroup.setLayoutX(posX + desRec.getX() / 2 - srcRec.getX() / 2 + ( resRec.getLayoutBounds().getWidth() / 2 - resGroup.getLayoutBounds().getWidth() / 2));
        resGroup.setLayoutY(posY + 140 + posLabelY + posValueY);

//        ArrayList<Line> pointerRes = new ArrayList<>();
//       for(int x = 0; x < 4; x++){
//           int srcXCompute = new Double(posX + ( srcRec.getWidth() / 2 - srcGroup.getLayoutBounds().getWidth() / 2)
//                   - (srcGroup.getLayoutBounds().getWidth() / 2 - (txtSrc.get(x).getLayoutBounds().getWidth() / 2 + distanceWord) / 2) * 7).intValue();
//            int srcYCompute = new Double(posY + posLabelY + posValueY).intValue();


//           Line line = timeFunc.drawLine(srcXCompute, srcYCompute, 150, 150);
//           parent.add(line);
//       }
    }
}
