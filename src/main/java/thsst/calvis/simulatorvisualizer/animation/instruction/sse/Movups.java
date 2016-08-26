package thsst.calvis.simulatorvisualizer.animation.instruction.sse;


import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Movups extends CalvisAnimation {

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
        scrollPane.setContent(this.root);

        RegisterList registers = this.currentInstruction.getRegisters();
        Memory memory = this.currentInstruction.getMemory();

        // ANIMATION ASSETS
        Token[] tokens = this.currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ ) {
            
        }

        // CODE HERE
        int width = 300;
        int height = 70;
        Rectangle desRectangle = this.createRectangle(tokens[0], width, height);
        Rectangle srcRectangle = this.createRectangle(tokens[1], width, height);

        if ( desRectangle != null && srcRectangle != null ) {
            desRectangle.setX(X);
            desRectangle.setY(Y);
            desRectangle.setArcWidth(10);
            desRectangle.setArcHeight(10);

            srcRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.widthProperty().getValue() + X);
            srcRectangle.setY(Y);
            srcRectangle.setArcWidth(10);
            srcRectangle.setArcHeight(10);

            this.root.getChildren().addAll(desRectangle, srcRectangle);

            int desSize;
            if ( tokens[0].getType().equals(Token.REG) )
                desSize = registers.getBitSize(tokens[0]);
            else if ( tokens[0].getType().equals(Token.MEM) && tokens[1].getType().equals(Token.REG) )
                desSize = registers.getBitSize(tokens[1]);
            else
                desSize = memory.getBitSize(tokens[0]);

            Text desLabelText = this.createLabelText(X, Y, tokens[0]);
            Text desValueText = this.createValueText(X, Y, tokens[0], registers, memory, desSize);
            Text srcLabelText = this.createLabelText(X, Y, tokens[1]);
            Text srcValueText = this.createValueText(X, Y, tokens[1], registers, memory, desSize);

            this.root.getChildren().addAll(desLabelText, desValueText, srcLabelText, srcValueText);

            // ANIMATION LOGIC
            TranslateTransition desLabelTransition = new TranslateTransition();
            TranslateTransition desValueTransition = new TranslateTransition(new Duration(1000), desValueText);
            TranslateTransition srcLabelTransition = new TranslateTransition();
            TranslateTransition srcValueTransition = new TranslateTransition();

            // Destination label static
            desLabelTransition.setNode(desLabelText);
            desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() - desLabelText.getLayoutBounds().getWidth()) / 2));
            desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 3));
            desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
            desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

            // Destination value moving
            desValueTransition.setInterpolator(Interpolator.LINEAR);
            desValueTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 100)
                    .add((srcRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desValueTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                    .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
            desValueTransition.toXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desValueTransition.toYProperty().bind(desValueTransition.fromYProperty());

            // Source label static
            srcLabelTransition.setNode(srcLabelText);
            srcLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 100)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
            srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
            srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

            // Source value static
            srcValueTransition.setNode(srcValueText);
            srcValueTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 100)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));
            srcValueTransition.fromYProperty().bind(desValueTransition.fromYProperty());
            srcValueTransition.toXProperty().bind(srcValueTransition.fromXProperty());
            srcValueTransition.toYProperty().bind(srcValueTransition.fromYProperty());

            // Play 1000 milliseconds of animation
            desLabelTransition.play();
            srcLabelTransition.play();
            desValueTransition.play();
            srcValueTransition.play();
        }
    }

//    @Override
//    public void animate(ScrollPane scrollPane) {
//        this.root.getChildren().clear();
//        scrollPane.setContent(root);
//        RegisterList registers = currentInstruction.getRegisters();
//        ObservableList<Node> parent = this.root.getChildren();
//        Memory memory = currentInstruction.getMemory();
//        TimeLineFunction timeFunc = new TimeLineFunction(timeline, root, registers, memory, finder);
//        Calculator c = new Calculator(registers, memory);
//        Token[] tokens = currentInstruction.getParameterTokens();
//        Token des = tokens[0];
//        Token src = tokens[1];
//        int size = timeFunc.getBitSize(des);
//        String desStr = timeFunc.getPreviousValue(des, size);
//        String srcStr = timeFunc.getPreviousValue(src, size);
//        String resultStr = timeFunc.getValue(des, size);

