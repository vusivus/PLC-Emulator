/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ld;


import com.vts.jplceditor.ld.component.LDInstruction;
import com.vts.jplceditor.ld.component.LDInstructionManager;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Vusivus
 */
public class LDInstructionsTest extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Point2D p = new Point2D(60,50);
        LDInstruction in = new LDInstruction(p);
        LDInstructionManager ldi = new LDInstructionManager(p.subtract(40,0));
        ldi.addInstruction(0, in);
        root.getChildren().add(ldi.getParent());
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
