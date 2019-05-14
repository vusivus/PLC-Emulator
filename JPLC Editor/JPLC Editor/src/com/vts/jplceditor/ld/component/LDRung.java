/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.component;

import com.vts.jplceditor.ld.component.node.INodeChangeListener;
import com.vts.jplceditor.ld.component.node.SNode;
import com.vts.jplceditor.ld.layout.Size;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 *
 * @author Vusivus
 */
public class LDRung extends Region implements INodeChangeListener{

    private Point2D sPoint, tPoint, cornerPoint;
    private LDLineManager rungLines;
    private LDInstructionManager instructions;
    private Group branches;
    private Group instructionGroup;
    private Size size;
    private final SNode sNode;
    private ContextMenu menu;
    private String selected;

    /**
     *
     * @param sNode The source node where this rung will start from
     * @param size the dimensions of the rung
     */
    public LDRung(SNode sNode, Size size, int iCount) {
        this.sNode = sNode;
        this.size = size;
        sPoint = sNode.getLocation();
        tPoint = sPoint.add(size.getWidth(), 0);
        cornerPoint = sPoint.subtract(0, size.getHeight() / 2);
        setPrefWidth(size.getWidth());
        setPrefHeight(size.getHeight());
        rungLines = new LDLineManager();
        instructions = new LDInstructionManager(sPoint);
        branches = new Group();
        instructionGroup = new Group();
        instructionGroup.getChildren().add(instructions);
        getChildren().add(branches);
        createDefaultRung(iCount);
        buildMenu();
    }

    public LDRung(LDInstructionManager instructions, Size size, SNode sNode) {
        this.instructions = instructions;
        this.size = size;
        this.sNode = sNode;
        sPoint = sNode.getLocation();
        tPoint = sPoint.add(size.getWidth(), 0);
        cornerPoint = sPoint.subtract(0, size.getHeight() / 2);
        setPrefWidth(size.getWidth());
        setPrefHeight(size.getHeight());
        rungLines = new LDLineManager();
        getChildren().add(rungLines);
        redrawRung();
        buildMenu();
    }

    /**
     *
     * @return
     */
    public SNode getsNode() {
        return sNode;
    }

    public LDInstructionManager getInstructions() {
        return instructions;
    }

    public void removeSelected() {
        int index = instructions.instructionIndex(selected);
        if (index >= 0) {
            LDInstruction i = instructions.getInstructionList().get(index);
            i.removeInstruction();
        }
    }

    private void createDefaultRung(int iCount) {
        getChildren().add(rungLines);
        //getChildren().add(instructions);
        if (iCount == 2) {
            double dist = tPoint.getX() - sPoint.getX();
            LDInstruction i1 = new LDInstruction(sPoint.add(dist * 0.25f, 0));
            LDInstruction i2 = new LDInstruction(sPoint.add(dist * 0.75f, 0));
            i1.registerChangeListener(this);
            i2.registerChangeListener(this);
            instructions.addInstruction(0, i1);
            instructions.addInstruction(1, i2);
            sNode.addChild(i1);
            sNode.addChild(i2);

            rungLines.addLine(sPoint, i1.getStartPoint());
            rungLines.addLine(i1.getEndPoint(), i2.getStartPoint());
            rungLines.addLine(i2.getEndPoint(), tPoint);
        } else if (iCount == 1) {
            double dist = tPoint.getX() - sPoint.getX();
            LDInstruction i1 = new LDInstruction(sPoint.add(dist * 0.33f, 0));
            i1.registerChangeListener(this);
            instructions.setMinInstructionCount((short) iCount);
            instructions.addInstruction(0, i1);
            sNode.addChild(i1);

            rungLines.addLine(sPoint, i1.getStartPoint());
            rungLines.addLine(i1.getEndPoint(), tPoint);
        }

    }

