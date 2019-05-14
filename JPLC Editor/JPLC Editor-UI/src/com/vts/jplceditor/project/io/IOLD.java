/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.project.io;

import java.util.List;

/**
 *
 * @author Vusivus
 */
public class IOLD {
    List<IORung> rungs;
    List<IONode> sNodes;

    /**
     *
     */
    public IOLD() {
    }

    /**
     *
     * @return
     */
    public List<IORung> getRungs() {
        return rungs;
    }

    /**
     *
     * @param rungs
     */
    public void setRungs(List<IORung> rungs) {
        this.rungs = rungs;
    }

    /**
     *
     * @return
     */
    public List<IONode> getsNodes() {
        return sNodes;
    }

    /**
     *
     * @param sNodes
     */
    public void setsNodes(List<IONode> sNodes) {
        this.sNodes = sNodes;
    }
       
}
