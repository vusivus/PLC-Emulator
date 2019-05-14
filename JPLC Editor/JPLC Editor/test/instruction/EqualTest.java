/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instruction;

import com.vts.jplceditor.ld.instruction.DragDataFormatter;
import com.vts.jplceditor.ld.instruction.compare.Equ;
import com.vts.jplceditor.ld.instruction.timer.Ton;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Vusivus
 */
public class EqualTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        Equ equ = new Equ();

        Pane p = new Pane();
        p.getChildren().add(equ);
        String text = DragDataFormatter.formatTag("temp", DragDataFormatter.TagType_INT);
        final Text source = new Text(200, 300, text);
        source.setScaleX(2.0);
        source.setScaleY(2.0);

        source.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                /* allow any transfer mode */
                Dragboard db = source.startDragAndDrop(TransferMode.COPY);

                /* put a string on dragboard */
                ClipboardContent content = new ClipboardContent();
                content.putString(source.getText());
                db.setContent(content);

                event.consume();
            }
        });

        source.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.COPY) {
                    source.setText("");
                }

                event.consume();
            }
        });
        p.getChildren().add(source);
        Scene scene = new Scene(p, 480, 360);

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
