package org.cuiyang.projection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Start
 *
 * @author cuiyang
 * @since 2018/3/6
 */
public class Start extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        @SuppressWarnings("ConstantConditions")
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("投屏1.0");
        primaryStage.show();
    }
}
