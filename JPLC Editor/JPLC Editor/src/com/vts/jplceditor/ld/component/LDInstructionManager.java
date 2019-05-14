/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.component;

import com.vts.jplceditor.ld.layout.Size;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.Group;

/**
 *
 * @author Vusivus
 */
public class LDInstructionManager extends Group {

    private List<LDInstruction> instructionList;
    public static int SPACING = 100;
    private Point2D sPoint;
    private short minInstructionCount = 2;

    /**
     *
     * @param origin
     */
    public LDInstructionManager(Point2D origin) {
        instructionList = new ArrayList<>();
        sPoint = origin;
    }

    /**
     * Highlights the provided instruction
     *
     * @param position
     */
    public void selectOnly(int position) {
        if (contained(position)) {
            for (LDInstruction i : instructionList) {
                i.deselect();
            }
            instructionList.get(position).select();
        }
    }

    public Point2D startPointOf(String nodeID) {
        int index = instructionIndex(nodeID);
        if (contained(index)) {
            return instructionList.get(index).getStartPoint();
        } else {
            return null;
        }
    }

    public Point2D endPointOf(String nodeID) {
        int index = instructionIndex(nodeID);
        if (contained(index)) {
            return instructionList.get(index).getEndPoint();
        } else {
            return null;
        }
    }

    public Size SizeOf(String nodeID) {
        int index = instructionIndex(nodeID);
        if (contained(index)) {
            return instructionList.get(index).getSize();
        } else {
            return null;
        }
    }


    /**
     *
     * @param position
     * @param inode
     */
    public void addInstruction(int position, LDInstruction inode) {
        instructionList.add(position, inode);
        getChildren().add(position, inode);
    }

    /**
     * Remves the instruction at the position provided
     *
     * @param position
     */
    public void removeInstruction(int position) {
        if (instructionList.size() > minInstructionCount) { //if we have more than 2 instructions
            if (position < instructionList.size()) {
                instructionList.remove(position);
                getChildren().remove(position);
                shiftAfterRemove(position);   // shift instructions to the left after remove
            } else {
                System.out.println("Could not remove instruction aat position " + position
                        + "Instruction index is not within the bounds " + instructionList.size());
            }
        } else {
            System.out.println("Could not remove instruction aat position " + position);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public int instructionIndex(String id) {
        int index = -1;
        String identity;
        for (int i = 0; i < instructionList.size(); i++) {
            identity = instructionList.get(i).idProperty().get();
            if (identity.equals(id)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     *
     * @param amount
     * @param start
     */
    public void shiftInstrX(double amount, int start) {
        if (contained(start)) {
            Point2D p;
            for (int i = start; i < instructionList.size(); i++) {
                p = instructionList.get(i).getStartPoint();
                instructionList.get(i).relocateToPoint(p.add(amount, 0));
            }
        } else {
            System.out.println("Cannot shift uncontained position " + start);
        }
    }

    public void shiftInstrY(double amount, int start) {
        if (contained(start)) {
            Point2D p;
            for (int i = start; i < instructionList.size(); i++) {
                p = instructionList.get(i).getStartPoint();
                instructionList.get(i).relocateToPoint(p.add(0, amount));
            }
        } else {
            System.out.println("Cannot shift uncontained position " + start);
        }
    }

    public Point2D getTPoint() {
        return instructionList.get(count() - 1).getEndPoint();
    }

    /**
     * <p>
     * Calculates a point to the right where a new instruction node can. be
     * placed.</p>
     *
     * @param point The index of the instruction to add from
     * @param tPoint The termination point of the rung
     * @return
     */
    public Point2D nextFreeSlot(int point, Point2D tPoint) {
        Point2D p = null;
        if (point < instructionList.size() - 1) {
            Point2D p1, p2;
            p1 = instructionList.get(point).getEndPoint();
            p2 = instructionList.get(point + 1).getStartPoint();
            double dist = Math.abs(p1.getX() - p2.getX());
            double min = 2 * SPACING + 40;
            if (dist < SPACING) {
                shiftInstrX(min - dist, point + 1);
            }
            double x, y;
            x = p1.getX() + SPACING;
            y = p1.getY();
            p = new Point2D(x, y);
        } else if (point == instructionList.size() - 1) {
            Point2D p1;
            p1 = instructionList.get(point).getEndPoint();
            double dist = Math.abs(p1.getX() - tPoint.getX());
            double min = 2 * SPACING + 40;
            if (dist < min) {
                shiftInstrX(min - dist, point + 1);
            }
            double x, y;
            x = p1.getX() + SPACING;
            y = p1.getY();
            p = new Point2D(x, y);
        }
        return p;
    }

    /**
     *
     * @return
     */
    public List<LDInstruction> getInstructionList() {
        return instructionList;
    }

    public void setInstructionList(List<LDInstruction> instructionList) {
        this.instructionList = instructionList;
    }
 
    public void clearAll(){
        instructionList.clear();
        this.getChildren().clear();
    }
    /**
     *
     * @return
     */
    public int count() {
        return instructionList.size();
    }

    /**
     *
     * @param index
     * @return
     */
    public boolean contained(int index) {
        return index < instructionList.size();
    }

    private void shiftAfterRemove(int position) {
        if (contained(position)) {
            double p1, p2;
            if (position > 0) {

                p1 = instructionList.get(position - 1).getEndPoint().getX();
            } else {
                p1 = sPoint.getX();
            }
            p2 = instructionList.get(position).getStartPoint().getX();
            double dist = p2 - p1;
            if (dist > SPACING) {
                shiftInstrX(SPACING - dist, position);
            }
        } else {
            System.out.println("Cannot shift instructions about uncontained position " + position);
        }
    }

    public void setMinInstructionCount(short minInstructionCount) {
        this.minInstructionCount = minInstructionCount;
    }

}
