/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.instruction.compare;

import com.vts.jplceditor.compiler.plc.PLCDataType;
import com.vts.jplceditor.compiler.plc.PLCInstruction;
import com.vts.jplceditor.compiler.plc.PLCOperand;
import com.vts.jplceditor.compiler.plc.PLCOperator;
import com.vts.jplceditor.ld.instruction.DragDataFormatter;
import com.vts.jplceditor.ld.instruction.IR;
import com.vts.jplceditor.ld.instruction.Instruction;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Vusman
 */
public class Lim extends AnchorPane implements Initializable, Instruction {

    @FXML
    private TextField lowTF;
    @FXML
    private TextField testTF;
    @FXML
    private TextField highTF;
    private EventHandler<DragEvent> dragDropped;
    private EventHandler<DragEvent> dragOver;
    private EventHandler<DragEvent> dragEntered;
    private EventHandler<DragEvent> dragExited;
    private Point2D dragOffset;
    private boolean entered;
    private List<PLCOperand> parameters;
    private final BackgroundFill fillDragOver;

    public Lim() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource(IR.Path_LIM));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {

        }
        parameters = new ArrayList<>();
        parameters.add(new PLCOperand(PLCDataType.BIT, "EN"));
        parameters.add(new PLCOperand(PLCDataType.BIT, "ENO"));
        parameters.add(new PLCOperand(PLCDataType.INT, "low"));
        parameters.add(new PLCOperand(PLCDataType.INT, "test"));
        parameters.add(new PLCOperand(PLCDataType.INT, "high"));
        fillDragOver = new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buildHandlers();
    }

    public void relocateTo(Point2D p) {
        Point2D point = getParent().sceneToLocal(p);
        relocate(point.getX(), point.getY());
    }

    private void buildHandlers() {

        dragOver = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != this
                        && event.getDragboard().hasString()) {
                    /* allow for moving */
                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
            }
        };

        dragDropped = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureTarget() != this) {
                    /* data dropped */
 /* if there is a string data on dragboard, read it and use it */
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasString()) {

                        String data = db.getString();
                        String dataType = DragDataFormatter.getTagType(data);
                        if (validType(dataType)) {
                            String name = DragDataFormatter.getTagName(data);

                            if (event.getTarget().equals(lowTF)) {
                                lowTF.setText(name);
                            }
                            if (event.getTarget().equals(testTF)) {
                                testTF.setText(name);
                            }
                            if (event.getTarget().equals(highTF)) {
                                highTF.setText(name);
                            }
                        }
                    }
                    /* let the source know whether the string was successfully
* transferred and used */
                    event.setDropCompleted(success);

                    event.consume();
                }
            }
        };

        dragEntered = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != this
                        && event.getDragboard().hasString()) {
                    if (event.getTarget().equals(lowTF)) {
                        lowTF.setBackground(new Background(fillDragOver));
                    } else if (event.getTarget().equals(testTF)) {
                        testTF.setBackground(new Background(fillDragOver));
                    } else if (event.getTarget().equals(highTF)) {
                        highTF.setBackground(new Background(fillDragOver));
                    }
                }
                event.consume();
            }
        };
        dragExited = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != this
                        && event.getDragboard().hasString()) {
                    if (event.getTarget().equals(lowTF)) {
                        lowTF.setBackground(Background.EMPTY);
                    } else if (event.getTarget().equals(testTF)) {
                        testTF.setBackground(Background.EMPTY);
                    } else if (event.getTarget().equals(highTF)) {
                        highTF.setBackground(Background.EMPTY);
                    }
                }
                event.consume();
            }
        };

        lowTF.setOnDragOver(dragOver);
        lowTF.setOnDragDropped(dragDropped);
        lowTF.setOnDragEntered(dragEntered);
        lowTF.setOnDragExited(dragExited);
        testTF.setOnDragOver(dragOver);
        testTF.setOnDragDropped(dragDropped);
        testTF.setOnDragEntered(dragEntered);
        testTF.setOnDragExited(dragExited);
        highTF.setOnDragOver(dragOver);
        highTF.setOnDragDropped(dragDropped);
        highTF.setOnDragEntered(dragEntered);
        highTF.setOnDragExited(dragExited);
    }

    @Override
    public AnchorPane getInstance() {
        return this;
    }
@Override
    public PLCInstruction getPLCInstruction() {
        parameters.get(2).setIdentity(lowTF.getText());
        parameters.get(3).setIdentity(testTF.getText());
        parameters.get(4).setIdentity(highTF.getText());
        PLCInstruction instruction = new PLCInstruction(PLCOperator.LIM);
        instruction.setOperands(parameters);
        return instruction;
    }

    private boolean validType(String type) {
        return type.equals(DragDataFormatter.TagType_INT) || type.equals(DragDataFormatter.TagType_BYTE) || type.equals(DragDataFormatter.TagType_FLOAT);
    }
    
    @Override
    public void setParameters(List<PLCOperand> parameters) {
       this.parameters = parameters; 
       lowTF.setText(parameters.get(2).getIdentity());
       testTF.setText(parameters.get(3).getIdentity());
       highTF.setText(parameters.get(4).getIdentity());
    }
}
