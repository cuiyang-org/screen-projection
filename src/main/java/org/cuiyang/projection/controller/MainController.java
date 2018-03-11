package org.cuiyang.projection.controller;

import com.android.ddmlib.IDevice;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import org.cuiyang.projection.Start;
import org.cuiyang.projection.core.DeviceManager;
import org.cuiyang.projection.core.ScreenListener;
import org.cuiyang.projection.core.ScreenProjection;
import org.cuiyang.projection.widget.Alert;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * MainController
 *
 * @author cy48576
 * @version $ Id: MainController.java, v 0.1 2018/2/7 cy48576 Exp $
 */
public class MainController implements ScreenListener, Initializable{
    public Button start;
    public Button setting;
    public Canvas canvas;
    public Label bottomInfo;
    public Button zoomUp;
    public Button zoomDown;

    private IDevice device;
    private ScreenProjection screenProjection;
    private DeviceManager deviceManager;

    public MainController() {
    }

    public void start() throws Exception {
        IDevice[] devices = deviceManager.getDevices();
        if (devices == null || devices.length == 0) {
            Alert alert = new Alert(Start.primaryStage);
            alert.showAndWait();
            return;
        }
        device = devices[0];
        screenProjection = new ScreenProjection(device, this);
        screenProjection.start();
    }

    @Override
    public void projection(Image frame) {
        canvas.setWidth(frame.getWidth());
        canvas.setHeight(frame.getHeight());
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.drawImage(frame, 0, 0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setDeviceManager(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }
}
