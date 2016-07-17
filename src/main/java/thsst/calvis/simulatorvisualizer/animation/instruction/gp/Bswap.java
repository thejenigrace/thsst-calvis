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

import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Bswap extends CalvisAnimation {

    @Override
    public void animate(ScrollPane tab) {
        this.root.getChildren().clear();
        tab.setContent(root);

        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();

        // ANIMATION ASSETS
        Token[] tokens = currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ ) {
            System.out.println(tokens[i] + " : " + tokens[i].getClass());
        }

        // CODE HERE
        int width = 100;
        int height = 150;
        Rectangle desRectangle = this.createRectangle(tokens[0], width, height);
//        Rectangle srcRectangle = this.createRectangle(tokens[0], width, height);

        if ( desRectangle != null) {
            desRectangle.setX(300);
            desRectangle.setY(120);
            desRectangle.setArcWidth(10);
            desRectangle.setArcHeight(10);
            root.getChildren().add(desRectangle);
            int desSize = 0;
            desSize = registers.getBitSize(tokens[0]);


            Text desLabelText = this.createLabelText(300, 70, tokens[0]);
            Text desValueText = this.createValueText(tokens[0], registers, memory, desSize);
            String wholeValues = desValueText.getText();
            ArrayList<Text> arrValuesText = new ArrayList<>();

            for(int x = 0; x < 8; x += 2){
                arrValuesText.add(new Text(wholeValues.substring(x, x + 2)));

            }

            for(int x = 0; x < 4; x++){
                arrValuesText.get(x).setX(desLabelText.getX() + desLabelText.getX() / 6.75);
                arrValuesText.get(x).setY(240 - (25 * x));
            }


            root.getChildren().add(desLabelText);
            for(int x = 0; x < arrValuesText.size(); x++){
                root.getChildren().add(arrValuesText.get(x));
            }

            TranslateTransition desLabelTransition = new TranslateTransition();
//            // ANIMATION LOGICoO
//            TranslateTransition desLabelTransition = new TranslateTransition();
//            TranslateTransition desTransition = new TranslateTransition(new Duration(1000), desValueText);
            ArrayList<TranslateTransition> desValuesTransition = new ArrayList<>();
            for(int x = 0; x < arrValuesText.size(); x++){
                desValuesTransition.add( new TranslateTransition(new Duration(2000), arrValuesText.get(x)));
            }

//             Destination label static
            desLabelTransition.setNode(desLabelText);
            desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() - desLabelText.getLayoutBounds().getWidth()) / 2));
            desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 2));
            desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
            desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

            desLabelTransition.play();

            // values
            // Destination value moving
//            srcTransition.setNode(srcValueText);
            desValuesTransition.get(0).setInterpolator(Interpolator.LINEAR);
            desValuesTransition.get(0).fromXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() / 4.8 - desLabelText.getLayoutBounds().getWidth())));
            desValuesTransition.get(0).toXProperty().bind(desValuesTransition.get(0).fromXProperty());
            desValuesTransition.get(0).toYProperty().bind(desRectangle.translateXProperty()
                    .add((-75)));

            desValuesTransition.get(3).setInterpolator(Interpolator.LINEAR);
            desValuesTransition.get(3).fromXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() / 4.8 - desLabelText.getLayoutBounds().getWidth())));
            desValuesTransition.get(3).toXProperty().bind(desValuesTransition.get(3).fromXProperty());
            desValuesTransition.get(3).toYProperty().bind(desRectangle.translateXProperty()
                    .add((75)));

            desValuesTransition.get(2).setInterpolator(Interpolator.LINEAR);
            desValuesTransition.get(2).fromXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() / 4.8 - desLabelText.getLayoutBounds().getWidth())));
            desValuesTransition.get(2).toXProperty().bind(desValuesTransition.get(2).fromXProperty());
            desValuesTransition.get(2).toYProperty().bind(desRectangle.translateXProperty()
                    .add((25)));

            desValuesTransition.get(1).setInterpolator(Interpolator.LINEAR);
            desValuesTransition.get(1).fromXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth() / 4.8 - desLabelText.getLayoutBounds().getWidth())));
            desValuesTransition.get(1).toXProperty().bind(desValuesTransition.get(1).fromXProperty());
            desValuesTransition.get(1).toYProperty().bind(desRectangle.translateXProperty()
                    .add((-25)));

            for(int x = 0; x < 4; x++){
                desValuesTransition.get(x).play();
            }
        }
    }
}

