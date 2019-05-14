/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.component;

import com.vts.jplceditor.compiler.plc.PLCOperator;
import com.vts.jplceditor.compiler.plc.PLCOperatorDecoder;
import com.vts.jplceditor.ld.component.node.INodeChangeListener;
import com.vts.jplceditor.ld.component.node.NodeType;
import com.vts.jplceditor.ld.instruction.Instruction;
import com.vts.jplceditor.ld.instruction.InstructionUtil;
import com.vts.jplceditor.ld.layout.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Vusivus
 */
public class LDInstruction extends Region implements LDNode {

    private Point2D startPoint, endPoint, cornerPoint;
    private EventHandler<DragEvent> dragEntered;
    private EventHandler<DragEvent> dragExited;
    private EventHandler<DragEvent> dragDropped;
    private EventHandler<DragEvent> dragOver;
    private EventHandler<MouseEvent> mouseClicked;
    private final BackgroundFill fill, fillDragOver, fillDragDropped;
    private Size size = new Size(40, 20);
    private boolean hasInstruction, selected;
    private final List<INodeChangeListener> listeners;
    private int inDegree, outDegree;
    private Instruction instruction;
    private Rectangle selection;

    /**
     * <p>
     * creates an InstructionNode block centered at this point</p>
     *
     * @param startPoint
     */
    public LDInstruction(Point2D startPoint) {
        this.startPoint = startPoint;

        //Creates the default blue color of the square block
        fill = new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY);
        fillDragOver = new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY);
        fillDragDropped = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(fill);
        setBackground(background);

        endPoint = startPoint.add(size.getWidth(), 0);
        cornerPoint = startPoint.subtract(0, size.getHeight() / 2);
        setLayoutX(cornerPoint.getX());
        setLayoutY(cornerPoint.getY());
        setPrefWidth(size.getWidth());
        setPrefHeight(size.getHeight());
        //Assigns a unique ID to this Node
        setId(UUID.randomUUID().toString());
        listeners = new ArrayList<>();
        createDragHandlers();

    }

    /**
     * An INode change listener is notified every time this node is assigned an
     * Instruction
     *
     * @param listener
     */
    public void registerChangeListener(INodeChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Assigns an Instruction to this INode
     *
     * @param operator
     */
    public void setInstruction(PLCOperator operator) {
        if (!getChildren().isEmpty()) {
            getChildren().remove(0);
        }

        if (operator != PLCOperator.NULL) {
            setBackground(new Background(fillDragDropped));
            instruction = InstructionUtil.getInstruction(operator.name());
            AnchorPane node = instruction.getInstance();
            size.setWidth(node.getPrefWidth());
            size.setHeight(node.getPrefHeight());
            setPrefWidth(size.getWidth());
            setPrefHeight(size.getHeight());

            getChildren().add(0, node);

            relocateToPoint(startPoint);
            hasInstruction = true;

            if (listeners.size() > 0) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onInstructionAdded(getId());
                }
            }

        }

    }

    public void select() {
        if (!selected && hasInstruction) {
            double x, y;
            x = cornerPoint.subtract(10, 10).getX();
            y = cornerPoint.subtract(10, 10).getY();
            selection = new Rectangle(x, y, Color.TRANSPARENT);
            selection.setStroke(Color.BLUE);
            selection.setWidth(size.getWidth() + 10);
            selection.setHeight(size.getHeight() + 10);
            getChildren().add(1, selection);
            selected = true;
        }

    }

    public void deselect() {
        if (selected) {
            getChildren().remove(1);
            selected = false;
        }
    }

    public void removeInstruction() {
        if (!getChildren().isEmpty()) {
            getChildren().remove(0);
            size.setHeight(20);
            size.setWidth(40);
            setPrefWidth(40);
            setPrefHeight(20);
            relocateToPoint(startPoint);
            setBackground(new Background(fill));
            instruction = null;
            hasInstruction = false;
        }

        if (listeners.size() > 0) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onInstructionRemoved(getId());
            }
        }
    }

    public Point2D getStartPoint() {
        return startPoint;
    }

    public Point2D getEndPoint() {
        return endPoint;
    }

    public void relocateToPoint(Point2D point) {
        startPoint = point;
        endPoint = startPoint.add(size.getWidth(), 0);
        cornerPoint = startPoint.subtract(0, size.getHeight() / 2);
        relocate(cornerPoint.getX(), cornerPoint.getY());
    }

    public void setSize(Size size) {
        this.size = size;
        endPoint = startPoint.add(size.getWidth(), 0);
        cornerPoint = startPoint.subtract(0, size.getHeight() / 2);
    }

    public Size getSize() {
        return size;
    }

    public Point2D getCornerPoint() {
        return cornerPoint;
    }

    public boolean hasInstruction() {
        return hasInstruction;
    }

    private void createDragHandlers() {
        dragEntered = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != this
                        && event.getDragboard().hasString()) {
                    if (getChildren().isEmpty()) {
                        setBackground(new Background(fillDragOver));
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
                    if (getChildren().isEmpty()) {
                        setBackground(new Background(fill));
                    }
                }
                event.consume();
            }
        };

        dragOver = new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                if (event.getGestureSource() != this
                        && event.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }

                event.consume();
            }
        };

        dragDropped = new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    String name = db.getString();
                    PLCOperator i = PLCOperatorDecoder.fromID(name);
                    if (i != null) {
                        
                        setInstruction(i);
                    }

                    event.setDropCompleted(true);
                    event.consume();
                }
            }
        };

        mouseClicked = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (INodeChangeListener listener : listeners) {
                    listener.onInstructionClicked(getId(), event);
                }
            }
        };
        setOnDragEntered(dragEntered);
        setOnDragExited(dragExited);
        setOnDragDropped(dragDropped);
        setOnDragOver(dragOver);
        setOnMouseClicked(mouseClicked);
    }

    public Instruction getInstruction() {
        return instruction;
    }

    @Override
    public void setInDegree(int inDegree) {

    }

    @Override
    public void setOutDegree(int outDegree) {

    }

    @Override
    public int getInDegree() {
        return 1;
    }

    @Override
    public int getOutDegree() {
        return 0;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.Instruction;
    }

}
