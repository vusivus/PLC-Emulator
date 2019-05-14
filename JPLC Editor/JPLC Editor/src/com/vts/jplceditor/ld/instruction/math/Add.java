/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.instruction.math;

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
public class Add extends AnchorPane implements Initializable, Instruction {

    @FXML
    private TextField in2TF;
    @FXML
    private TextField in1TF;
    @FXML
    private TextField destTF;
    private EventHandler<DragEvent> dragDropped;
    private EventHandler<DragEvent> dragOver;
    private EventHandler<DragEvent> dragEntered;
    private EventHandler<DragEvent> dragExited;
    private Point2D dragOffset;
    private boolean entered;
    private List<PLCOperand> parameters;
    private final BackgroundFill fillDragOver;

    public Add() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource(IR.Path_ADD));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {

        }
        parameters = new ArrayList<>();
        parameters.add(new PLCOperand(PLCDataType.BIT, "EN"));
        parameters.add(new PLCOperand(PLCDataType.BIT, "ENO"));
        parameters.add(new PLCOperand(PLCDataType.INT, "In1"));
        parameters.add(new PLCOperand(PLCDataType.INT, "In2"));
        parameters.add(new PLCOperand(PLCDataType.INT, "Dest"));
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
                            if (event.getTarget().equals(in1TF)) {
                                in1TF.setText(name);
                            } else if (event.getTarget().equals(in2TF)) {
                                in2TF.setText(name);
                            } else if (event.getTarget().equals(destTF)) {
                                destTF.setText(name);
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
                    if (event.getTarget().equals(in1TF)) {
                        in1TF.setBackground(new Background(fillDragOver));
                    }
                    if (event.getTarget().equals(in2TF)) {
                        in2TF.setBackground(new Background(fillDragOver));
                    }
                    if (event.getTarget().equals(destTF)) {
                        destTF.setBackground(new Background(fillDragOver));
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
                    if (event.getTarget().equals(in1TF)) {
                        in1TF.setBackground(Background.EMPTY);
                    } else if (event.getTarget().equals(in2TF)) {
                        in2TF.setBackground(Background.EMPTY);
                    } else if (event.getTarget().equals(destTF)) {
                        destTF.setBackground(Background.EMPTY);
                    }
                }
                event.consume();
            }
        };

        in1TF.setOnDragOver(dragOver);
        in1TF.setOnDragDropped(dragDropped);
        in1TF.setOnDragEntered(dragEntered);
        in1TF.setOnDragExited(dragExited);
        in2TF.setOnDragOver(dragOver);
        in2TF.setOnDragDropped(dragDropped);
        in2TF.setOnDragEntered(dragEntered);
        in2TF.setOnDragExited(dragExited);
        destTF.setOnDragOver(dragOver);
        destTF.setOnDragDropped(dragDropped);
        destTF.setOnDragEntered(dragEntered);
        destTF.setOnDragExited(dragExited);
    }

    @Override
    public AnchorPane getInstance() {
        return this;
    }

    @Override
    public PLCInstruction getPLCInstruction() {
        parameters.get(2).setIdentity(in1TF.getText());
        parameters.get(3).setIdentity(in2TF.getText());
        parameters.get(4).setIdentity(destTF.getText());
        PLCInstruction instruction = new PLCInstruction(PLCOperator.ADD);
        instruction.setOperands(parameters);
        return instruction;
    }

    private boolean validType(String type) {
        return type.equals(DragDataFormatter.TagType_INT) || type.equals(DragDataFormatter.TagType_BYTE) || type.equals(DragDataFormatter.TagType_FLOAT);
    }

    @Override
    public void setParameters(List<PLCOperand> parameters) {
       this.parameters = parameters; 
       in1TF.setText(parameters.get(2).getIdentity());
       in2TF.setText(parameters.get(3).getIdentity());
       destTF.setText(parameters.get(4).getIdentity());
    }
}
