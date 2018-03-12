package org.cuiyang.projection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.cuiyang.projection.controller.MainController;
import org.cuiyang.projection.core.DeviceManager;

import static org.cuiyang.minicap.util.ResourceUtils.getResource;

/**
 * Start
 *
 * @author cuiyang
 * @since 2018/3/6
 */
public class Start extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Start.primaryStage = primaryStage;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getResource("main.fxml"));
        Parent root = fxmlLoader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("投屏1.0");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
