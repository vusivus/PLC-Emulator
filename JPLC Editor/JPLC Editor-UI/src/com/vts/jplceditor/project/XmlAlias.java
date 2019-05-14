/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.project;

import com.thoughtworks.xstream.XStream;
import com.vts.jplceditor.compiler.plc.PLCOperand;
import com.vts.jplceditor.project.io.IOInstruction;
import com.vts.jplceditor.project.io.IOLD;
import com.vts.jplceditor.project.io.IONode;
import com.vts.jplceditor.project.io.IORung;

/**
 *
 * @author Vusivus
 */
public class XmlAlias {

    /**
     *
     * @return
     */
    public static XStream projectAlias() {
        XStream xs = new XStream();
        xs.alias("Project", SavableProject.class);
        xs.alias("LadderDiagram", IOLD.class);
        xs.alias("Rung", IORung.class);
        xs.alias("Node", IONode.class);
        xs.alias("Instruction", IOInstruction.class);
        xs.alias("Parameter", PLCOperand.class);
        return xs;
    }
}
