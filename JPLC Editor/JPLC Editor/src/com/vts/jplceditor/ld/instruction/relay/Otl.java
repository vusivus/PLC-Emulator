/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.instruction.relay;

import com.vts.jplceditor.compiler.plc.PLCDataType;
import com.vts.jplceditor.compiler.plc.PLCInstruction;
import com.vts.jplceditor.compiler.plc.PLCOperator;
import com.vts.jplceditor.compiler.plc.PLCOperand;
import com.vts.jplceditor.ld.instruction.IR;
import com.vts.jplceditor.ld.instruction.DragDataFormatter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import com.vts.jplceditor.ld.instruction.Instruction;

/**
 *
 * @author Vusumuzi_Tshabangu
 */
public class Otl extends AnchorPane implements Instruction, Initializable {

    @FXML
    Label tagName;
    @FXML
    private Rectangle dragOverIndicator;
    @FXML
    private Line left_line;
    @FXML
    private Line right_line;
    @FXML
    private Line left_bar;
    @FXML
    private Line right_bar;
    @FXML
    private Line center_bar;

    private EventHandler<DragEvent> dragDropped;
    private EventHandler<DragEvent> dragOver;
    private EventHandler<DragEvent> dragEntered;
    private EventHandler<DragEvent> dragExited;
    private Point2D dragOffset;
    private boolean entered;
    private List<PLCOperand> parameters;

    /**
     *
     */
    public Otl() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource(IR.Path_OTL));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {

        }
        parameters = new ArrayList<>();
        parameters.add(new PLCOperand(PLCDataType.BIT, "EN"));
        parameters.add(new PLCOperand(PLCDataType.BIT, "ENO"));
        parameters.add(new PLCOperand(PLCDataType.BIT,tagName.getText()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                    if (entered) {
                        relocateTo(new Point2D(event.getSceneX() - dragOffset.getX(),
                                event.getSceneY() - dragOffset.getY()));
                    }
                }
                event.consume();
            }
        };

        dragDropped = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureTarget() == this) {
                    entered = false;
                }
                /* data dropped */
 /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    String data = db.getString();
                    String dataType = DragDataFormatter.getTagType(data);
                    if (dataType.equals(DragDataFormatter.TagType_BIT)) {
                        String name = DragDataFormatter.getTagName(data);
                        tagName.setText(name);
                    }
                }
                /* let the source know whether the string was successfully
* transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        };

        dragEntered = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != this
                        && event.getDragboard().hasString()) {
                    dragOverIndicator.setFill(Color.BLUE);
                }
                event.consume();
            }
        };
        dragExited = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != this
                        && event.getDragboard().hasString()) {
                    dragOverIndicator.setFill(Color.WHITE);
                }
                event.consume();
            }
        };

        setOnDragOver(dragOver);
        setOnDragDropped(dragDropped);
        setOnDragEntered(dragEntered);
        setOnDragExited(dragExited);
    }

    @Override
    public AnchorPane getInstance() {
        return this;
    }

    @Override
    public PLCInstruction getPLCInstruction() {
        parameters.get(2).setIdentity(tagName.getText());
        PLCInstruction instruction = new PLCInstruction(PLCOperator.OTL);
        instruction.setOperands(parameters);
        return instruction;
    }
    
    @Override
    public void setParameters(List<PLCOperand> parameters) {
       this.parameters = parameters; 
       tagName.setText(parameters.get(2).getIdentity());
    }
}
