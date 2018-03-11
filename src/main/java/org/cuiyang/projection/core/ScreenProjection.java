package org.cuiyang.projection.core;

import com.android.ddmlib.IDevice;
import javafx.scene.image.Image;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.cuiyang.minicap.MinicapClient;
import org.cuiyang.minicap.MinicapServer;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.util.concurrent.TimeoutException;

/**
 * ScreenProjection
 *
 * @author cuiyang
 * @since 2018/3/6
 */
@Slf4j
public class ScreenProjection extends Thread implements Closeable {

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

            while (server.isRunning()) {
                byte[] take = client.take();
                Image image = new Image(new ByteArrayInputStream(take));
                try {
                    listener.projection(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            log.error("屏幕映射失败", e);
        } finally {
            close();
        }
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(server);
        IOUtils.closeQuietly(client);
    }
}
