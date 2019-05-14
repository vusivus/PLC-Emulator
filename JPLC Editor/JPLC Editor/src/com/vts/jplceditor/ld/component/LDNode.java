/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.component;

import com.vts.jplceditor.ld.component.node.NodeType;

/**
 *
 * @author Vusivus
 */
public interface LDNode {
   public void setInDegree(int inDegree);
   public void setOutDegree(int outDegree);
   public int getInDegree();
   public int getOutDegree();
   public NodeType getNodeType();
}
