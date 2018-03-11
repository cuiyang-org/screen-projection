package org.cuiyang.projection.widget;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

import static org.cuiyang.minicap.util.ResourceUtils.getResource;

/**
 * Alert
 *
 * @author cy48576
 */
public class Alert extends Stage {

    public Alert() {
        init(null);
    }

    public Alert(Window owner) {
        init(owner);
    }

    private void init(Window owner) {
        setTitle("系统提示");
        initModality(Modality.APPLICATION_MODAL);
        initOwner(owner);
        setResizable(false);

        Pane root;
        try {
            root = FXMLLoader.load(getResource("alert.fxml"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        Scene scene = new Scene(root);
        setScene(scene);
    }
}
