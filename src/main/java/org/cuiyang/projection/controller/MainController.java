package org.cuiyang.projection.controller;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import org.cuiyang.minicap.MinicapClient;
import org.cuiyang.minicap.MinicapServer;
import org.cuiyang.minicap.ddmlib.DdmlibUtils;
import org.cuiyang.projection.core.ScreenListener;
import org.cuiyang.projection.core.ScreenProjection;

import java.io.ByteArrayInputStream;

/**
 * MainController
 *
 * @author cy48576
 * @version $ Id: MainController.java, v 0.1 2018/2/7 cy48576 Exp $
 */
public class MainController implements ScreenListener {
    public Button button;
    public Canvas canvas;
    private IDevice device;
    private ScreenProjection screenProjection;

    public MainController() {
        AndroidDebugBridge.init(false);
        AndroidDebugBridge bridge = AndroidDebugBridge.createBridge("D:\\program\\Android\\Sdk\\platform-tools\\adb", false);
        waitForDevice(bridge);
        IDevice[] devices = bridge.getDevices();
        device = devices[0];
        screenProjection = new ScreenProjection(device, this);
    }

    public void start(MouseEvent mouseEvent) throws Exception {
        screenProjection.start();
    }

    private static void waitForDevice(AndroidDebugBridge bridge) {
        int count = 0;
        while (!bridge.hasInitialDeviceList()) {
            try {
                Thread.sleep(100);
                count++;
            } catch (InterruptedException ignored) {
            }
            if (count > 300) {
                System.err.print("Time out");
                break;
            }
        }
    }

    @Override
    public void projection(Image frame) {
        canvas.setWidth(frame.getWidth());
        canvas.setHeight(frame.getHeight());
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.drawImage(frame, 0, 0);
    }
}
