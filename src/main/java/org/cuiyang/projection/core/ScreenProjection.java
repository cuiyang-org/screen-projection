package org.cuiyang.projection.core;

import com.android.ddmlib.IDevice;
import javafx.scene.image.Image;
import org.cuiyang.minicap.MinicapClient;
import org.cuiyang.minicap.MinicapServer;

import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeoutException;

/**
 * ScreenProjection
 *
 * @author cuiyang
 * @since 2018/3/6
 */
public class ScreenProjection extends Thread{

    private IDevice device;
    private MinicapServer server;
    private MinicapClient client;
    private ScreenListener listener;

    public ScreenProjection(IDevice device, ScreenListener listener) {
        this.device = device;
        this.listener = listener;
    }

    private void startServer() throws TimeoutException, InterruptedException {
        server = new MinicapServer(device, 1717, 0.4f, 0, 100);
        server.start();
        server.waitRunning(5000);
    }

    private void startClient() {
        client = new MinicapClient(1717);
        client.start();
    }

    @Override
    public void run() {
        try {
            startServer();
            startClient();

            while (true) {
                byte[] take = client.take();
                Image image = new Image(new ByteArrayInputStream(take));
                try {
                    listener.projection(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
