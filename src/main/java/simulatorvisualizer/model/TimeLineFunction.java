package simulatorvisualizer.model;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Created by Ivan on 7/10/2016.
 */
public class TimeLineFunction{
	private Timeline tl;
	private Group root;

	public TimeLineFunction(Timeline timeline, Group root){
		tl = timeline;
		this.root = root;
	}

	public void addTimeline(double xProperty, double yProperty, double duration, Shape shape){
		tl.getKeyFrames().add(new KeyFrame(new Duration(duration),
				new KeyValue(shape.translateXProperty(), xProperty),
				new KeyValue(shape.translateYProperty(), yProperty)
				));
	}

	public void addTimeline(double xProperty, double yProperty, double duration, Text shape){
		tl.getKeyFrames().add(new KeyFrame(new Duration(duration),
				new KeyValue(shape.translateXProperty(), xProperty),
				new KeyValue(shape.translateYProperty(), yProperty)
		));
	}

	public void addTimeline(double xProperty, double yProperty, Duration duration, Text shape){
		tl.getKeyFrames().add(new KeyFrame(duration,
				new KeyValue(shape.translateXProperty(), xProperty),
				new KeyValue(shape.translateYProperty(), yProperty)
		));
	}

	public void addTimeline(double xProperty, double yProperty, Duration duration, Shape shape){
		tl.getKeyFrames().add(new KeyFrame(duration,
				new KeyValue(shape.translateXProperty(), xProperty),
				new KeyValue(shape.translateYProperty(), yProperty)
		));
	}

	public void editText(Text text, double size, String color){
		text.setFont(Font.font(size));
		text.setFill(Color.web(color));
		root.getChildren().add(text);
	}
}
