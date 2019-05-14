package ld;

import com.vts.jplceditor.ld.component.LDLine;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vusumuzi_Tshabangu
 */
public class LDLineTest extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        Point2D p1,p2;
        p1 = new Point2D(235,245);
        p2 = new Point2D(556,245);
        LDLine b= new LDLine(p1,p2);
        b.setEndPoint(p2.subtract(0, 200));
        b.setStartPoint(p1.subtract(0,200));
        Pane p = new Pane();
        p.getChildren().add(b);
        Scene scene = new Scene(p,640,480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
