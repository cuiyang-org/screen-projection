package org.cuiyang.projection.controller;

import com.android.ddmlib.IDevice;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
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
        if (start.getText().equals("启动")) {
            // 启动
            if (device != null && device.isOnline()) {
                screenProjection = new ScreenProjection(device, this);
                screenProjection.start();
                start.setText("暂停");
            } else {
                Alert alert = new Alert(Start.primaryStage);
                alert.showAndWait();
            }
        } else {
            // 暂停
            if (screenProjection != null) {
                screenProjection.close();
            }
            start.setText("启动");
        }
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
        deviceManager = new DeviceManager();
        deviceManager.init();

        new Thread(() -> {
            while (true) {
                IDevice[] devices = deviceManager.getDevices();
                if (devices != null && devices.length > 0) {
                    device = devices[0];
                    Platform.runLater(() -> {
                        bottomInfo.setText(device.getName() + " - " + device.getState().name().toLowerCase());
                        bottomInfo.setTextFill(Paint.valueOf("green"));
                    });
                } else {
                    // 关闭同屏
                    if (screenProjection != null) {
                        screenProjection.close();
                    }
                    start.setText("启动");
                    Platform.runLater(() -> {
                        bottomInfo.setText("未连接");
                        bottomInfo.setTextFill(Paint.valueOf("red"));
                    });
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
