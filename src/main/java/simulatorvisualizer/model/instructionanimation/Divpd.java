package simulatorvisualizer.model.instructionanimation;

import configuration.model.engine.Calculator;
import configuration.model.engine.Memory;
import configuration.model.engine.RegisterList;
import configuration.model.engine.Token;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import simulatorvisualizer.model.CalvisAnimation;
import simulatorvisualizer.model.TimeLineFunction;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Divpd extends CalvisAnimation {

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
        Token des = tokens[0];
        Token src = tokens[1];
        int size = timeFunc.getBitSize(des);
        String desStr = timeFunc.getPreviousValue(des, size);
        String srcStr = timeFunc.getPreviousValue(src, size);
        String resultStr = timeFunc.getValue(des, size);
        System.out.println(resultStr + " jhere here");
        int operationSize = 16;
        Text sign = timeFunc.generateText(new Text("/"), 30, "#98777b");
        Text equal = timeFunc.generateText(new Text("="), 30, "#98777b");
        String dividedStrDes = "";
        String dividedStrSrc = "";
        String dividedStrRes = "";
        String dividedConvertedSrc = "";
        String dividedConvertedDes = "";
        String dividedConvertedRes = "";

        for(int x = 0; x < (size/2) / 2; x = x + operationSize){
            String extractedHexDes = desStr.substring(0 + x, operationSize + x);
            String extractedHexSrc = (srcStr.substring(0 + x, operationSize + x));
            String extractedHexRes = (resultStr.substring(0 + x, operationSize + x));

            dividedStrDes += extractedHexDes + "     ";
            dividedStrSrc += extractedHexSrc + "     ";
            dividedStrRes += (resultStr.substring(0 + x, operationSize + x)) + "     ";

            dividedConvertedDes += c.hexToDoublePrecisionFloatingPoint(extractedHexDes) + "      ";
            dividedConvertedSrc += c.hexToDoublePrecisionFloatingPoint(extractedHexSrc) + "      ";
            dividedConvertedRes += c.hexToDoublePrecisionFloatingPoint(extractedHexRes) + "      ";
        }

        Rectangle fake = new Rectangle(0,0, Color.web("#4b5320"));

        fake.setX(0);
        fake.setY(0);

        double cordX = 200;
        double cordY = 25;

        Rectangle desRec = this.createRectangle(des, 500, 90);
        Rectangle srcRec = this.createRectangle(src, 500, 90);
        Rectangle resRec = this.createRectangle(des, 500, 90);
        parent.addAll(fake, desRec, srcRec, resRec, sign, equal);

        Text desLabel = this.createLabelText(des);
        Text srcLabel = this.createLabelText(src);
        Text resLabel = this.createLabelText(des);

        Text desVals = new Text();
        Text srcVals = new Text();
        Text resVals = new Text();


        desRec.setX(cordX);
        desRec.setY(cordY);

        srcRec.setX(cordX);
        srcRec.setY(cordY + 50 + srcRec.getLayoutBounds().getHeight());

        resRec.setX(cordX);
        resRec.setY(srcRec.getY() + 50 + srcRec.getLayoutBounds().getHeight());

        desLabel.setX(srcRec.getLayoutBounds().getWidth() / 2 - desLabel.getLayoutBounds().getWidth() / 2 + desRec.getX() );
        desLabel.setY(cordY + 20);

        srcLabel.setX(srcRec.getLayoutBounds().getWidth() / 2 - srcLabel.getLayoutBounds().getWidth() / 2 + srcRec.getX());
        srcLabel.setY(cordY + 50 + srcRec.getLayoutBounds().getHeight() + 20);

        resLabel.setX(resRec.getLayoutBounds().getWidth() / 2 - resLabel.getLayoutBounds().getWidth() / 2 + srcRec.getX());
        resLabel.setY(srcRec.getY() + 50 + srcRec.getLayoutBounds().getHeight() + 20);


        Text desSinglePresVal = new Text(dividedConvertedDes);
        Text srcSinglePresVal = new Text(dividedConvertedSrc);
        Text resSinglePresVal = new Text(dividedConvertedRes);

        desVals.setText(dividedStrDes);
        srcVals.setText(dividedStrSrc);
        resVals.setText(dividedStrRes);
        parent.addAll(desVals, srcVals, resVals, srcSinglePresVal, resSinglePresVal);


        parent.addAll(resLabel, srcLabel, desLabel, desSinglePresVal);

        double desComp = desRec.getLayoutBounds().getWidth() / 2 - desVals.getLayoutBounds().getWidth() / 2 + desRec.getX();
        desVals.setX( desComp);
        desVals.setY(cordY + 40);

        desSinglePresVal.setX(desRec.getLayoutBounds().getWidth() / 2 - desSinglePresVal.getLayoutBounds().getWidth() / 2 + desRec.getX());
        desSinglePresVal.setY(desVals.getY() + 20);

        double srcComp = srcRec.getLayoutBounds().getWidth() / 2 - srcVals.getLayoutBounds().getWidth() / 2 + srcRec.getX();
        srcVals.setX(srcComp);
        srcVals.setY(cordY + 50 + srcRec.getLayoutBounds().getHeight() + 40);

        srcSinglePresVal.setX(desRec.getLayoutBounds().getWidth() / 2 - srcSinglePresVal.getLayoutBounds().getWidth() / 2 + desRec.getX());
        srcSinglePresVal.setY(srcVals.getY() + 20);

        double resComp = resRec.getLayoutBounds().getWidth() / 2 - resVals.getLayoutBounds().getWidth() / 2 + resRec.getX();
        resVals.setX(resComp);
        resVals.setY(srcRec.getY() + 50 + srcRec.getLayoutBounds().getHeight() + 40);

        resSinglePresVal.setX(desRec.getLayoutBounds().getWidth() / 2 - resSinglePresVal.getLayoutBounds().getWidth() / 2 + desRec.getX());
        resSinglePresVal.setY(resVals.getY() + 20);

        sign.setX(desRec.getLayoutBounds().getWidth() / 2 - sign.getLayoutBounds().getWidth() / 2 + desRec.getX());
        sign.setY(cordY + 120);

        equal.setX(desRec.getLayoutBounds().getWidth() / 2 - equal.getLayoutBounds().getWidth() / 2 + desRec.getX());
        equal.setY(cordY + 260);

    }
}

