/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor;

import com.vts.jplceditor.resource.R;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author Vusumuzi_Tshabangu
 */
public class JPlcEditor extends Application {

    URL url = null;
    VBox root = null;
    Rectangle2D screenBounds;

    @Override
    public void start(Stage primaryStage) {
        url = getClass().getResource(R.WINDOW_MAIN);
        screenBounds = Screen.getPrimary().getVisualBounds();
        try {
            FXMLLoader loader = new FXMLLoader(url);
            root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setX(screenBounds.getMinX());
            primaryStage.setY(screenBounds.getMinX());
            primaryStage.setWidth(screenBounds.getWidth());
            primaryStage.setHeight(screenBounds.getHeight());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(JPlcEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
