package thsst.calvis.simulatorvisualizer.animation.instruction.gp;

import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Jennica on 08/07/2016.
 */
public class Xlat extends CalvisAnimation {

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
//        tab.setContent(root);
        scrollPane.setContent(root);

        RegisterList registers = this.currentInstruction.getRegisters();
        Memory memory = this.currentInstruction.getMemory();

        int width = 140;
        int height = 70;

        Rectangle desRectangle = this.createRectangle(Token.REG, width, height);
        Rectangle srcRectangle = this.createRectangle(Token.REG, width, height);

        if ( desRectangle != null && srcRectangle != null ) {
            desRectangle.setX(110);
            desRectangle.setY(100);
            desRectangle.setArcWidth(10);
            desRectangle.setArcHeight(10);

            srcRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.widthProperty().getValue() + 100);
            srcRectangle.setY(100);
            srcRectangle.setArcWidth(10);
            srcRectangle.setArcHeight(10);

            root.getChildren().addAll(desRectangle, srcRectangle);

            String desRegisterName = "AL";
            String srcRegisterName = "BX";

            int desSize = registers.getBitSize(desRegisterName);
            int srcSize = registers.getBitSize(srcRegisterName);

            Text desLabelText = new Text(desRegisterName);
            Text desValueText = new Text(registers.get(desRegisterName));
            Text srcLabelText = new Text(srcRegisterName);
            Text srcValueText = new Text(registers.get(srcRegisterName));

            desLabelText.setX(100);
            desLabelText.setY(100);

            desValueText.setX(100);
            desValueText.setY(100);

            srcLabelText.setX(100);
            srcLabelText.setY(100);

            srcValueText.setX(100);
            srcValueText.setY(100);

            root.getChildren().addAll(desLabelText, desValueText, srcLabelText, srcValueText);

            // ANIMATION LOGIC
            TranslateTransition desLabelTransition = new TranslateTransition();
            TranslateTransition desTransition = new TranslateTransition(new Duration(1000), desValueText);
            TranslateTransition srcLabelTransition = new TranslateTransition();
            TranslateTransition srcTransition = new TranslateTransition();

            // Destination label static
            desLabelTransition.setNode(desLabelText);
            desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(10 + (desRectangle.getLayoutBounds().getWidth() - desLabelText.getLayoutBounds().getWidth()) / 2));
            desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 3));
            desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
            desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

            // Destination value moving
            desTransition.setInterpolator(Interpolator.LINEAR);
            desTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 110)
                    .add((srcRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                    .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
            desTransition.toXProperty().bind(desRectangle.translateXProperty()
                    .add(10 + (desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desTransition.toYProperty().bind(desTransition.fromYProperty());

            // Source label static
            srcLabelTransition.setNode(srcLabelText);
            srcLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 110)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
            srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
            srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

            // Source value static
            srcTransition.setNode(srcValueText);
            srcTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 110)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));
            srcTransition.fromYProperty().bind(desTransition.fromYProperty());
            srcTransition.toXProperty().bind(srcTransition.fromXProperty());
            srcTransition.toYProperty().bind(srcTransition.fromYProperty());

            // Play 1000 milliseconds of animation
            desLabelTransition.play();
            srcLabelTransition.play();
            desTransition.play();
            srcTransition.play();
        }
    }
}
