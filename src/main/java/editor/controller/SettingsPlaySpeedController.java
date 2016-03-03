package editor.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jennica on 03/03/2016.
 */
public class SettingsPlaySpeedController implements Initializable{

    @FXML
    private Slider sliderPlaySpeed;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sliderPlaySpeed.setMin(0);
        sliderPlaySpeed.setMax(5000);
        sliderPlaySpeed.setValue(1500);
        sliderPlaySpeed.setShowTickLabels(true);
        sliderPlaySpeed.setShowTickMarks(true);
        sliderPlaySpeed.setSnapToTicks(true);
        sliderPlaySpeed.setMajorTickUnit(2500);
//        sliderPlaySpeed.setMinorTickCount(100);
        sliderPlaySpeed.setBlockIncrement(500);
        sliderPlaySpeed.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("newValue = " + newValue);
            }
        });
    }
}