    public void redrawRung() {
        rungLines.clearAll();
        rungLines.addLine(sPoint, instructions.getInstructionList().get(0).getStartPoint());
        Point2D p1, p2;
        int count = instructions.count();
        for (int i = 0; i < count; i++) {
            if (i != instructions.count() - 1) {
                p1 = instructions.getInstructionList().get(i).getEndPoint();
                p2 = instructions.getInstructionList().get(i + 1).getStartPoint();
                rungLines.addLine(p1, p2);
            }
        }

        p1 = instructions.getInstructionList().get(count - 1).getEndPoint();
        rungLines.addLine(p1, tPoint);
    }

    /**
     *
     * Invoked whenever an INode is updated with an instruction.
     * <p>
     * adds a blank instruction node</p>
     *
     * @param nodeID
     */
    @Override
    public void onInstructionAdded(String nodeID) {
        int index = instructions.instructionIndex(nodeID);
        if (index > -1) {
            Point2D p = instructions.nextFreeSlot(index, tPoint);
            LDInstruction in = new LDInstruction(p);
            in.registerChangeListener(this);
            p = tPoint.add(0, 0);
            instructions.addInstruction(index + 1, in);
            tPoint = instructions.getTPoint();
            sNode.addChild(in);
            redrawRung();
        }
    }

    public Group getBranches() {
        return branches;
    }

    /**
     *
     * @param nodeID
     */
    @Override
    public void onInstructionRemoved(String nodeID) {
        int index = instructions.instructionIndex(nodeID);

        if (index > -1) {
            Point2D p = tPoint.add(0, 0);
            instructions.removeInstruction(index);
            tPoint = instructions.getTPoint();
            redrawRung();
        } else {
            System.out.println("Failed to remove instruction with ID: " + nodeID
                    + "It's index " + index + " is out of bounds.");
        }
    }

    public void cleanUp() {
        for (int i = 0; i < instructions.getInstructionList().size(); i++) {
            if (!instructions.getInstructionList().get(i).hasInstruction()) {
                instructions.removeInstruction(i);
                redrawRung();
            }
        }
    }

    public void relocateToPoint(Point2D point) {
        instructions.shiftInstrY(point.getY()-sPoint.getY(), 0);
        sPoint = point;
        tPoint = sPoint.add(size.getWidth(), 0);
        cornerPoint = sPoint.subtract(0, size.getHeight() / 2);
        relocate(cornerPoint.getX(), cornerPoint.getY());
        redrawRung();
    }

    public void setSize(Size size) {
        this.size = size;
        tPoint = sPoint.add(size.getWidth(), 0);
        cornerPoint = sPoint.subtract(0, size.getHeight() / 2);
    }

    public Size getSize() {
        return size;
    }

    public Point2D getStartPoint() {
        return sPoint;
    }

    public Point2D getEndPoint() {
        return tPoint;
    }

    public Point2D getCornerPoint() {
        return cornerPoint;
    }

    public List<LDInstruction> getInstructionList() {
        return instructions.getInstructionList();
    }

    @Override
    public void onInstructionClicked(String nodeId, MouseEvent event) {
        selected = nodeId;
        int index = instructions.instructionIndex(selected);
        if (event.getButton() == MouseButton.PRIMARY) {
            instructions.selectOnly(index);
        } else if (event.getButton() == MouseButton.SECONDARY) {
            Node node = instructions.getInstructionList().get(index);
            menu.show(node, event.getScreenX(), event.getSceneY());
        }

    }
    
    private void buildMenu() {
        menu = new ContextMenu();

        MenuItem delete = new MenuItem("delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = instructions.instructionIndex(selected);
                if (index >= 0) {
                    instructions.removeInstruction(index);
                    redrawRung();
                }

            }
        });
        MenuItem remove = new MenuItem("remove instruction");
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = instructions.instructionIndex(selected);
                if (index >= 0) {
                    LDInstruction i = instructions.getInstructionList().get(index);
                    i.removeInstruction();
                }

            }
        });
        menu.getItems().addAll(delete, remove);
    }
    public Group getInstructionGroup() {
        return instructionGroup;
    }


}
