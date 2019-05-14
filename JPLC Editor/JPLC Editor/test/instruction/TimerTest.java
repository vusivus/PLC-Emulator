/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instruction;

import com.vts.jplceditor.ld.instruction.timer.Ton;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Vusivus
 */
public class TimerTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        Ton timer = new Ton();

        Scene scene = new Scene(timer, 300, 250);

        primaryStage.setTitle("Hello World!");
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
