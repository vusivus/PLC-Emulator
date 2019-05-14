/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import com.vts.jplceditor.JPlcEditor;
import com.vts.jplceditor.resource.R;
import com.vts.jplceditor.ui.controller.DownloadPopopController;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author Electronix
 */
public class DownloadPopupTest extends Application {

    URL url = null;
    Parent root = null;
    static DownloadPopopController controller;

    @Override
    public void start(Stage primaryStage) {
        url = getClass().getResource("/com/vts/jplceditor/ui/fxml/DownloadPopup.fxml");
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(url);
            root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(DownloadPopupTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        controller = loader.getController();
        test();
    }

    private static void test() {
        float progress;

        for (int sessions = 0; sessions < 64; sessions++) {

            progress = 64 - sessions;
            progress /= 64;
            progress=1-progress;
            controller.setProgress(progress);
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(DownloadPopupTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
