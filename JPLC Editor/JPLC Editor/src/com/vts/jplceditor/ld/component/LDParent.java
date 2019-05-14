/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.ld.component;

import java.util.List;

/**
 *
 * @author Vusivus
 */
public interface LDParent {
    public void addChild(LDNode child);
    public void atChildAt(int index,LDNode child);
    public List<LDNode> getChildren();
}
