/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.component.node;

import com.vts.jplceditor.ld.component.LDNode;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 *
 * @author Vusivus
 */
public class JNode extends Vertex implements LDNode {

    private int inDegree, outDegree;

    public JNode(Point2D location) {
        super(location);
        setNodeType(NodeType.Join);
        setFill(Color.GREEN);
    }
@Override
    public void setInDegree(int inDegree) {
        this.inDegree = inDegree;
    }

    @Override
    public void setOutDegree(int outDegree) {
        this.outDegree = outDegree;
    }

    @Override
    public int getInDegree() {
        return inDegree;
    }

    @Override
    public int getOutDegree() {
        return outDegree;
    }

    @Override
    public NodeType getNodeType(){
        return NodeType.Join;
    }
}
