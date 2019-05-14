/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.component.node;

import javafx.scene.input.MouseEvent;

/**
 *
 * @author Vusivus
 */
public interface INodeChangeListener {
   public void onInstructionAdded(String nodeID);
   public void onInstructionRemoved(String nodeID);
   public void onInstructionClicked(String nodeId,MouseEvent event);
}
