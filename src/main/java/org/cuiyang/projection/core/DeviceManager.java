package org.cuiyang.projection.core;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static org.cuiyang.minicap.util.ResourceUtils.getResourceAsStream;

/**
 * 管理设备
 *
 * @author cy48576
 */
@Slf4j
public class DeviceManager {
    /** adb目录 */
    private static final String ADB_DIR = "adb/windows/";
    /** adb路径 */
    private static final String ADB_PATH = ADB_DIR + "adb";

    private static final String ADB_WIN = ADB_DIR + "adb.exe";
    private static final String ADB_WIN_API = ADB_DIR + "AdbWinApi.dll";
    private static final String ADB_WIN_USB_API = ADB_DIR + "AdbWinUsbApi.dll";

    /** adb */
    private AndroidDebugBridge bridge;

    /**
     * 初始化
     */
    public void init() {
        uncompressAdb();
        AndroidDebugBridge.init(false);
        bridge = AndroidDebugBridge.createBridge(ADB_PATH, false);
    }

    /**
     * 获取当前设备
     * @return 设备列表
     */
    public IDevice[] getDevices() {
        return bridge.getDevices();
    }

    /**
     * 解压adb
     */
    private void uncompressAdb() {
        try {
            FileUtils.forceMkdir(new File(ADB_DIR));
            String[] adbFiles = {ADB_WIN, ADB_WIN_API, ADB_WIN_USB_API};
            for (String adbFile : adbFiles) {
                File file = new File(adbFile);
                if (!file.exists()) {
                    FileUtils.copyInputStreamToFile(getResourceAsStream(adbFile), file);
                }
            }
        } catch (IOException e) {
            log.error("解压adb文件失败", e);
            throw new IllegalStateException("解压adb文件失败", e);
        }
    }

    /**
     * 等待设备连接
     */
    private void waitForDevice(AndroidDebugBridge bridge) {
        int count = 0;
        while (!bridge.hasInitialDeviceList()) {
            try {
                Thread.sleep(100);
                count++;
            } catch (InterruptedException ignored) {
            }
            if (count > 300) {
                log.error("Timeout");
                break;
            }
        }
    }
}
