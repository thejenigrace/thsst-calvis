package thsst.calvis.simulatorvisualizer.model;

import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.configuration.model.exceptions.MemoryReadException;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Created by Ivan on 7/10/2016.
 */
public class TimeLineFunction{
	private Timeline tl;
	private Group root;
	private RegisterList registers;
	private Memory memory;

	public TimeLineFunction(Timeline timeline, Group root, RegisterList registers, Memory memory){
		tl = timeline;
		this.root = root;
		this.registers = registers;
		this.memory = memory;
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

	public void setTimelinePosition(double xProperty, double yProperty, Text shape){
		tl.getKeyFrames().add(new KeyFrame(Duration.ZERO,
				new KeyValue(shape.translateXProperty(), xProperty),
				new KeyValue(shape.translateYProperty(), yProperty)
		));
		tl.getKeyFrames().add(new KeyFrame(new Duration(4000),
				new KeyValue(shape.translateXProperty(), xProperty),
				new KeyValue(shape.translateYProperty(), yProperty)
		));
	}

	public void setTimelinePosition(double xProperty, double yProperty, Shape shape){
		tl.getKeyFrames().add(new KeyFrame(Duration.ZERO,
				new KeyValue(shape.translateXProperty(), xProperty),
				new KeyValue(shape.translateYProperty(), yProperty)
		));
		tl.getKeyFrames().add(new KeyFrame(new Duration(4000),
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

	public Text generateText(Text text, double size, String color){
		text.setFont(Font.font(size));
		text.setFill(Color.web(color));
//		root.getChildren().add(text);
		return text;
	}

	public Text generateText(Text text, double size, String color, FontWeight weight, String font){
		text.setFill(Color.web(color));
		text.setFont(Font.font(font, weight, size));
//		root.getChildren().add(text);
		return text;
	}

	public Rectangle createRectangle(double length, double width, Color color){
		return new Rectangle( length, width, color);
	}

	public void editText(Text text, double size, String color, String value){
		text.setText(value);
		text.setFont(Font.font(size));
		text.setFill(Color.web(color));
		root.getChildren().add(text);
	}

	public int operandSizeGP(Token[] tokens, RegisterList registers, Memory memory){
		int desSize = 0;
		if ( tokens[0].getType() == Token.REG )
			desSize = registers.getBitSize(tokens[0]);
		else if ( tokens[0].getType() == Token.MEM && tokens[1].getType() == Token.REG )
			desSize = registers.getBitSize(tokens[1]);
		else
			desSize = memory.getBitSize(tokens[0]);
		return desSize;
	}


	public void fadeText(FadeTransition ft, Text shape, double from, double to){
		ft.setFromValue(from);
		ft.setToValue(to);
		ft.setCycleCount(Timeline.INDEFINITE);
		ft.setAutoReverse(true);
	}

	public int getBitSize(Token token){
		if(token.isMemory()){
			return memory.getBitSize(token);
		}
		else{
			return registers.getBitSize(token);
		}
	}

	public int parseHex(Token token){
		if(token.isRegister()){
			return Integer.parseInt(registers.get(token), 16);
		}
		else if(token.isHex()){
			return Integer.parseInt(token.getValue(), 16);
		}
			return 0;
	}

	public String getValue(Token token, int desSize){
		if(token.isMemory()){
			try{
				return memory.read(token, desSize);
			} catch(MemoryReadException e){
				e.printStackTrace();
			}
		}
		else if(token.isRegister()){
			return registers.get(token);
		}
		else if(token.isHex()){
			return token.getValue();
		}
		else if(token.isLabel()){
			return token.getValue();
		}
			return null;
	}


	public void hideshowElement(Text text, double time, boolean hideShow){
		Timeline timeline = new Timeline();
		tl.getKeyFrames().add(new KeyFrame(Duration.millis(time),
				new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						text.setVisible(hideShow);
					}
				}));
	}

	public String getValue(Token token){
		return registers.get(token);
	}
}