//        int operationSize = 8;
//        Text sign = timeFunc.generateText(new Text("Transfer To:"), 22, "#98777b");
//        Text equal = timeFunc.generateText(new Text("="), 30, "#98777b");
//        String dividedStrDes = "";
//        String dividedStrSrc = "";
//        String dividedStrRes = "";
//        String dividedSinglePresNum = "";
//        String duplicatedividedSinglePresNum = "";
//
//        for(int x = 0; x < (size/2) / 2; x = x + operationSize){
//            String extractedHex = (srcStr.substring(0 + x, operationSize + x));
//            dividedStrDes += desStr.substring(0 + x, operationSize + x) + "     ";
//            dividedStrSrc += extractedHex + "     ";
//            dividedSinglePresNum += c.convertHexToSinglePrecision(extractedHex) + "      ";
//            duplicatedividedSinglePresNum += c.convertHexToSinglePrecision(extractedHex) + "      ";
//        }
//
//        Rectangle fake = new Rectangle(0,0, Color.web("#4b5320"));
//
//        fake.setX(0);
//        fake.setY(0);
//
//        double cordX = 200;
//        double cordY = 25;
//
//        Rectangle desRec = this.createRectangle(des, 400, 80);
//        Rectangle srcRec = this.createRectangle(src, 400, 80);
//        Rectangle resRec = this.createRectangle(des, 400, 80);
//        parent.addAll(fake, desRec, srcRec, sign);
//
//        Text desLabel = this.createLabelText(des);
//        Text srcLabel = this.createLabelText(src);
//        Text dupLabel = new Text(dividedStrSrc);
//
//        Text desVals = new Text();
//        Text srcVals = new Text();
//        Text resVals = new Text();
//        Text convertedVal = new Text();
//        Text convertedValDup = new Text();
//
//        desRec.setX(cordX);
//        desRec.setY(cordY);
//
//        srcRec.setX(cordX);
//        srcRec.setY(cordY + 50 + srcRec.getLayoutBounds().getHeight());
//
////        resRec.setX(cordX);
////        resRec.setY(srcRec.getY() + 50 + srcRec.getLayoutBounds().getHeight());
//
//        desLabel.setX(srcRec.getLayoutBounds().getWidth() / 2 - desLabel.getLayoutBounds().getWidth() / 2 + desRec.getX() );
//        desLabel.setY(cordY + 20);
//
//        srcLabel.setX(srcRec.getLayoutBounds().getWidth() / 2 - srcLabel.getLayoutBounds().getWidth() / 2 + srcRec.getX());
//        srcLabel.setY(cordY + 50 + srcRec.getLayoutBounds().getHeight() + 20);
//
////        resLabel.setX(resRec.getLayoutBounds().getWidth() / 2 - resLabel.getLayoutBounds().getWidth() / 2 + srcRec.getX());
////        resLabel.setY(srcRec.getY() + 50 + srcRec.getLayoutBounds().getHeight() + 20);
//
//
//
//
//
//
//
//
//
//        desVals.setText(dividedStrDes);
//        srcVals.setText(dividedStrSrc);
//        convertedVal.setText(dividedSinglePresNum);
//        convertedVal.setWrappingWidth(desRec.getLayoutBounds().getWidth());
//        convertedValDup.setText(dividedSinglePresNum);
//        convertedValDup.setWrappingWidth(desRec.getLayoutBounds().getWidth() - 40);
//
//        resVals.setText(dividedStrRes);
//        parent.addAll(srcVals, convertedValDup, dupLabel);
//
//
//        parent.addAll(srcLabel, desLabel, convertedVal);
//
//
//        double srcComp = srcRec.getLayoutBounds().getWidth() / 2 - srcVals.getLayoutBounds().getWidth() / 2 + srcRec.getX();
//        srcVals.setX(srcComp);
//        srcVals.setY(cordY + 50 + srcRec.getLayoutBounds().getHeight() + 40);
//
//        double computedComp = desRec.getLayoutBounds().getWidth() / 2 - convertedVal.getLayoutBounds().getWidth() / 4 + desRec.getX();
//        convertedVal.setX(computedComp);
//        convertedVal.setY(cordY + 70 + desRec.getLayoutBounds().getHeight() + 40);
//
////        convertedValDup.setX(computedComp);
////        convertedValDup.setY(cordY + 70 + desRec.getLayoutBounds().getHeight() + 40);
//
//        sign.setX(desRec.getLayoutBounds().getWidth() / 2 - sign.getLayoutBounds().getWidth() / 2 + desRec.getX());
//        sign.setY(cordY + 110);
//
//        double desComp = desRec.getLayoutBounds().getWidth() / 2 - convertedValDup.getLayoutBounds().getWidth() / 4 + desRec.getX();
//        timeFunc.addTimeline(computedComp, cordY + 70 + desRec.getLayoutBounds().getHeight() + 40, 0, convertedValDup);
//        timeFunc.addTimeline(desComp, cordY + 60, 2000, convertedValDup);
//
//        double desLabelComp = desRec.getLayoutBounds().getWidth() / 2 - srcVals.getLayoutBounds().getWidth() / 2 + desRec.getX();
//        timeFunc.addTimeline(srcComp, cordY + 50 + srcRec.getLayoutBounds().getHeight() + 40, 0, dupLabel);
//        timeFunc.addTimeline(desLabelComp, cordY + 40, 2000, dupLabel);
//        timeline.play();
//
//    }
}

