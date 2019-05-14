/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vts.jplceditor.project;

import com.vts.jplceditor.compiler.plc.PLCOperand;
import com.vts.jplceditor.project.io.IOLD;
import java.util.List;

/**
 *
 * @author Vusivus
 */
public class SavableProject {

    String directory;
    String projectTitle;
    List<IOLD> ladderDiagramList;
    List<PLCOperand> tagList;

    /**
     *
     * @return
     */
    public String getDirectory() {
        return directory;
    }

    /**
     *
     * @param directory
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     *
     * @return
     */
    public String getProjectTitle() {
        return projectTitle;
    }

    /**
     *
     * @param projectTitle
     */
    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    /**
     *
     * @return
     */
    public List<IOLD> getLadderDiagramList() {
        return ladderDiagramList;
    }

    /**
     *
     * @param ladderDiagramList
     */
    public void setLadderDiagramList(List<IOLD> ladderDiagramList) {
        this.ladderDiagramList = ladderDiagramList;
    }

    /**
     *
     * @return
     */
    public List<PLCOperand> getTagList() {
        return tagList;
    }

    /**
     *
     * @param tagList
     */
    public void setTagList(List<PLCOperand> tagList) {
        this.tagList = tagList;
    }

}
