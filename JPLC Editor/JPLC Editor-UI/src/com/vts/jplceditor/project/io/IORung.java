/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.project.io;

import com.vts.jplceditor.ld.layout.Size;
import java.util.List;

/**
 *
 * @author Vusivus
 */
public class IORung {
    private Size size;
    private IONode sNode;
    private List<IOInstruction> instructions;

    /**
     *
     * @param sNode
     */
    public IORung(IONode sNode) {
        this.sNode = sNode;
    }

    /**
     *
     * @return
     */
    public Size getSize() {
        return size;
    }

    /**
     *
     * @param size
     */
    public void setSize(Size size) {
        this.size = size;
    }

    /**
     *
     * @return
     */
    public IONode getsNode() {
        return sNode;
    }

    /**
     *
     * @param sNode
     */
    public void setsNode(IONode sNode) {
        this.sNode = sNode;
    }

    /**
     *
     * @return
     */
    public List<IOInstruction> getInstructions() {
        return instructions;
    }

    /**
     *
     * @param instructions
     */
    public void setInstructions(List<IOInstruction> instructions) {
        this.instructions = instructions;
    }
        
}
