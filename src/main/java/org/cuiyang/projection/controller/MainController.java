package org.cuiyang.projection.controller;

import com.android.ddmlib.IDevice;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import org.cuiyang.minicap.ScreenListener;
import org.cuiyang.minicap.ScreenProjection;
import org.cuiyang.projection.Start;
import org.cuiyang.projection.core.DeviceManager;
import org.cuiyang.projection.widget.Alert;

import java.io.ByteArrayInputStream;
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

    private float scale = 0.4f;

    public MainController() {
    }

    public void start() throws Exception {
        if ("启动".equals(start.getText())) {
            // 启动
            if (device != null && device.isOnline()) {
                screenProjection = new ScreenProjection(device, this);
                screenProjection.setZoom(this.scale);
                screenProjection.setQuality(80);
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
    public void projection(byte[] frame) {
        Image image = new Image(new ByteArrayInputStream(frame));
        canvas.setWidth(image.getWidth());
        canvas.setHeight(image.getHeight());
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.drawImage(image, 0, 0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deviceManager = new DeviceManager();
        deviceManager.init();
        new DeviceMonitor().start();
    }

    /**
     * 缩小
     * @param mouseEvent MouseEvent
     */
    public void zoomDown(MouseEvent mouseEvent) {
        this.scale -= 0.1;
        update();
    }

    /**
     * 放大
     * @param mouseEvent MouseEvent
     */
    public void zoomUp(MouseEvent mouseEvent) {
        this.scale += 0.1;
        update();
    }

    /**
     * 更新配置
     */
    private void update() {
        this.screenProjection.setZoom(this.scale);
        this.screenProjection.restart();
    }

    /**
     * 设备监控
     */
    private class DeviceMonitor extends Thread {
        @Override
        public void run() {
            // noinspection InfiniteLoopStatement
            while (true) {
                IDevice[] devices = deviceManager.getDevices();
                if (devices != null && devices.length > 0) {
                    device = devices[0];
                    Platform.runLater(() -> {
                        bottomInfo.setText(device.getName() + " - " + device.getState().name().toLowerCase());
                        bottomInfo.setTextFill(Paint.valueOf("green"));
                    });
                } else {
                    device = null;
                    // 关闭同屏
                    if (screenProjection != null) {
                        screenProjection.close();
                    }
                    Platform.runLater(() -> {
                        start.setText("启动");
                        bottomInfo.setText("未连接");
                        bottomInfo.setTextFill(Paint.valueOf("red"));
                    });
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

}
