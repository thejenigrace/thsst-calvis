package thsst.calvis.simulatorvisualizer.animation.instruction.mmx;

import javafx.animation.TranslateTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Jennica on 19/07/2016.
 */
public class Pcmpgt extends CalvisAnimation {

    int packedSize;

    public Pcmpgt(int packedSize) {
        this.packedSize = packedSize;
    }

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
        scrollPane.setContent(root);

        RegisterList registers = this.currentInstruction.getRegisters();
        Memory memory = this.currentInstruction.getMemory();

        // ANIMATION ASSETS
        Token[] tokens = this.currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ )
            System.out.println(tokens[i] + " : " + tokens[i].getClass());

        // CODE HERE
        int width = 280;
        int height = 60;

        Rectangle desRectangle = this.createRectangle(tokens[0], width, height);
        Rectangle srcRectangle = this.createRectangle(tokens[1], width, height);
        Rectangle resultRectangle = this.createRectangle(tokens[0], width, height);

        desRectangle.setX(X);
        desRectangle.setY(Y);
        desRectangle.setArcWidth(10);
        desRectangle.setArcHeight(10);

        srcRectangle.setX(X);
        srcRectangle.setY(170);
        srcRectangle.setArcWidth(10);
        srcRectangle.setArcHeight(10);

        resultRectangle.setX(X);
        resultRectangle.setY(240);
        resultRectangle.setArcWidth(10);
        resultRectangle.setArcHeight(10);

        root.getChildren().addAll(desRectangle, srcRectangle, resultRectangle);

        // Destination is always a Register: XMM or MM
        int desBitSize = registers.getBitSize(tokens[0]);

        // Cut per byte
        String desValue = this.finder.getRegister(tokens[0].getValue());
        System.out.println("desValue = " + desValue);

        String desValueChop = this.chopHexValue(desValue, packedSize);
        System.out.println(desValueChop);

        Text desLabelText = this.createLabelText(X, Y, tokens[0]);
        Text desValueText = new Text(X, Y, desValueChop);

        Text srcLabelText = this.createLabelText(X, Y, tokens[1]);
        String srcValueString = this.getValueString(tokens[1], registers, memory, desBitSize);
        String srcValueChop = this.chopHexValue(srcValueString, packedSize);
        Text srcValueText = new Text(X, Y, srcValueChop);

        Text lineText = new Text(X-15, 237.5, "---------------------------------------------------");
        Text noteText = new Text(X, Y-10, "Note: " + tokens[0].getValue() + " <-- " + tokens[0].getValue() +
                " > " + tokens[1].getValue() + "?");

        Text resultLabelText = this.createLabelText(X, Y, tokens[0]);
        String resultValueString = this.getValueString(tokens[0], registers, memory, desBitSize);
        String resultValueChop = this.chopHexValue(resultValueString, packedSize);
        Text resultValueText = new Text(X, Y, resultValueChop);

        root.getChildren().addAll(desLabelText, desValueText, srcLabelText, srcValueText,
                resultLabelText, resultValueText, lineText, noteText);

        // ANIMATION LOGIC
        TranslateTransition desLabelTransition = new TranslateTransition();
        TranslateTransition desValueTransition = new TranslateTransition();
        TranslateTransition srcLabelTransition = new TranslateTransition();
        TranslateTransition srcValueTransition = new TranslateTransition();
        TranslateTransition resultLabelTransition = new TranslateTransition();
        TranslateTransition resultValueTransition = new TranslateTransition();

        // DES LABEL  -- STATIC
        desLabelTransition.setNode(desLabelText);
        desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                .add((desRectangle.getLayoutBounds().getWidth() - desLabelText.getLayoutBounds().getWidth()) / 2));
        desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
                .add(desRectangle.getLayoutBounds().getHeight() / 3));
        desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
        desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

        // DES VALUE -- STATIC
        desValueTransition.setNode(desValueText);
        desValueTransition.fromXProperty().bind(desRectangle.translateXProperty()
                .add((desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2 + 5));
        desValueTransition.fromYProperty().bind(desRectangle.translateYProperty()
                .add(desRectangle.getLayoutBounds().getHeight() / 1.5));
        desValueTransition.toXProperty().bind(desValueTransition.fromXProperty());
        desValueTransition.toYProperty().bind(desValueTransition.fromYProperty());

        // SRC LABEL -- STATIC
        srcLabelTransition.setNode(srcLabelText);
        srcLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                .add((srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
        srcLabelTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                .add(desRectangle.getLayoutBounds().getHeight() + 10)
                .add(srcRectangle.getLayoutBounds().getHeight() / 3));
        srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
        srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

        // SRC VALUE -- STATIC
        srcValueTransition.setNode(srcValueText);
        srcValueTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                .add((srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2 + 5));
        srcValueTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                .add(desRectangle.getLayoutBounds().getHeight() + 10)
                .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
        srcValueTransition.toXProperty().bind(srcValueTransition.fromXProperty());
        srcValueTransition.toYProperty().bind(srcValueTransition.fromYProperty());

        // RESULT LABEL -- STATIC
        resultLabelTransition.setNode(resultLabelText);
        resultLabelTransition.fromXProperty().bind(resultRectangle.translateXProperty()
                .add((resultRectangle.getLayoutBounds().getWidth() - resultLabelText.getLayoutBounds().getWidth()) / 2));
        resultLabelTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                .add(desRectangle.getLayoutBounds().getHeight() + 10)
                .add(srcRectangle.getLayoutBounds().getHeight() + 10)
                .add(resultRectangle.getLayoutBounds().getHeight() / 3));
        resultLabelTransition.toXProperty().bind(resultLabelTransition.fromXProperty());
        resultLabelTransition.toYProperty().bind(resultLabelTransition.fromYProperty());

        // RESULT VALUE -- STATIC
        resultValueTransition.setNode(resultValueText);
        resultValueTransition.fromXProperty().bind(resultRectangle.translateXProperty()
                .add((resultRectangle.getLayoutBounds().getWidth() - resultValueText.getLayoutBounds().getWidth()) / 2 + 5));
        resultValueTransition.fromYProperty().bind(resultRectangle.translateYProperty()
                .add(desRectangle.getLayoutBounds().getHeight() + 10)
                .add(srcRectangle.getLayoutBounds().getHeight() + 10)
                .add(resultRectangle.getLayoutBounds().getHeight() / 1.5));
        resultValueTransition.toXProperty().bind(resultValueTransition.fromXProperty());
        resultValueTransition.toYProperty().bind(resultValueTransition.fromYProperty());

        // PLAY TRANSLATE TRANSITION
        desLabelTransition.play();
        desValueTransition.play();
        srcLabelTransition.play();
        srcValueTransition.play();
        resultLabelTransition.play();
        resultValueTransition.play();
    }
}


